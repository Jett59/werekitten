JDK_14_VERSION=openjdk-14-jpackage+1-49_windows-x64_bin.zip
JDK_14_EXTRACTED_PATH=jdk-14

if [[ ! -e $JDK_14_EXTRACTED_PATH ]]; then
  rm -rf ./${JDK_14_EXTRACTED_PATH}
  rm -rf ./${JDK_14_VERSION}

  wget https://download.java.net/java/early_access/jpackage/1/${JDK_14_VERSION}
  unzip ${JDK_14_VERSION}

elif [[ ! -d $JDK_14_EXTRACTED_PATH ]]; then
  echo "JDK 14 already downloaded."
fi