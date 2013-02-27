<%--
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

--%>
<%@include file="header.jsp" %>

<div class="container">
    <div class="hero-unit">
        <h1>BBQ Temp controller</h1>
        <p>Select function from the top</p>
    </div>

    <!--  Get values from ajax -->
    
    <div class="row">
        <div class="span4">
            <h2>OVEN TEMPERATURE</h2>
            <p>BIG SYMBOL WITH CURRENT OVEN TEMPERATURE AND UP OR DOWN SYMBOL GOES HERE</p>

        </div>
        <div class="span4">
            <h2>MEAT TEMPERATURE</h2>
            <p> BIG SYMBOL WITH CURRENT MEAT TEMPERATURE AND UP OR DOWN SYMBOL GOES HERE</p>

        </div>
    </div>
    <div class="row">
        <div class="span4">
            <h3>Ambien temperture</h3>
            <p>maller symbols with ambient temperatures, data time etc..</p>

        </div>
        <div class="span4">
            <h3>Current time etc..</h3>
            <p> maller symbols with ambient temperatures, data time etc..</p>

        </div>
    </div>


</div> <!-- /container -->
<%@include file="footer.jsp" %>   
<script src="js/jquery.js"></script> 
<script src="js/bootstrap/js/bootstrap.min.js"></script>