set -e
set +x

MAVEN_TAR="apache-maven-3.6.2-bin.tar.gz"
wget "http://apache.mirror.digitalpacific.com.au/maven/maven-3/3.6.2/binaries/${MAVEN_TAR}"
tar xzf "${MAVEN_TAR}"

export MAVEN_HOME="$(pwd)/${MAVEN_TAR}"
echo "MAVEN_HOME = ${MAVEN_HOME}"

export PATH=$MAVEN_HOME/bin:$PATH
echo $(mvn -version)
