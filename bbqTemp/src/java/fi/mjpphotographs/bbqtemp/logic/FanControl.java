/**
 * Copyright 2013 M.J.Pitkänen
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
package fi.mjpphotographs.bbqtemp.logic;

import fi.mjpphotographs.bbqtemp.db.dao.DAOException;
import fi.mjpphotographs.bbqtemp.db.dao.TemperatureDAO;
import fi.mjpphotographs.bbqtemp.io.FanDevice;
import fi.mjpphotographs.bbqtemp.io.Temperature;
import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;

/**
 *
 * @author MjP
 */
public class FanControl implements ControlEngine
{
    static Logger logger = Logger.getLogger( FanControl.class );
    
    
    /**
     * Fan device object which handles the actual JNI calls to GPIO board.
     */
    FanDevice fan = null;
    
    /**
     * Is the FanControl initialized.
     */
    private boolean initalized = false;

    /**
     * Configuration object for BBQTemp.
     */
    private Configuration bbqTempConfig = null;
    
    public FanControl(){}
   
    /**
     * Fan CutOff temperature
     */
    private float maxTemperature = 0;
    
    /**
     * Max temperature in smoker
     */
    private float targetTemperature = 0; 

    /**
     * Temperature dao object for persistent activity.
     */
    private TemperatureDAO tempDAO;
    
    @Override
    public void initControlEngine( Configuration bbqTempConfig, TemperatureDAO tempDAO )
    {
        logger.debug( "Entering init ControlEngine." );
        if (!initalized)
        {
            logger.debug( "Control engine initializing started." );
            
            this.bbqTempConfig = bbqTempConfig;
            fan = new FanDevice( this.bbqTempConfig );   
            logger.debug( "Fan Device initalized." );
            //TODO get max and min values from config
            maxTemperature = 30;
            targetTemperature = 28;
            
            // refrence to TemperatureDAO object.
            this.tempDAO  =tempDAO;
            
            initalized = true;
        }
        else
        {
          logger.error( "FanControl already initialized." );
          throw new IllegalStateException ("FanControl already initalized.");  
        }   
   
    }

    @Override
    public void handleDevice()
    {
        logger.debug("Device handler called.");
        
        Temperature temperature = null;
        try
        {
            //TODO get latest temp values from DB
            
            temperature = tempDAO.getLatestTemperature();
        }
        catch ( DAOException ex )
        {
           logger.error( "Cannot retrieve latest temperature data from data persistance.",ex );
           //TODO DEVICE EXPTION HERE...
        }
        
        
        //TODO better logic
        
        
        if (null != temperature && temperature.getMjTemperature() < this.maxTemperature )
        {
            fan.startFan();
            logger.debug(  "Fan started. Latest reading:" + temperature.getMjTemperature() );
        }
        else if (null != temperature && temperature.getMjTemperature() >= this.maxTemperature )
        {
            fan.stopFan();
            logger.debug("Fan stopped. Latest reading:"+ temperature.getMjTemperature());
        }
        else
        {
 
            logger.debug("Fan exception. Latest reading:"+ temperature.getMjTemperature());
            throw new IllegalArgumentException ("Temperature object was null");
            
        }
  
    }

  
    @Override
    public boolean isInitialized()
    {
        return this.initalized;
    }

    @Override
    public void shutDown()
    {
        fan.shutDown();
    }
}
