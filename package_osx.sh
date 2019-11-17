set -e
set +x

JDK_14_EXTRACTED_PATH=jdk-14.jdk
export JPACKAGE_HOME=./${JDK_14_EXTRACTED_PATH}

export JAVA_HOME="${JPACKAGE_HOME}/Contents/Home"
export MAVEN_HOME=$(cat MAVEN_HOME.txt)

export MAVEN_OPTS=-"Xmx3072m -XX:MaxPermSize=512m -XX:+CMSClassUnloadingEnabled -XX:-UseGCOverheadLimit"
$MAVEN_HOME/bin/mvn clean package

${JPACKAGE_HOME}/Contents/Home/bin/jpackage \
    -d ./target/jpackage-app \
    -n WereKitten \
    --module-path $JAVA_HOME/jmods:target/jpackage-app/lib:target/jpackage-app/WereKitten.jar \
    -m com.mycodefu.werekitten