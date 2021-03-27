FROM openjdk:8u262-jre-slim-buster
ADD ./target/irbl-business.jar /app/irbl-business.jar
ADD runboot.sh /app/
WORKDIR /app
RUN chmod a+x runboot.sh
CMD /app/runboot.sh