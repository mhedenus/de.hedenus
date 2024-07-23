@echo off
set MAVEN_OPTS="-Xmx10G"
mvn compile exec:java -Dexec.mainClass="de.hedenus.astro.map.MapGeneration"
