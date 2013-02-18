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
package fi.mjpphotographs.bbqtemp.main;

import fi.mjpphotographs.bbqtemp.db.dao.DAOException;
import fi.mjpphotographs.bbqtemp.db.dao.TemperatureDAO;
import fi.mjpphotographs.bbqtemp.db.dao.TemperatureDaoImpl;
import fi.mjpphotographs.bbqtemp.io.Temperature;
import fi.mjpphotographs.bbqtemp.logic.DataLogger;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;

/**
 * Json support
 *
 * @author MjP
 */
class JsonHandler
{

    static Logger logger = Logger.getLogger( DataLogger.class );
    private TemperatureDAO tempDAO;
    private Configuration bbqTempConfig = null;

    public JsonHandler( Configuration bbqTempConfig )
    {
        this.bbqTempConfig = bbqTempConfig;
        logger.debug( "Configurations loaded succesfully in JsonHandler." );

        // initializes temperature DAO (uses same implementation as Datalogger. 
        // Will split this to own DAO when methods are ready.
        tempDAO = new TemperatureDaoImpl();
        logger.debug( "Temperature DAO loaded succesfully in JsonHandler." );
    }

    public void executeRequest( HttpServletRequest request, HttpServletResponse response )
    {
        String jsonActionName = request.getParameter( "json" );

        if ( null != jsonActionName && jsonActionName.equalsIgnoreCase( "getLastHourData" ) )
        {
            getData( request, response, 60 );
        }
        else if ( null != jsonActionName && jsonActionName.equalsIgnoreCase( "getAlltimeData" ) )
        {
            getData( request, response, -1 );
        }
    }

    /**
     * Returns selected hours of temperature data.
     *
     * @param request
     * @param response
     * @param minutes how many minutes of data is returned. -1 for all data.
     */
    private void getData( HttpServletRequest request, HttpServletResponse response, int minutes )
    {
        List<Temperature> temperatures = new ArrayList();
        Date startDate = null;
        if ( minutes == -1 )
        {
            //TODO think better aproach of this, possibilities for errors.. RPI resets its time epoch or last used time (fake hardware clock), but this should get all data if db is not corrupted or datime is not chaged pre 1970s.
            startDate = new Date( 0 );  
        }
        else
        {
            // get selected amount of minutes
            startDate = new Date( System.currentTimeMillis() - minutes * 60 * 1000 );

        }

        try
        {
            temperatures = tempDAO.getTemperatures( startDate, new Date() );
        }
        catch ( DAOException ex )
        {
            logger.error( "Data query failed for json data", ex );
            //TODO exception
        }

        //Making datagroup much smaller (calculating averages of every minute log events or so)
        temperatures = averageData( 30, temperatures );

        String json = constructJsonFromTemperatures( temperatures );

        response.setContentType( "application/json" );
        response.setCharacterEncoding( "UTF-8" );
        try
        {
            response.getWriter().write( json );
        }
        catch ( IOException ex )
        {
            logger.error( "Error while writing JSON data to Response object.", ex );

            logger.debug( "JSON response data:" + json );
        }
    }

    public String constructJsonFromTemperatures( List<Temperature> temperatures )
    {
        StringBuilder sb = new StringBuilder();

        sb.append( "[{\"data\":[" );

        for ( int i = 0; i < temperatures.size(); i++ )
        {
            SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd h:mma" );

            String formatedTimestamp = sdf.format( temperatures.get( i ).getLogDatatime() );

            sb.append( "[\"" ).append( formatedTimestamp ).append( "\"" );

            sb.append( "," ).append( temperatures.get( i ).getMjTemperature() ).append( "]" );

            if ( i + 1 < temperatures.size() )
            {
                sb.append( "," );
            }

        }
        sb.append( "]}]" );

        return sb.toString();
    }

    /**
     * This method is used for averaging temperature data arrays
     * (List<Temperature>). Mainly used for chart drawing purposes. This is
     * thread safe implementation (creates new/clone objects for output list)
     *
     * @param groupSize How many samples is taken for average.
     * @param temperatures List of Temperature objects.
     * @return Returns new array of Temperature objects which has been averaged.
     * The first occurence of logDatetime of current group is used for time.
     */
    private List<Temperature> averageData( int groupSize, List<Temperature> temperatures )
    {
        List<Temperature> returnArray = new ArrayList();
        int j = 0;
        float sum = 0;

        Date firstDate = null;
        for ( int i = 0; i < temperatures.size(); i++ )
        {
            if ( j == 0 )
            {
                firstDate = temperatures.get( i ).getLogDatatime();
            }
            if ( j < groupSize )
            {
                sum += temperatures.get( i ).getMjTemperature();
                j++;
            }

            if ( j == groupSize )
            {
                Temperature tmpTemperature = new Temperature();
                tmpTemperature.setMjTemperature( (sum / groupSize) );
                tmpTemperature.setLogDatatime( ( Date ) firstDate.clone() );
                returnArray.add( tmpTemperature );
                j = 0;
                sum = 0;
            }
        }
        if ( sum > 0 )
        {
            Temperature tmpTemperature = new Temperature();
            tmpTemperature.setMjTemperature( (sum / j) );
            tmpTemperature.setLogDatatime( firstDate );
            returnArray.add( tmpTemperature );
        }

        return returnArray;
    }
}
