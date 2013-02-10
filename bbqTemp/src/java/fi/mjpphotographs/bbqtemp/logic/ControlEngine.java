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

import fi.mjpphotographs.bbqtemp.db.dao.TemperatureDAO;
import org.apache.commons.configuration.Configuration;

/**
 * Interface for Fan
 *
 * @author MjP
 */
public interface ControlEngine
{

    /**
     * Inits ControlEngine
     *
     * @param config System config object
     */
    public void initControlEngine( Configuration config, TemperatureDAO tempDAO );

    /*
     * Actual logic which turns device (fan) on and off. This method is called once per thread cycle.
     */
    public void handleDevice();

    /**
     * Returns true if ControlEngine is initalized.
     *
     * @return Returns true if Control engine is initialized correctly.
     */
    public boolean isInitialized();
}
