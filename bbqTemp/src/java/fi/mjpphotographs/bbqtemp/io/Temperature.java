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

import java.util.Date;
import java.util.HashMap;

/**
 *
 * @author Mikko
 */
public class Temperature
{

    public Temperature(){}
    
    
    /**
     * Thermocouple measuring temperature
     */
    private float mjTemperature;
  /**
     * Thermocouple reference temperature
     */
    private float rjTemperature;
    /**
     * Temperature from meat sensor1
     */
    private float meatTemperature1;
    /**
     * Temperature from meat sensor2
     */
    private float meatTemperature2;
    /**
     * Ambient air temperature (e.g. from ds1820)
     */
    private float ambientTemperature;
    
    /**
     * Temperature measurement date time.
     */
    private Date logDatatime;

    public Date getLogDatatime()
    {
        return logDatatime;
    }

    public void setLogDatatime( Date logDatatime )
    {
        this.logDatatime = logDatatime;
    }
    
    
    /**
     * Key value pair of multiple sensor data. First one is temperature sensor
     * identifier and second is float value of temperature.
     */
    private HashMap temperatures;
    
    public float getMjTemperature()
    {
        return mjTemperature;
    }

    public void setMjTemperature( float mjTemperature )
    {
        this.mjTemperature = mjTemperature;
    }

    public float getRjTemperature()
    {
        return rjTemperature;
    }

    public void setRjTemperature( float rjTemperature )
    {
        this.rjTemperature = rjTemperature;
    }

    public float getMeatTemperature1()
    {
        return meatTemperature1;
    }

    public void setMeatTemperature1( float meatTemperature1 )
    {
        this.meatTemperature1 = meatTemperature1;
    }

    public float getMeatTemperature2()
    {
        return meatTemperature2;
    }

    public void setMeatTemperature2( float meatTemperature2 )
    {
        this.meatTemperature2 = meatTemperature2;
    }

    public float getAmbientTemperature()
    {
        return ambientTemperature;
    }

    public void setAmbientTemperature( float ambientTemperature )
    {
        this.ambientTemperature = ambientTemperature;
    }

    public HashMap getTemperatures()
    {
        return temperatures;
    }

    public void setTemperatures( HashMap temperatures )
    {
        this.temperatures = temperatures;
    }
  
}
