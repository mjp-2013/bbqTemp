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


package fi.mjpphotographs.bbqtemp.db.pojo;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.Date;

class TemperatureLog
{
    
    private int id;
    private float rjTemp;
    private float ambientTemp;
    private java.util.Date loggedTime;
    private float mjTemp;

    TemperatureLog(){}
    
   
    TemperatureLog(float rjTemp, float mjTemp, float ambientTemp){
        this.mjTemp = mjTemp;
        this.rjTemp = rjTemp;
        this.ambientTemp = ambientTemp;
        this.loggedTime = new java.util.Date();
    }
    
    
    public void setId( int id )
    {
        this.id = id;
    }

    public void setMjTemp( float mjTemp )
    {
        this.mjTemp = mjTemp;
    }

    public void setRjTemp( float rjTemp )
    {
        this.rjTemp = rjTemp;
    }

    public void setAmbientTemp( float ambientTemp )
    {
        this.ambientTemp = ambientTemp;
    }

    public void setLoggedTime( Date loggedTime )
    {
        this.loggedTime = loggedTime;
    }

    public int getId()
    {
        return id;
    }

    public float getMjTemp()
    {
        return mjTemp;
    }

    public float getRjTemp()
    {
        return rjTemp;
    }

    public float getAmbientTemp()
    {
        return ambientTemp;
    }

    public Date getLoggedTime()
    {
        return loggedTime;
    }
    
    
    
    
    
}
