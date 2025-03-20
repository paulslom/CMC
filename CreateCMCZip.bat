call mvn clean install
cd target
jar --create -M --file cmc.zip cmc.war ..\ROOT.war
