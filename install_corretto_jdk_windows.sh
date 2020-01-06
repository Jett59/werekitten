set -e
set +x

CORRETTO_VERSION="11.0.5.10.1"
CORRETTO_ZIP_VERSION="amazon-corretto-${CORRETTO_VERSION}-windows-x64.zip"
CORRETTO_VERSION_EXTRACTED_PATH="amazon-corretto-11.jdk"

rm -rf ./${CORRETTO_VERSION_EXTRACTED_PATH}
rm -rf ./${CORRETTO_TAR_VERSION}

wget "https://corretto.aws/downloads/resources/${CORRETTO_VERSION}/${CORRETTO_ZIP_VERSION}"
unzip "${CORRETTO_ZIP_VERSION}"

export JAVA_HOME="$(pwd)/${CORRETTO_VERSION_EXTRACTED_PATH}"
echo "${JAVA_HOME}" > JAVA_HOME.txt

export PATH=$JAVA_HOME/bin:$PATH
echo $(java -version)

