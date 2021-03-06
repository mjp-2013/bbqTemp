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
import fi.mjpphotographs.bbqtemp.db.dao.TemperatureDaoImpl;
import fi.mjpphotographs.bbqtemp.io.Max31855;
import fi.mjpphotographs.bbqtemp.io.Temperature;
import fi.mjpphotographs.bbqtemp.io.TemperatureDevice;
import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;

/**
 *
 * @author Mikko
 */
public class DataLogger implements Runnable
{

    static Logger logger = Logger.getLogger( DataLogger.class );
    //private final Thread t;
    private Thread t;
    
    private volatile boolean threadRun = false;
    private TemperatureDAO tempDAO;
    private Configuration bbqTempConfig = null;
    private ControlEngine fanControlEngine = null;

    /**
     * Tells if Datalogger/logic thread is running.
     *
     * @return
     */
    public boolean isPolling()
    {
        return this.threadRun;
    }

    /**
     * Starts poller thread.
     */
    public void startPolling()
    {
        t.start();
    }

    /**
     * Stops poller thread from running.
     */
    public void stopPolling()
    {
        threadRun = false;
        
        logger.debug("Thread stop polling called.");
        
        t.interrupt();

        // Wait until the thread exits
        try
        {
            t.join();
            logger.debug("Thread join executed");
        }
        catch ( InterruptedException ex )
        {
         
            logger.error("BBQ Thread shutdown failed.");
            
            //System.exit( 1 );
        }
        t= null;
    }

    public DataLogger( Configuration bbqTempConfig )
    {
       this.bbqTempConfig = bbqTempConfig;

        tempDAO = new TemperatureDaoImpl();
        logger.debug( "Temperature DAO loaded succesfully." );

        //TODO possible changeable implementation for different devices (select implementation by configuration and class loader)
        /*
         * Creates new Fan control object. 
         */
        fanControlEngine = new FanControl();
        logger.debug( "FanControl loaded succesfully." );

        t = new Thread( this, "bbqPollerThread" );
        logger.debug( "Poller thread initialized succesfully." );
    }

    @Override
    public void run()
    {
        logger.debug( "Datalogger polling thread entered to run state." );

        threadRun = true;
        TemperatureDevice tempDevice = new Max31855( 0 );

        logger.debug( "Temperature device MAX31855 initialized." );


        fanControlEngine.initControlEngine( bbqTempConfig, tempDAO );

        logger.debug( "FanControl enige initialized correctly." );

        while ( threadRun )
        {
            Temperature tempData = tempDevice.getTemperature();
            try
            {
                // TODO Check for configuraiotn if additional sensors are on
                // TODO ADD meat Sensor(s)
                // TODO ADD ambient sensor

                tempDAO.insertTemperature( tempData );
            }
            catch ( DAOException ex )
            {
                logger.error( "Error occured while storing temperature data to persistent storage", ex );
            }

            //starts and stops the device by its implementation.
            fanControlEngine.handleDevice();

            try
            {
                //TODO interval form confing..
                Thread.sleep( 2000 );
            }
            catch ( InterruptedException ie )
            {
                logger.error( "Thread interrupted.", ie );
            }

        }

        // when thread exists shutdown the ControlEngine and clean everything.
        fanControlEngine.shutDown();
    }
}
