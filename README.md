bbqTemp
=======

BBQ Temperature controller for Raspberry Pi written in Java.

Uses:
WiringPi/pi4j,
Servlet container (tomcat used in dev),
Python scripts (these will be replaced by pi4j),
GPIO with darlington driver,
SPI and thermocouple sensor(s),
Database (postgresql)

Target is to make standalone software for RPI (filebased db, integrated application server etc).

Should be usable in summer 2013 (current status: fan on / off logic works (temperatures in cofigure), simple hourly graph included)
