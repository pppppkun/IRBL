FROM registry.cn-hangzhou.aliyuncs.com/irbl/irbl-business-env:latest
WORKDIR /app
ADD runboot.sh /app/
ADD reset.sh /app/
ADD getFile.sh /app/
RUN chmod a+x reset.sh
RUN chmod a+x getFile.sh
RUN chmod a+x runboot.sh
ADD ./target/irbl-business.jar /app/irbl-business.jar
CMD /app/runboot.sh