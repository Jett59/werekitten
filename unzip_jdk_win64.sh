set -e
set +x

JDK_14_VERSION=openjdk-14-jpackage+1-70_windows-x64_bin.zip

JDK_14_EXTRACTED_PATH=jdk-14
rm -rf ./${JDK_14_EXTRACTED_PATH}
unzip ${JDK_14_VERSION}

