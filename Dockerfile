FROM amazonlinux:2 as graalvm

RUN yum update -y
RUN yum install -y wget unzip tar gzip bzip2-devel ed gcc gcc-c++ gcc-gfortran \
        less libcurl-devel openssl openssl-devel readline-devel xz-devel \
        zlib-devel glibc-static libcxx libcxx-devel llvm-toolset-7 zlib-static

ENV JAVA_VERSION="java17"
ENV GRAAL_VERSION="22.3.1"

ENV GRAAL_FOLDERNAME graalvm-ce-${JAVA_VERSION}-${GRAAL_VERSION}
ENV GRAAL_FILENANE graalvm-ce-${JAVA_VERSION}-linux-amd64-${GRAAL_VERSION}.tar.gz

RUN wget https://github.com/graalvm/graalvm-ce-builds/releases/download/vm-${GRAAL_VERSION}/${GRAAL_FILENANE}
RUN tar -xzf $GRAAL_FILENANE
RUN mv $GRAAL_FOLDERNAME /usr/lib/graalvm
RUN rm -rf $GRAAL_FOLDERNAME

# Gradle
ENV GRADLE_VERSION="7.5.1"
ENV GRADLE_FOLDERNAME gradle-${GRADLE_VERSION}
ENV GRALE_FILENAME ${GRADLE_FOLDERNAME}-bin.zip
RUN curl -L https://services.gradle.org/distributions/${GRALE_FILENAME} > $GRALE_FILENAME
RUN unzip -o $GRALE_FILENAME
RUN mv $GRADLE_FOLDERNAME /usr/lib/gradle
RUN rm $GRALE_FILENAME

# AWS Lambda Builders
RUN amazon-linux-extras enable python3.8
RUN yum clean metadata && yum -y install python3.8
RUN curl -L get-pip.io | python3.8
RUN pip3 install aws-lambda-builders

VOLUME /project
WORKDIR /project

RUN /usr/lib/graalvm/bin/gu install native-image
RUN ln -s /usr/lib/graalvm/bin/native-image /usr/bin/native-image

ENV JAVA_HOME /usr/lib/graalvm

ENTRYPOINT ["sh"]
