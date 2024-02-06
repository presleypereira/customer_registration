FROM docker.impello.co.uk/dbi__java17--jre:17.0.8-1.25.0-9

COPY target/app.jar /usr/src/app/app.jar
