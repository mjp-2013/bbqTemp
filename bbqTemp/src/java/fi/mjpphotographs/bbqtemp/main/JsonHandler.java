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

import com.google.gson.Gson;
import fi.mjpphotographs.bbqtemp.db.dao.DAOException;
import fi.mjpphotographs.bbqtemp.db.dao.TemperatureDAO;
import fi.mjpphotographs.bbqtemp.db.dao.TemperatureDaoImpl;
import fi.mjpphotographs.bbqtemp.io.Temperature;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
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

    static Logger logger = Logger.getLogger( JsonHandler.class );
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

        if ( null != jsonActionName && jsonActionName.equalsIgnoreCase( "getXhourlyData" ) )
        {
            String hoursStr = request.getParameter( "hours" );
            int hours = 1; //defaults for one hour graph
           
            if ( hoursStr != null )
            {
                try
                {
                  hours = Integer.parseInt( hoursStr );    
                }
                catch(NumberFormatException e) { 
                     // todo exception or loggin
                    // nothing do ... continues with default value
                }
            } 
            getData( response, hours * 60 );
        }
        
        else if (null != jsonActionName && jsonActionName.equalsIgnoreCase( "getConfigurationData" ))
        {
           // calls for json representation of configuration data
            getConfigData(response, request);
        }
        else if (null != jsonActionName && jsonActionName.equalsIgnoreCase( "setConfigurationData" ))
        {
          // sets json configuration with new vales. Used only for main switches etc.
            setConfigData(response, request);
        }
        else if (null != jsonActionName && jsonActionName.equalsIgnoreCase( "getDataByName" ))
        {
          // Get json data for specific items, like main page trending items etc. 
            getDataByName(response, request);
        }
        
       
    }

   /**
    * REturns commong configuration items (if no specific parameter is requested with request parameter cfgParamName)
    * @param response
    * @param request 
    */
   private void getConfigData(HttpServletResponse response,HttpServletRequest request){
        
        //TODO only selected item to config map..
       
        // collect common configs to this map and send it to client as json
        Map <String, String>commonCfg = new HashMap();
        commonCfg.put("max_temperature",bbqTempConfig.getString( "max_temperature") );
        commonCfg.put("temperature_unit",bbqTempConfig.getString( "temperature_unit") );
        
       
        response.setContentType( "application/json" );
        response.setCharacterEncoding( "UTF-8" );
        try
        {
            response.getWriter().write( new Gson().toJson( commonCfg ) );
        }
        catch ( IOException ex )
        {
             logger.error("Error while writing Configuration GSON to response.", ex );
 
        }
       
   } 
   
   private void setConfigData(HttpServletResponse response, HttpServletRequest request){
       
       //TODO value validity checks here...
       String maxTemperature = request.getParameter( "maxTemperatureValue" );
       String temperatureUnit = request.getParameter( "tempTypeValue");
       
       logger.debug("Tried to set configuration value:max_temperature to:" +maxTemperature );
       logger.debug("Tried to set configuration value:temperature_unit to:" +temperatureUnit );
       bbqTempConfig.setProperty(  "max_temperature", maxTemperature );
       bbqTempConfig.setProperty(  "temperature_unit", temperatureUnit );
       logger.debug("Read configuration max_temperature value after set:" + bbqTempConfig.getFloat( "max_temperature") );
       logger.debug("Read configuration temperature_unit value after set:" + bbqTempConfig.getString( "temperature_unit") );
 
       
        response.setContentType( "text/html" );
        response.setCharacterEncoding( "UTF-8" );
        try
        {
            //TODO JSON OUTPUT RESULT:ok
            response.getWriter().write( "<result>ok</result>" );
        }
        catch ( IOException ex )
        {
             logger.error("Error while writing respone xml to ajax request", ex );
 
        }
       
      
   
   }
    
   private void getDataByName(HttpServletResponse response, HttpServletRequest request){
       //TODO implementation
   }
   
   
    
    /**
     * Returns selected hours of temperature data.
     *
     * @param request
     * @param response
     * @param minutes how many minutes of data is returned. -1 for all data.
     */
    private void getData( HttpServletResponse response, int minutes )
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
        //Making tick amount for about 30 per chart.Datalogger logs about 30 logs per minute.
        int averageMultiplier = minutes / 2;
        
        temperatures = averageData( averageMultiplier, temperatures );
        logger.debug("Chart array size: " + temperatures.size() + " average multplier size was:"+averageMultiplier);
        
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
