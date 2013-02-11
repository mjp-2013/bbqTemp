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
package fi.mjpphotographs.bbqtemp.io;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Date;
import org.apache.log4j.Logger;

/**
 *
 * @author Mikko
 */
public class Max31855 implements TemperatureDevice
{

    static Logger logger = Logger.getLogger( Max31855.class );
    /**
     * Max31855 chanel number default is 0;
     */
    private int channel = 0;

    public Max31855( int channel )
    {
        this.channel = channel;
    }

    // TODO from config the path and move to pi4j as soon as possible
    @Override
    public Temperature getTemperature()
    {
        Temperature tempData = new Temperature();
        try
        {

            Process process = Runtime.getRuntime().exec( "/home/pi/temp/max31855/max31855.py" );
            DataInputStream dataInputStream = new DataInputStream( process.getInputStream() );
            try
            {
                String tempValues;
                // TODO replace with pi4j
                // deprecated, need to fix this...
                tempValues = dataInputStream.readLine();

                if ( tempValues != null )
                {
                    float rj;
                    float mj;
                    int position = tempValues.indexOf( ":" );
                    mj = Float.parseFloat( tempValues.substring( 0, position - 1 ) );
                    rj = Float.parseFloat( tempValues.substring( position + 1 ) );

                    tempData.setMjTemperature( mj );
                    tempData.setRjTemperature( rj );
                    tempData.setLogDatatime( new java.util.Date() );
                }

            }
            catch ( IOException e )
            {
                logger.fatal( "IO-error during the reading of temperature values from SPI device via python script.", e );
            }
        }
        catch ( IOException e1 )
        {
            logger.fatal( "Cannot find or access python script.", e1 );
        }

        return tempData;
    }
}
