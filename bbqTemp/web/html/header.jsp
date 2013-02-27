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
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <title>BBQ Temp</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta name="description" content="BBQ Temp - BBQ grill temperature controller">
        <meta name="author" content="MjP">

        <link href="js/bootstrap/css/bootstrap.css" rel="stylesheet">
        <link href="js/bootstrap/css/footer.css" rel="stylesheet">
        <link href="js/bootstrap/css/bootstrap-responsive.css" rel="stylesheet">
        <link href="js/css/flick/jquery-ui-1.10.1.custom.min.css" rel="stylesheet">
        
        <!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
        <!--[if lt IE 9]>
          <script src="../assets/js/html5shiv.js"></script>
        <![endif]-->

        <!-- Fav and touch icons -->
        <link rel="apple-touch-icon-precomposed" sizes="144x144" href="../assets/ico/apple-touch-icon-144-precomposed.png">
        <link rel="apple-touch-icon-precomposed" sizes="114x114" href="../assets/ico/apple-touch-icon-114-precomposed.png">
        <link rel="apple-touch-icon-precomposed" sizes="72x72" href="../assets/ico/apple-touch-icon-72-precomposed.png">
        <link rel="apple-touch-icon-precomposed" href="../assets/ico/apple-touch-icon-57-precomposed.png">
        <link rel="shortcut icon" href="../assets/ico/favicon.png">
    </head>

    <body>
        <div id="wrap">
            <div class="navbar navbar-inverse navbar-fixed-top">
                <div class="navbar-inner">
                    <div class="container">
                        <button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                            <span class="icon-bar"></span>
                            <span class="icon-bar"></span>
                            <span class="icon-bar"></span>
                        </button>
                        <a class="brand" href="#">BBQ Temp</a>
                        <div class="nav-collapse collapse">
                            <ul class="nav">
                                <li><a href="index.jsp" >Home</a></li>

                                <li class="dropdown">
                                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">Charts<b class="caret"></b></a>
                                    <ul class="dropdown-menu">
                                        <li><a href="temps.jsp">Hourly Charts</a></li>
                                        <li><a href="temps-live.jsp">Live Charts</a></li>

                                    </ul>
                                </li>
                                <li><a href="settings.jsp">Settings</a></li>

                            </ul>
                        </div><!--/.nav-collapse -->
                    </div>
                </div>
            </div>
