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

    <h1>Temperature charts</h1>



    <div id="chart" style="height:400px; width:1220px;"></div>

    <button class="btn btn-primary btn-small" id="button1hData">1h data</button>
    <button class="btn btn-primary btn-small" id="button2hData">2h data</button>
    <button class="btn btn-primary btn-small" id="button4hData">4h data</button>
    <button class="btn btn-primary btn-small" id="button8hData">8h data</button>
    <button class="btn btn-primary btn-small" id="button12hData">12h data</button>
    <button class="btn btn-primary btn-small" id="button24hData">24h data</button>
    <button class="btn btn-primary btn-small" id="buttonRefresh"><i class="icon-white icon-refresh"></i> Refresh</button>
</div> <!-- /container -->

<script src="js/jquery.js"></script>
<script src="js/bootstrap/js/bootstrap.min.js"></script>
<script class="include" type="text/javascript" src="js/jquery.jqplot.min.js"></script>
<script class="include" language="javascript" type="text/javascript" src="js/jqplot.dateAxisRenderer.min.js"></script>
<script class="code" type="text/javascript">

            $(document).ready(function(){

                var ajaxUrlCurrent = "../BBQTemp?json=getXhourlyData&hours=1";
            
                function updateGraph(ajaxUrl, title) {
                    ajaxUrlCurrent = ajaxUrl;
                    
                    $.ajax({
                        async: false,
                        url: ajaxUrl,
                        dataType:"json",
                        success: function(data) {
                            drawGraph(data, title);
                        }
                    }
                )};
                    
                function drawGraph(data, title) {
                    $('#chart').html("");
                    $.jqplot(
                    'chart',
                    [data[0].data],
                    {
                        title:title,
                        axes:{
                            xaxis:{
                                renderer:$.jqplot.DateAxisRenderer,
                                tickOptions:{formatString:'%H:%M'},
                                tickInterval:120000
                            }
                        },
                        grid: {
       
                            background: '#ffffff'      // grid background
        
                        },

                        series:[{
                                lineWidth:1,
                                markerOptions:{style:'circle',size:5},
                                rendererOptions: {
                                    smooth: true
                                }
                            }]
                        
                    }
                )};

                $("#button24hData").on("click", function() {
                    var ajaxUrl = "../BBQTemp?json=getXhourlyData&hours=24";
                    updateGraph(ajaxUrl,"Last 24 hour temperatures");
                    return false;
                });
                        
                $("#button1hData").on("click", function() {
                    var ajaxUrl = "../BBQTemp?json=getXhourlyData&hours=1";
                    updateGraph(ajaxUrl,"Last 60 minutes temperatures");

                    return false;
                });
                $("#button2hData").on("click", function() {
                    var ajaxUrl = "../BBQTemp?json=getXhourlyData&hours=2";
                    updateGraph(ajaxUrl,"Last 2 hour temperatures");

                    return false;
                });
                $("#button4hData").on("click", function() {
                    var ajaxUrl = "../BBQTemp?json=getXhourlyData&hours=4";
                    updateGraph(ajaxUrl,"Last 4 hour temperatures");

                    return false;
                });
                $("#button8hData").on("click", function() {
                    var ajaxUrl = "../BBQTemp?json=getXhourlyData&hours=8";
                    updateGraph(ajaxUrl,"Last 8 hour temperatures");

                    return false;
                });
                $("#button12hData").on("click", function() {
                    var ajaxUrl = "../BBQTemp?json=getXhourlyData&hours=12";
                    updateGraph(ajaxUrl,"Last 12 hour temperatures");

                    return false;
                });
       
                $("#buttonRefresh").on("click", function() {
                    updateGraph(ajaxUrlCurrent,"Refreshed data...");
                    return false;
                });

                updateGraph(ajaxUrlCurrent, "Last 60 minutes temperatures");
            });
</script>
<%@include file="footer.jsp" %> 