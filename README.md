# POS
A console-based POS system. Build automation is orchestrated through Gradle and
the Gradle Wrapper.

## Running the .jar
POS and it's dependencies are packaged in a fat jar. The following gradle task
builds the jar.

`./gradlew clean shadowJar`

Running the jar takes a single argument, the path to the properties file.

`java -jar -DpropertiesFile="/etc/pos/dev.system.properties" pos-1.0.0-all.jar `

## Configuration Files
The POS application requires two configurations files:

*pricingTables.csv* - Contains information about tool type, daily charge value, weekday charge, weekend charge,
and holiday charge,

*tools.csv* - Contains information about tool type, brand, and tool code.

The path to these configuration files are specified in the provided properties file,
under the property 'configurationFilePath'.

## Unit Tests
Run the unit test suite with the following gradle task:
`./gradlew clean test`


