bbqTemp
=======

BBQ Smoker Temperature controller (for kettle grills etc) for Raspberry Pi written in Java.

Uses:
WiringPi/pi4j,
Servlet container (tomcat used in dev),
GPIO with darlington driver,
SPI and thermocouple sensor(s) via MAX31855,
Database (postgresql)

Target is to make standalone software for RPI (filebased db, integrated application server etc).

Should be usable in summer 2013 

Current status: temperature logging from thermocouple works (MJ and RJ temps), fan on / off logic works (logic temperatures in config file), 
simple hourly graph included... More to come.
