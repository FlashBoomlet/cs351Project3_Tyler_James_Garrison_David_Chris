# CS 351 DLP_phase01
by  
David Ringo and Winston Riley.

## Building
find the following files `make.sh` and `run.sh` and...

~~~bash
./make.sh && ./run.sh
~~~


##Controls
### Navigation
* arrow keys pan around the globe
* shift + up arrow or down arrow zooms in and out respectively
* control clicking on a point on the map centers the camera at that point.
* mouse scroll wheel also zooms in and out


### Inspecting
* Meta (on mac command) click allows multiple region selection (as does click and drag).
* num-key 1 displays the default map look
* num-key 2 displays the percent of Corn in each region.
* num-key 3 displays the percent of Wheat in each region.
* num-key 4 displays the percent of Rice in each region.
* num-key 5 displays the percent of Soy in each region.
* num-key 6 displays the percent of Other crops in each region.
* num-key 7 displays the percent land that is Organic in each region.
* num-key 8 displays the percent land that is Conventional in each region.
* num-key 9 displays the percent land that is GMO in each region.

### Time

* num-key 8 sets the game time to normal
* num-key 9 speed the game to medium
* num-key 0 set the game to super fast speed.
* space bar pauses the game.

#### credits
text editor for XML editing was build using [RSyntaxTextArea](https://github.com/bobbylight).  
region data from [Natural Earth](http://www.naturalearthdata.com).
Soy, Wheat and Other approximation numbers are 2011,2012,2013 data respectively from FAOSTAT.
GMO = Area of Biotech Crops in 2014 (from ISAAA) divided by FAOSTAT 2011 Arable land and Permanent crops area.
(http://www.isaaa.org/resources/publications/briefs/49/executivesummary/default.asp)
Temperature data from Berkeley (http://berkeleyearth.org/data)
Precipitation data (http://climate.geog.udel.edu/~climate/html_pages/download.html#P2011rev)
Climate Projection from IPCC (http://www.ipcc.ch/ipccreports/tar/wg2/index.php?idp=29)