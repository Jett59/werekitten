set -e
set +x

JDK_14_EXTRACTED_PATH=jdk-14.0.1.jdk
export JPACKAGE_HOME=./${JDK_14_EXTRACTED_PATH}

export JAVA_HOME="${JPACKAGE_HOME}/Contents/Home"
export MAVEN_HOME=$(cat MAVEN_HOME.txt)

export MAVEN_OPTS=-"Xmx3072m -XX:MaxPermSize=512m -XX:+CMSClassUnloadingEnabled -XX:-UseGCOverheadLimit"
$MAVEN_HOME/bin/mvn clean package

${JPACKAGE_HOME}/Contents/Home/bin/jpackage \
      --name WereKitten \
      -d ./target/jpackage-app \
      --type pkg \
      --input ./target/release-directory \
      --description "A game where you control a cat and fight for territory" \
      --app-version 1.0 \
      --license-file ./LICENSE \
      --icon ./src/main/resources/icons/app.ico \
      --copyright "2019 Jett Thompson" \
      --vendor "Jett Thompson" \
      --main-jar WereKitten.jar \
      --module-path ./target/release-directory/lib \
      --add-modules javafx.controls \
      --verbose
