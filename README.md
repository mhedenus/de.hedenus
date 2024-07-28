
# Star Map

This Java application produces a high resolution *Star Map* (ca. 18000x9000 pixels).

## HOW-TO

This application has been developed with [OpenJDK 22](https://jdk.java.net/22/)
and uses [Apache Maven](https://maven.apache.org/).


The original Gaia data (see below) is too big for `ImageIO` (the implementation hits the 2G limit),
so I have reduced it to 36000x18000 pixels. Download the file

	https://www.hedenus.de/astro/data/Gaia_36k.bmp
	
and store it as 

	de.hedenus.astro/.cache/Gaia_36k.bmp


Under Windows run

	generate_starmap.bat

The result is found in the `target` folder.	Two files are generated:

- the map as PNG
- a CSV table of the stars with proper names 



## Third Party Sources

### Milky Way Image

*Gaia*'s all-sky view displayed in an equirectangular projection:

	https://sci.esa.int/web/gaia/-/60196-gaia-s-sky-in-colour-equirectangular-projection

### Star Data

*Bright Star Catalogue, 5th Revised Ed* aka *Yale Catalogue of Bright Stars*:

	https://cdsarc.u-strasbg.fr/cgi-bin/qcat?V/50#/article

German star names are taken from *Wikipedia*:

	https://de.wikipedia.org/wiki/Liste_von_Sternennamen


### Constellations

Boundaries published by *International Astronomical Union*:

	https://www.iau.org/public/themes/constellations/


Center points computed and published by *Pierre Barbier*:

	https://watcheroftheskies.net/constellations/boundaries.html


Lines are taken from *Stellarium*:

	https://github.com/Stellarium/stellarium/blob/master/skycultures/modern/constellationship.fab

	https://github.com/Stellarium/stellarium/blob/master/stars/default/cross-id.dat 


## License

I assume, this license covers all licenses:

	https://creativecommons.org/licenses/by-sa/4.0/