==Orchestration Layer Dummy API Mediation Service==
This service provides an API mediation service for the back end dummy service. It exposes 3 operations:

time -return the current time as a string in the response body
echo - return the string parameter in the response body
reverse - return the strin parameter reversed in the response body


=Build=
This project uses a Maven build, and generates a docker container called dummy-mediation. To build the VM use:

mvn clean package docker:build


=Test=
This project uses Arquillian Cube to instantiate a number of docker containers and run the tests against them. You will need the dummy service container (ol_dummy_service) available in your docker registry.

The tests are run via Maven, using a "test" profile:

mvn -P test test


=Run=
The services are packaged in docker containers, and can be run by starting a docker container:

docker run --name dummy-mediation dummy-mediation:0.0.1-SNAPSHOT
