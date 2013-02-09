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
package fi.mjpphotographs.bbqtemp.main;

import fi.mjpphotographs.bbqtemp.logic.DataLogger;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletConfig;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *
 * @author Mikko
 */
@WebServlet(name = "BBQTemp", urlPatterns =
{
    "/BBQTemp"
})
public class BBQTemp extends HttpServlet
{

    static Logger logger = Logger.getLogger( BBQTemp.class );
    private DataLogger dataLogger;

    /**
     * Inits db, configurations, device controllers etc.
     *
     * @param config
     * @throws ServletException
     */
    @Override
    public void init( ServletConfig config ) throws ServletException
    {
        super.init( config );
        // myLed = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00 ,"My LED", PinState.LOW);   
        dataLogger = new DataLogger();
    }

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException
    {

        response.setContentType( "text/html;charset=UTF-8" );

        PrintWriter out = response.getWriter();
        if ( !dataLogger.isPolling() )
        {
            dataLogger.startPolling();
            try
            {
                out.println( "<html>" );
                out.println( "<head>" );
                out.println( "<title>BBQ Temp</title>" );
                out.println( "</head>" );
                out.println( "<body>" );
                out.println( "<h1>BBQTemp initalized correctly.</h1>" );
                out.println( "</body>" );
                out.println( "</html>" );
            }
            finally
            {
                out.close();
            }
        }
        else
        {
            try
            {
                out.println( "<html>" );
                out.println( "<head>" );
                out.println( "<title>BBQ Temp</title>" );
                out.println( "</head>" );
                out.println( "<body>" );
                out.println( "<h1>Datalogger thread is already up and running.</h1>" );
                out.println( "</body>" );
                out.println( "</html>" );
            }
            finally
            {
                out.close();
            }

        }



    }

// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException
    {
        processRequest( request, response );
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException
    {
        processRequest( request, response );
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo()
    {
        return "BBQTemp started servlet (starts poller and control thread and initializes db, logger and configurations). ";
    }// </editor-fold>

    public void destroy()
    {
        logger.info("BBQTemp shutdown in process...");
        dataLogger.stopPolling();
        dataLogger = null;
        logger.info("BBQTemp shutdown completed.");
    }
}
