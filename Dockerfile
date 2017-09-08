FROM frolvlad/alpine-oraclejdk8:slim
ADD /target/highloadcup.jar highloadcup.jar
RUN sh -c 'touch /highloadcup.jar'
ENV JAVA_OPTS="-Xmx4g -Xms4g -server -XX:+AggressiveOpts -XX:+UseG1GC -XX:MaxGCPauseMillis=500"
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar /highloadcup.jar" ]