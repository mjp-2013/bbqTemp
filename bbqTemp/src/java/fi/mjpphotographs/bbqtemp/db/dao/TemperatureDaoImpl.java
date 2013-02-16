/**
 * Copyright 2013 M.Pitkänen
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package fi.mjpphotographs.bbqtemp.db.dao;

import fi.mjpphotographs.bbqtemp.db.DB;
import fi.mjpphotographs.bbqtemp.io.Temperature;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Mikko
 */
public class TemperatureDaoImpl implements TemperatureDAO
{

    /**
     * SQL queries for simplier editing...
     */
    /**
     * SQL for inserting main temperature (MJ,RJ,AMBIENT,DATETIME) values to
     * database.
     */
    static final String SQL_INSERT_TEMPERATURE_LOG_DATA = "insert into templog (mjTemp, rjTemp, ambientTemp, loggedTime) values (?,?,?,?)";
    static final String SQL_SELECT_AMOUNT_LATEST_TEMPERATURES = "select * from templog order by id desc limit ?";
    static final String SQL_SELECT_DATERANGE_LATEST_TEMPERATURES = "select * from templog WHERE loggedtime BETWEEN CAST(? AS timestamp) AND CAST(? AS timestamp) order by id desc";
    static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger( TemperatureDaoImpl.class );

    @Override
    public Temperature getLatestTemperature() throws DAOException
    {
        ArrayList<Temperature> temperatures = getTemperatures( 1 );

        if ( temperatures.isEmpty() )
        {
            return new Temperature();
        }
        else
        {
            return ( Temperature ) temperatures.get( 0 );
        }
    }

    @Override
    public ArrayList<Temperature> getTemperatures( int amount ) throws DAOException
    {
        Connection conn = DB.getConnection();
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        ArrayList temperatures = new ArrayList();

        try
        {

            ps = conn.prepareStatement( SQL_SELECT_AMOUNT_LATEST_TEMPERATURES );
            ps.setInt( 1, amount );
            resultSet = ps.executeQuery();

            while ( resultSet.next() )
            {

                long id = resultSet.getLong( "id" );
                float mjTemp = resultSet.getFloat( "mjtemp" );
                float rjTemp = resultSet.getFloat( "rjtemp" );
                float ambientTemp = resultSet.getFloat( "ambienttemp" );
                Date logDatetime = new java.util.Date( resultSet.getTimestamp( "loggedtime" ).getTime() );

                Temperature temperature = new Temperature( id, mjTemp, rjTemp, ambientTemp, logDatetime );
                temperatures.add( temperature );
            }

        }
        catch ( SQLException ex )
        {
            throw new DAOException( "SQL Exception occured during inserting temperature data into database.", ex );
        }
        finally
        {

            if ( resultSet != null )
            {
                try
                {
                    resultSet.close();
                }
                catch ( SQLException ex )
                {
                    throw new DAOException( "Error occured while closing resultset object.", ex );
                }
            }

            if ( ps != null )
            {
                try
                {
                    ps.close();
                }
                catch ( SQLException ex )
                {
                    throw new DAOException( "Error occured while closing prepared statement object", ex );
                }
            }

            DB.closeConnection( conn );

        }

        return temperatures;


    }

    @Override
    public ArrayList<Temperature> getTemperatures( Date start, Date end ) throws DAOException
    {
        if ( start == null || end == null )
        {
            throw new IllegalArgumentException( "Date range start or end object(s) is null." );
        }

        Connection conn = DB.getConnection();
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        ArrayList temperatures = new ArrayList();

        try
        {

            ps = conn.prepareStatement( SQL_SELECT_DATERANGE_LATEST_TEMPERATURES );
            ps.setTimestamp( 1, new Timestamp( start.getTime() ) );
            ps.setTimestamp( 2, new Timestamp( end.getTime() ) );
            resultSet = ps.executeQuery();

            while ( resultSet.next() )
            {

                long id = resultSet.getLong( "id" );
                float mjTemp = resultSet.getFloat( "mjtemp" );
                float rjTemp = resultSet.getFloat( "rjtemp" );
                float ambientTemp = resultSet.getFloat( "ambienttemp" );
                Date logDatetime = new java.util.Date( resultSet.getTimestamp( "loggedtime" ).getTime() );

                Temperature temperature = new Temperature( id, mjTemp, rjTemp, ambientTemp, logDatetime );
                temperatures.add( temperature );
            }

        }
        catch ( SQLException ex )
        {
            throw new DAOException( "SQL Exception occured during inserting temperature data into database.", ex );
        }
        finally
        {

            if ( resultSet != null )
            {
                try
                {
                    resultSet.close();
                }
                catch ( SQLException ex )
                {
                    throw new DAOException( "Error occured while closing resultset object.", ex );
                }
            }

            if ( ps != null )
            {
                try
                {
                    ps.close();
                }
                catch ( SQLException ex )
                {
                    throw new DAOException( "Error occured while closing prepared statement object", ex );
                }
            }

            DB.closeConnection( conn );

        }

        return temperatures;
    }

    /**
     *
     * @param temperature
     * @throws SQLException
     */
    @Override
    public void insertTemperature( Temperature temperature ) throws DAOException
    {

        if ( temperature != null )
        {
            Connection conn = DB.getConnection();
            PreparedStatement ps = null;
            ResultSet generatedKeys = null;

            try
            {
                //@todo NULL CHECK FOR VALUES HERE!!!!

                ps = conn.prepareStatement( SQL_INSERT_TEMPERATURE_LOG_DATA, Statement.RETURN_GENERATED_KEYS );
                ps.setDouble( 1, temperature.getMjTemperature() );
                ps.setDouble( 2, temperature.getRjTemperature() );
                ps.setDouble( 3, temperature.getAmbientTemperature() );

                java.sql.Timestamp logTime = new java.sql.Timestamp( temperature.getLogDatatime().getTime() );

                ps.setTimestamp( 4, logTime );

                int resultRows = ps.executeUpdate();

                if ( resultRows == 0 )
                {
                    throw new DAOException( "Temperature data db insert failed. Zero row affected." );
                }

                generatedKeys = ps.getGeneratedKeys();
                if ( generatedKeys.next() )
                {
                    logger.debug( "Temperature datas inserted into database. RowID was:" + generatedKeys.getLong( 1 ) );
                }
                else
                {
                    throw new SQLException( "Creating user failed, no generated key obtained." );
                }

            }
            catch ( SQLException ex )
            {
                throw new DAOException( "SQL Exception occured during inserting temperature data into database.", ex );
            }
            finally
            {

                if ( generatedKeys != null )
                {
                    try
                    {
                        generatedKeys.close();
                    }
                    catch ( SQLException ex )
                    {
                        throw new DAOException( "Error occured while closing resultset object.", ex );
                    }
                }

                if ( ps != null )
                {
                    try
                    {
                        ps.close();
                    }
                    catch ( SQLException ex )
                    {
                        throw new DAOException( "Error occured while closing prepared statement object", ex );
                    }
                }
                DB.closeConnection( conn );

            }

        }
        else
        {
            // should this be NPE instead?
            throw new IllegalArgumentException( "Temperature parameter object was null." );
        }

    }
}
