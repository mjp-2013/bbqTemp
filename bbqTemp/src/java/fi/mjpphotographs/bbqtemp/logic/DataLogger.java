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
import java.io.File;
import java.util.logging.Level;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;



import org.apache.commons.configuration.DefaultConfigurationBuilder;
import org.apache.log4j.Logger;

/**
 *
 * @author Mikko
 */
public class DataLogger implements Runnable
{

    static Logger logger = Logger.getLogger( DataLogger.class );
    private final Thread t;
    private volatile boolean threadRun = false;
    private TemperatureDAO tempDAO;
    private Configuration bbqTempConfig= null;
    

    /**
     * Tells if this object thread is running.
     *
     * @return
     */
    public boolean isPolling()
    {
        return this.threadRun;
    }

    public void startPolling()
    {
        t.start();
    }

    public void stopPolling()
    {
        threadRun = false;
    }

    public DataLogger()
    {
        DefaultConfigurationBuilder builder = new DefaultConfigurationBuilder();
        builder.setFile( new File( "bbq-config.xml" ) );
        try       
        {
            bbqTempConfig = builder.getConfiguration( true );
 
        }
        catch ( ConfigurationException ex )
        {
            logger.error( "Cannot read XML configuration file.", ex );
            throw new IllegalArgumentException ("Cannot read XML configuration file. Consult logs for more information", ex);
        }

      

        tempDAO = new TemperatureDaoImpl();

        t = new Thread( this, "bbqPollerThread" );
    }

    @Override
    public void run()
    {
        threadRun = true;
        TemperatureDevice tempDevice = new Max31855( 0 );


        while ( threadRun )
        {
            Temperature tempData = tempDevice.getTemperature();
            try
            {
                // Check for configuraiotn if additional sensors are on
                // ADD meat Sensor(s)
                // ADD ambient sensor

                tempDAO.insertTemperature( tempData );
            }
            catch ( DAOException ex )
            {
                logger.error( "Error occured while storing temperature data to persistent storage", ex );
            }

            try
            {
                Thread.sleep( 2000 );
            }
            catch ( InterruptedException ie )
            {
                //loggia
            }

        }

    }
}
