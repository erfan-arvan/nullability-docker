FROM openjdk:11

LABEL maintainer="Nima Karimipour <karimipour.nima@gmail.com>"

# Install basic software support
RUN apt-get update && apt-get install --yes software-properties-common

ENV JAVA_HOME=/usr/local/openjdk-11
RUN export JAVA_HOME

# Install required softwares (curl & zip & wget)
RUN apt-get install curl -y
RUN apt-get install zip -y
RUN apt-get install wget -y
RUN apt-get install cloc -y
RUN apt-get install -y psmisc && rm -rf /var/lib/apt/lists/*

# Install Maven
ARG MAVEN_VERSION=3.9.9
ARG USER_HOME_DIR="/root"
ARG SHA=a555254d6b53d267965a3404ecb14e53c3827c09c3b94b5678835887ab404556bfaf78dcfe03ba76fa2508649dca8531c74bca4d5846513522404d48e8c4ac8b
ARG BASE_URL=https://downloads.apache.org/maven/maven-3/${MAVEN_VERSION}/binaries

RUN mkdir -p /usr/share/maven /usr/share/maven/ref \
  && echo "Downlaoding maven" \
  && curl -fsSL -o /tmp/apache-maven.tar.gz ${BASE_URL}/apache-maven-${MAVEN_VERSION}-bin.tar.gz \
  \
  && echo "Checking download hash" \
  && echo "${SHA}  /tmp/apache-maven.tar.gz" | sha512sum -c - \
  \
  && echo "Unziping maven" \
  && tar -xzf /tmp/apache-maven.tar.gz -C /usr/share/maven --strip-components=1 \
  \
  && echo "Cleaning and setting links" \
  && rm -f /tmp/apache-maven.tar.gz \
  && ln -s /usr/share/maven/bin/mvn /usr/bin/mvn

ENV MAVEN_HOME=/usr/share/maven
ENV MAVEN_CONFIG="$USER_HOME_DIR/.m2"

# Install Python 3 and pip
RUN apt-get update && apt-get install -y python3 python3-pip
RUN ln -sf /usr/bin/python3 /usr/bin/python

# Install git
RUN apt-get install -y git

#Install Android SDK
ARG ANDROID_SDK_VERSION=6858069
ENV ANDROID_SDK_ROOT=/opt/android-sdk
RUN mkdir -p ${ANDROID_SDK_ROOT}/cmdline-tools && \
    wget -q https://dl.google.com/android/repository/commandlinetools-linux-${ANDROID_SDK_VERSION}_latest.zip && \
    unzip *tools*linux*.zip -d ${ANDROID_SDK_ROOT}/cmdline-tools && \
    mv ${ANDROID_SDK_ROOT}/cmdline-tools/cmdline-tools ${ANDROID_SDK_ROOT}/cmdline-tools/tools && \
    rm *tools*linux*.zip

# set the environment variables
ENV PATH=${PATH}:${ANDROID_SDK_ROOT}/cmdline-tools/latest/bin:${ANDROID_SDK_ROOT}/cmdline-tools/tools/bin:${ANDROID_SDK_ROOT}/platform-tools:${ANDROID_SDK_ROOT}/emulator
ENV LD_LIBRARY_PATH=${ANDROID_SDK_ROOT}/emulator/lib64:${ANDROID_SDK_ROOT}/emulator/lib64/qt/lib
ENV QTWEBENGINE_DISABLE_SANDBOX=1
ENV ANDROID_HOME=/opt/android-sdk

# accept the license agreements of the SDK components
COPY ./license_accepter.sh /opt/
RUN chmod +x /opt/license_accepter.sh && /opt/license_accepter.sh $ANDROID_SDK_ROOT

# copy NJR
COPY ./NJR /opt/NJR
ENV NJR=/opt/NJR

# copy Benchmark
COPY ./Benchmarks /opt/Benchmarks


# Bias study tables
COPY ./table_1/ /opt/table_1/
COPY ./table_2/ /opt/table_2/
COPY ./table_3/ /opt/table_3/
RUN python3 /opt/table_3/versions/nullaway/update_xml.py

# Copy ErrorReduction
COPY ./ErrorReduction /opt/error-reduction

# Copy manual invesigations
COPY ./ManualInvestigation /opt/manual-investigations

# copy tools
COPY ./nullability-inference-comparison-tools /opt/nullability-inference-comparison-tools
RUN cd /opt/nullability-inference-comparison-tools/java/AnnotUtils && ./gradlew publishToMavenLocal

# install Daikon, make daikonparent dir
RUN mkdir -p /opt/daikonparent
RUN wget -P /opt/daikonparent http://plse.cs.washington.edu/daikon/download/daikon-5.8.20.tar.gz
RUN tar -xzf /opt/daikonparent/daikon-5.8.20.tar.gz -C /opt/daikonparent
ENV DAIKONDIR=/opt/daikonparent/daikon-5.8.20
RUN echo "source $DAIKONDIR/scripts/daikon.bashrc" >> /root/.bashrc

# Copy dynamic nullability
COPY ./DynamicNullability /opt/dynamic-nullability

# === Install cache2k-java11-parent as 2.0-SNAPSHOT ===
RUN mkdir -p /opt/libs/cache2k
WORKDIR /opt/libs/cache2k
RUN git clone https://github.com/cache2k/cache2k.git

WORKDIR /opt/libs/cache2k/cache2k-java11-parent
RUN mvn versions:set -DnewVersion=2.0-SNAPSHOT && mvn clean install

# === Install custom checker-framework ===
WORKDIR /opt/libs
RUN git clone https://github.com/erfan-arvan/checker-framework.git

WORKDIR /opt/libs/checker-framework
RUN ./gradlew assemble && ./gradlew publishToMavenLocal


CMD ["tail", "-f", "/dev/null"]
