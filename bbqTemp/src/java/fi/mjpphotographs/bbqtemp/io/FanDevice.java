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
package fi.mjpphotographs.bbqtemp.io;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;

/**
 * Fan control via pi4j.
 * @author MjP
 */
public class FanDevice
{

    //TODO EXCEPTIONS 
    
    static Logger logger = Logger.getLogger( FanDevice.class );
    
    /**
     * Controls fan pin.
     */
    private GpioPinDigitalOutput fanIO = null;
    /**
     * Instance to GPIO controller from factory
     */
    final GpioController gpioController = GpioFactory.getInstance();

    /**
     * Constructs fan device object.
     * @param bbqTempConfig
     */
    public FanDevice( Configuration bbqTempConfig )
    {
        logger.debug( "FanDevice initialization started." );
        //TODO PIN FROM CONFIGURATION
        Pin fanPinNumber = RaspiPin.GPIO_00;

         logger.debug( "FanDevice Pin set" );
        
        //todo read calibration from config
        
        // sets selected pin to output mode and set pins state to low. 
        this.fanIO = gpioController.provisionDigitalOutputPin( fanPinNumber, "BBQ Fan 1", PinState.LOW );
        // sets pin to low state.
        fanIO.low();
        
        logger.debug( "FanIO GPIO contoller set as output and state as low." );
    
    }

     /*
     * Starts the fan if it is stopped. Otherwise the state is not changed.
     */
    public void startFan()
    {
        if ( fanIO.isLow() )
        {
            fanIO.high();
        }

    }

    /*
     * Stops the fan if it is running. Otherwise the state is not changed.
     */
    public void stopFan()
    {
           if ( fanIO.isHigh() )
        {
            fanIO.low();
        }
    }

    /**
     * Returns fan state low or high (off or on)
     *
     * @return Fan state as PinSate.
     */
    public PinState getFanState()
    {
        return this.gpioController.getState( this.fanIO );
    }

    /**
     * Shutdowns the GPIO controll
     */
    public void shutDown()
    {
        if ( fanIO.isHigh() )
        {
            fanIO.low();
        }
        fanIO.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
        try
        {
            // wait 10 seconds, not sure is this mandatory (no javadoc for shutdown command at mo.
            Thread.sleep(10000);
        }
        catch ( InterruptedException ex )
        {
          logger.debug( "Shutting down wait state ßcanceled",ex );  
        }
        
        gpioController.shutdown();
    }
}
