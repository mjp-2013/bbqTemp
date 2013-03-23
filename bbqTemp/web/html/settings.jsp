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
    <ul class="nav nav-tabs" id="configuretab">
        <li><a href="#current" data-toggle="tab">Smoking</a></li>
        <li><a href="#custom" data-toggle="tab">Custom</a></li>
    </ul> 


    <div class="tab-content">
        <div class="tab-pane active" id="current">
            <p>
                <label for="amount">Max oven temperature:</label>
                <input type="text" id="maxTempAmount" style="border: 0; color: #f6931f; font-weight: bold;" />
            </p>
            <div id="slider-range-max"></div>


        </div>
        <div class="tab-pane" id="custom">
            Temperature type
            <select id="temperatureType">
                <option value="c">Celsius</option>
                <option value="f">Fahrenheit</option>
            </select>
        </div>





    </div>
    <div id="buttons">
        <button id="saveButton">Save</button>

    </div>

</div> <!-- /container -->



<%@include file="footer.jsp" %>   


<script src="js/jquery.js"></script>
<script src="js/bootstrap/js/bootstrap.min.js"></script>
<script src="js/jquery-ui.min.js"></script>
<script>
    $(document).ready(function(){  
        $('#configuretab a').click(function (e) {
            e.preventDefault();
            $(this).tab('show');
        })
  
        $("#saveButton").on("click", function() {
            var maxTemperature = $( "#slider-range-max" ).slider( "value" );
            var tempType = $("#temperatureType option:selected").val();
            
            //call the server and store the values.
            
            $.get("../BBQTemp?json=setConfigurationData", { 
                maxTemperatureValue: maxTemperature, 
                tempTypeValue: tempType  
            })
            .done(function(data) {
       
                // Server should respond <result>ok</result>
      
                if (data == "<result>ok</result>")
                {
                    //todo MODAL JQUERY/BOOTSTRAP ALERT BOX
                    alert ("Settings Saved succesfully!");
                        
                }
                else
                {
                    //todo MODAL JQUERY/BOOTSTRAP ALERT BOX and with error code from server...
                    alert ("Settings save failed!");
                }    
            },"html");   
        });   
    });
    
    $(function() {


        
       
    
       
    
        $( "#slider-range-max" ).slider({
            range: "max",
            min: 10,
            max: 450,
            value: 120,
            step: 2.5,
            slide: function( event, ui ) {
                $( "#maxTempAmount" ).val( ui.value );
            }
        });
        $( "#maxTempAmount" ).val( $( "#slider-range-max" ).slider( "value" ) );
        
         $.getJSON('../BBQTemp?json=getConfigurationData', function(data) {
           
         
           $( "#slider-range-max" ).slider("value",data.max_temperature );
           $( "#maxTempAmount" ).val( data.max_temperature );
           
           //data.temperature_unit
           //temperatureType
           $("#temperatureType option[value='"+data.temperature_unit+"']").attr("selected", "selected")
          

        });    
    });
    
    
</script>