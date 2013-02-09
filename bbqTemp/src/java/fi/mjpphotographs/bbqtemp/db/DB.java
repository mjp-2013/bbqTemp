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
/**
 * DB connection handling
 */
package fi.mjpphotographs.bbqtemp.db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 *
 * @author Mikko
 */
public class DB
{

    private static DataSource dataSource;
    
static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger( Class.class.getName() );
    

    static
    {
        try
        {
            InitialContext cxt = new InitialContext();
            if ( cxt == null ) {
                
                   logger.fatal("Inital context not found.");
            
            }

             dataSource = (DataSource) cxt.lookup( "java:/comp/env/jdbc/bbqdb" );

            logger.debug("Datasource obtained:" + dataSource);
            if ( dataSource == null ) {
  
                   logger.fatal( "Datasource not found.");
            }
        }
        catch ( NamingException ex )
        {
           logger.fatal("Inital Context not found.", ex); 
        }
        
        
    }

  
            
            
            

            
    
    public static Connection getConnection()
    {
        Connection conn = null;
        if ( dataSource != null )
        {
            try
            {
                conn = dataSource.getConnection();
            }
            catch ( SQLException sqle )
            {
                logger.fatal("Connection not found:" + sqle);
            }
        }
        
        logger.debug( "Database connection" +  conn );
        return conn;
    }

    public static void closeConnection( Connection conn )
    {
        if ( conn != null )
        {
            try
            {
                conn.close();
            }
            catch ( SQLException ex )
            {
                //log4j
            }
            finally
            {
                conn = null;
            }
        }
    }
}