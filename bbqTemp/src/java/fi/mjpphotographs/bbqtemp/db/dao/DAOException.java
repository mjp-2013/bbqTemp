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

/**
 * Used for as container for exceptions coming from persistent layer.
 * @author Mikko
 */
public class DAOException extends Exception
{
    /**
     * DAO exception used for catching exceptions from data layer.
     * @param message
     * @param cause 
     */
    public DAOException(String message, Throwable cause){
        super  (message, cause);
    }
    
    public DAOException(String message){
        super  (message);
    }  
    
}
