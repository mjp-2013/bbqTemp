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

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import org.apache.commons.configuration.Configuration;

/**
 *
 * @author Mikko
 */
public class FanDevice
{

    /**
     * Controls fan pin.
     */
    private GpioPinDigitalOutput fanIO = null;
    /**
     * Instance to GPIO controls from factory
     */
    final GpioController gpioController = GpioFactory.getInstance();

    
    /**
     * 
     * @param bbqTempConfig 
     */
    public FanDevice( Configuration bbqTempConfig )
    {
        //TODO PIN FROM CONFIGURATION
        Pin fanPinNumber = RaspiPin.GPIO_00;
              
        // sets selected pin to output mode and set pins state to low. 
        this.fanIO = gpioController.provisionDigitalOutputPin(fanPinNumber ,"BBQ Fan 1", PinState.LOW);   
    }
    
    /**
     * Starts fan
     */
    public void startFan()
    {
        
    }
    
    public void stopFan()
    {
        
    }
    
    /**
     * Returns fan state low or high (off or on)
     * @return 
     */
    public PinState getFanState()
    {
        return this.gpioController.getState( this.fanIO );
    }
    
}
