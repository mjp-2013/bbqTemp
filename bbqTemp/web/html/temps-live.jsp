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

                var refreshId = setInterval(function(){
                    updateGraph(ajaxUrlCurrent, "Last 60 minutes temperatures, refresh every 10 seconds");
                }, 10000);


                
            });
</script>
<%@include file="footer.jsp" %> 
