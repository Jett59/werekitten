set -e
set +x

JDK_14_VERSION_NUMBER=30
JDK_14_VERSION=openjdk-14-ea+${JDK_14_VERSION_NUMBER}_windows-x64_bin.zip
JDK_14_EXTRACTED_PATH=jdk-14

if [[ ! -e $JDK_14_VERSION ]]; then
  rm -rf ./${JDK_14_EXTRACTED_PATH}
  wget https://download.java.net/java/early_access/jdk14/${JDK_14_VERSION_NUMBER}/GPL/${JDK_14_VERSION}

elif [[ ! -d $JDK_14_VERSION ]]; then
  echo "JDK 14 already downloaded."
fi
