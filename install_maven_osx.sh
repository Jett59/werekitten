set -e
set -x

export JAVA_HOME=$(cat JAVA_HOME.txt)

MAVEN_VERSION="3.6.3"
MAVEN_TAR="apache-maven-${MAVEN_VERSION}-bin.tar.gz"
MAVEN_DIR="apache-maven-${MAVEN_VERSION}"
wget "http://mirror.intergrid.com.au/apache/maven/maven-3/${MAVEN_VERSION}/binaries/${MAVEN_TAR}"
tar xzf "${MAVEN_TAR}"

export MAVEN_HOME="$(pwd)/${MAVEN_DIR}"
echo "MAVEN_HOME = ${MAVEN_HOME}"
echo "${MAVEN_HOME}" > MAVEN_HOME.txt

export PATH=$MAVEN_HOME/bin:$PATH
echo "mvn version = $(mvn -version)"
echo "fwd mvn version = $($MAVEN_DIR/bin/mvn -version)"
