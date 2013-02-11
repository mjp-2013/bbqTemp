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
package fi.mjpphotographs.bbqtemp.db.dao;

import fi.mjpphotographs.bbqtemp.io.Temperature;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Mikko
 */
public interface TemperatureDAO
{
    
   
    public void insertTemperature ( Temperature temperature) throws DAOException;
    
    public Temperature getLatestTemperature() throws DAOException;;
    
    public ArrayList<Temperature> getTemperatures(int amount) throws DAOException;;
    
    public ArrayList<Temperature> getTemperatures(Date start, Date end) throws DAOException;;
       
}
