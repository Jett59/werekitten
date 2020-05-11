set -e
set +x

JDK_14_VERSION_NUMBER=30
JDK_14_VERSION=openjdk-14-ea+${JDK_14_VERSION_NUMBER}_osx-x64_bin.tar.gz
JDK_14_EXTRACTED_PATH=jdk-14.jdk

rm -rf ./${JDK_14_EXTRACTED_PATH}
rm -rf ./${JDK_14_VERSION}

wget https://download.java.net/java/GA/jdk14.0.1/664493ef4a6946b186ff29eb326336a2/7/GPL/openjdk-14.0.1_osx-x64_bin.tar.gz
tar xzf ${JDK_14_VERSION}
