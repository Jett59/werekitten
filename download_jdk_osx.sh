set -e
set +x

JDK_14_VERSION_NUMBER=30
JDK_14_VERSION=openjdk-14-ea+${JDK_14_VERSION_NUMBER}_osx-x64_bin.tar.gz
JDK_14_EXTRACTED_PATH=jdk-14.jdk

rm -rf ./${JDK_14_EXTRACTED_PATH}
rm -rf ./${JDK_14_VERSION}

wget https://download.java.net/java/early_access/jdk14/${JDK_14_VERSION_NUMBER}/GPL/${JDK_14_VERSION}
tar xzf ${JDK_14_VERSION}
