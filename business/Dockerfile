FROM openjdk:8u262-jre-slim-buster
ADD ./target/irbl-business.jar /app/irbl-business.jar
ADD runboot.sh /app/
ADD reset.sh /app/
WORKDIR /app
RUN apt update
RUN apt install -y git
RUN chmod a+x runboot.sh
CMD /app/runboot.sh