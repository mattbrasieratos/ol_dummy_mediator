#!/bin/bash
docker run -it --rm   -v "$PWD":/usr/src/app  -v /var/run/docker.sock:/var/run/docker.sock  -w /usr/src/app  maven mvn -P test -B test
sudo rm -rf target
