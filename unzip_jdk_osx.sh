set -e
set +x

JDK_14_VERSION=openjdk-14-jpackage+1-70_osx-x64_bin.tar.gz

JDK_14_EXTRACTED_PATH=jdk-14.jdk
rm -rf ./${JDK_14_EXTRACTED_PATH}
tar xzf ${JDK_14_VERSION}