/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.mjpphotographs.bbqtemp.main;

import fi.mjpphotographs.bbqtemp.logic.DataLogger;
import java.io.File;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.DefaultConfigurationBuilder;
import org.apache.log4j.Logger;

/**
 * This class is used for simplified configuration loading outside the main servlet/jsp page
 * @author MjP
 */
public class ConfigHelper
{
    private Configuration bbqTempConfig = null;
    
    static Logger logger = Logger.getLogger( DataLogger.class );
    
    
    public ConfigHelper (File bbqConfigFilePath)
    {
         DefaultConfigurationBuilder builder = new DefaultConfigurationBuilder();


        //TODO SEVERE config files need to be in home directory. This must be fixed to point meta-inf or web-inf directory...

        builder.setFile( bbqConfigFilePath );
        try
        {
            bbqTempConfig = builder.getConfiguration( true );

        }
        catch ( ConfigurationException ex )
        {
            logger.error( "Cannot read XML configuration file.", ex );
            throw new IllegalArgumentException( "Cannot read XML configuration file. Consult logs for more information", ex );
        }

    }
    
    public Configuration getConfiguration(){
        return this.bbqTempConfig;
    }
    
}
