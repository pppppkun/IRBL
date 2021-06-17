FROM registry.cn-hangzhou.aliyuncs.com/irbl/irbl-business-env:latest
ADD ./target/irbl-business.jar /app/irbl-business.jar
ADD runboot.sh /app/
ADD reset.sh /app/
ADD getFile.sh /app/
WORKDIR /app
RUN chmod a+x runboot.sh
CMD /app/runboot.sh