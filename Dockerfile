FROM  --platform=linux/x86-64 amazonlinux:2023 as graalvm

RUN yum update -y
RUN yum install -y tar wget gzip glibc-devel zlib-devel zlib gcc libstdc++-static

RUN mkdir graalvm/
RUN wget https://github.com/graalvm/graalvm-ce-builds/releases/download/vm-22.3.1/graalvm-ce-java17-linux-amd64-22.3.1.tar.gz
RUN tar -xzf graalvm-ce-java17-linux-amd64-22.3.1.tar.gz -C /graalvm

RUN export PATH="/graalvm/bin:${PATH}"
ENV JAVA_HOME="/graalvm"
#
#RUN echo pwd
#RUN echo $JAVA_HOME
