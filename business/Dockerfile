FROM arm64v8/openjdk:8u212-jre-slim
ADD ./target/irbl-business.jar /app/irbl-business.jar
ADD runboot.sh /app/
WORKDIR /app
RUN chmod a+x runboot.sh
CMD /app/runboot.sh