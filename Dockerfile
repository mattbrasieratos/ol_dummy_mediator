FROM timrobinson/fuse_eap:6.3.0

EXPOSE 8080
COPY target/*.war /opt/eap/standalone/deployments/.
