#!/bin/bash
docker run -it --rm   -v "$PWD":/usr/src/app  -v /var/run/docker.sock:/var/run/docker.sock -v /home/europa/.m2/settings.xml.docker:/root/.m2/settings.xml -u root -w /usr/src/app  maven mvn -P unit-test -B clean package test
sudo rm -rf target
