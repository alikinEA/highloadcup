FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD /target/highloadcup.jar highloadcup.jar
RUN sh -c 'touch /highloadcup.jar'
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java -XX:+UseG1GC -Xmx4g -Xms4g -jar /highloadcup.jar" ]