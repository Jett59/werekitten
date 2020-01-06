set -e
set +x

CORRETTO_VERSION="11.0.5.10.1"
CORRETTO_TAR_VERSION="amazon-corretto-${CORRETTO_VERSION}-macosx-x64.tar.gz"
CORRETTO_VERSION_EXTRACTED_PATH="amazon-corretto-11.jdk"

rm -rf ./${CORRETTO_VERSION_EXTRACTED_PATH}
rm -rf ./${CORRETTO_TAR_VERSION}

wget "https://corretto.aws/downloads/resources/${CORRETTO_VERSION}/${CORRETTO_TAR_VERSION}"
tar xzf "${CORRETTO_TAR_VERSION}"

export JAVA_HOME="$(pwd)/${CORRETTO_VERSION_EXTRACTED_PATH}/Contents/Home"
echo "${JAVA_HOME}" > JAVA_HOME.txt

export PATH=$JAVA_HOME/bin:$PATH
echo $(java -version)

