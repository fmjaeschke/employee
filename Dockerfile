ARG BUILD_IMAGE=maven:3.8.1-jdk-8
ARG RUNTIME_IMAGE=tomcat:8.5

ARG DB_URL
ARG DB_USER
ARG DB_PASSWORE
ARG DB_DRIVER_CLASS
ARG DB_DRIVER

#############################################################################################
###                Stage where Docker is pulling all maven dependencies                   ###
#############################################################################################
FROM ${BUILD_IMAGE} as dependencies

ARG PROXY_SET=false
ARG PROXY_HOST=
ARG PROXY_PORT=

COPY pom.xml ./

RUN mvn -B dependency:go-offline \
        -DproxySet=${PROXY_SET} \
        -DproxyHost=${PROXY_HOST} \
        -DproxyPort=${PROXY_PORT}
#############################################################################################

#############################################################################################
###              Stage where Docker is building spring boot app using maven               ###
#############################################################################################
FROM dependencies as build

ARG PROXY_SET=false
ARG PROXY_HOST=
ARG PROXY_PORT=

COPY src ./src

RUN mvn -B clean package \
        -DproxySet=${PROXY_SET} \
        -DproxyHost=${PROXY_HOST} \
        -DproxyPort=${PROXY_PORT}
#############################################################################################

#############################################################################################
### Stage where database driver for Tomcat is fetched                                     ###
#############################################################################################
from build as db_driver

WORKDIR /tmp/

ARG DB_DRIVER
ENV DB_DRIVER=${DB_DRIVER}

RUN mvn dependency:copy -Dartifact=${DB_DRIVER}:jar -DoutputDirectory=/tmp/

#############################################################################################

#############################################################################################
### Stage where Docker is running a Tomcat with application built in previous stage       ###
#############################################################################################
FROM ${RUNTIME_IMAGE} as web

COPY --from=db_driver /tmp/*.jar $CATALINA_HOME/lib/
COPY --from=build /target/employee-?.?.?-SNAPSHOT.war $CATALINA_HOME/webapps/employee.war
COPY tomcat/context.xml $CATALINA_HOME/conf/Catalina/localhost/employee.xml

ARG DB_USER
ARG DB_PASSWORD
ARG DB_DRIVER_CLASS
ARG DB_URL
ENV DB_USER=${DB_USER}
ENV DB_PASSWORD=${DB_PASSWORD}
ENV DB_DRIVER_CLASS=${DB_DRIVER_CLASS}
ENV DB_URL=${DB_URL}

ENV JAVA_OPTS='-Ddb.url="${DB_URL}" -Ddb.username="${DB_USER}" -Ddb.password="${DB_PASSWORD}" -Ddb.driver_class="${DB_DRIVER_CLASS}"'

# add tomcat jpda debugging environmental variables
ENV JPDA_OPTS="-agentlib:jdwp=transport=dt_socket,address=8000,server=y,suspend=n"


# start tomcat8 with remote debugging
RUN ["catalina.sh", "run"]