FROM python:3.7
ADD ./src/ /app/
ADD run.sh /app/
ADD requirements.txt /app/
ADD download_data.py /app/
WORKDIR /app
RUN pip install -r requirements.txt -i https://pypi.tuna.tsinghua.edu.cn/simple
RUN python3 download_data.py
RUN chmod a+x run.sh
CMD /app/run.sh
