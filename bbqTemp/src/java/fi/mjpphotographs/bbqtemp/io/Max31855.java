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

import com.pi4j.wiringpi.Spi;
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

    // TODO refecator byte array from sensor without String bit/binary hack to temp values.
    @Override
    public Temperature getTemperature()
    {

   
        byte[] spiData = new byte[ 4 ];
        Temperature temperature = null;
        
        //TODO get pin from init...
        
        int setupReturnValue = Spi.wiringPiSPISetup( 1, 1000000 );
        if ( setupReturnValue != -1 )
        {
            int rwReturnValue = Spi.wiringPiSPIDataRW( 1, spiData, 4 );
            if (rwReturnValue!= -1 ){
                temperature = convertSPIData( spiData );
            }
            else
            {
                logger.error("Error occured during SPI read/write." );
                //TODO exception
            }        
        }
        else
        {
            logger.error( "Error occured during SPI setup.");
        }


        return temperature;
    }

    //TODO re-write this
    /**
     * This need some work (it works but the inner work is quite hard to read).
     * Converts first 4 bytes of byte array to binary string.
     * @param bytes (byte array of 4 bytes)
     * @return Returns binary string of 4 bytes.
     */
    private String toBinary( byte[] bytes )
    {
        StringBuilder sb = new StringBuilder( 32 );

        for ( int i = 0; i < 32; i++ )
        {
            sb.append( (bytes[i / 8] << i % 8 & 0x80) == 0 ? '0' : '1' );
        }
        return sb.toString();
    }

    //TODO exception throwing if MAX31855 failure bits are set.
    
   
    
    /**
     * Convert SPI byte array data (4bytes, 32bits) to Temperature object (MJ and RJ temperatures suplied) Needs heavy refactoring due very hackish code...
     * @param spiData
     * @return 
     */
    private Temperature convertSPIData( byte[] spiData )
    {

        // Look for MAX31855 datasheet for binary details.
        
        Temperature temperature = new Temperature();
        
        String spiDataString  = toBinary ( spiData);
        
        // TODO better errorhandling..
        if ( spiDataString.substring( 15, 16).equalsIgnoreCase( "1") )
        {
            logger.error("SPI device reported failure.");
            //TODO exception and error logging for: OC,SCG,SCV faults.
        }
        
        // for MT temperatures from MAX 31885.
        String mjTemp = toBinary( spiData ).substring( 0, 14 );
        String integerValueMj = mjTemp.substring( 1, 12 );
        String negativeBitMj = mjTemp.substring( 0, 1 );
        String decimalValueMj = mjTemp.substring( 12 );
        float mj = getFloat (integerValueMj,decimalValueMj,negativeBitMj, 0.25f );
        
        // for RJ temperatures from MAX 31885
        String rjTemp = toBinary( spiData ).substring( 16, 28 );
        String integerValueRj = rjTemp.substring( 1, 8 );
        String negativeBitRj = rjTemp.substring( 0, 1 );
        String decimalValueRj = rjTemp.substring(8 );
        float rj = getFloat (integerValueRj,decimalValueRj,negativeBitRj, 0.0625f );

        temperature.setMjTemperature( mj );
        temperature.setRjTemperature( rj );
        temperature.setLogDatatime( new java.util.Date() );
       
        logger.debug ("spiData:" + toBinary (spiData));
        logger.debug( "MJ:"+mj+" RJ:"+ rj );
        return temperature;
    }
    
    /**
     * Support method for converting different bit-space and decimal values 
     * @param integerValueBin
     * @param decimalValueBin
     * @param negativeBit
     * @param decimalMultiplier What LSB each bit means in temperature
     * @return 
     */
    private float getFloat(String integerValueBin, String decimalValueBin, String negativeBit, float decimalMultiplier ){
        float returnValue =0;
        returnValue = Integer.parseInt( integerValueBin, 2 );
         
        float decimals = decimalMultiplier * Integer.parseInt( decimalValueBin, 2 );
        returnValue += decimals;

        if ( negativeBit.equalsIgnoreCase( "1" ) )
        {
            returnValue *= -1f;
        }       
        return returnValue;
    }
    
}
