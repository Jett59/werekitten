set -e
set +x

JDK_14_EXTRACTED_PATH=jdk-14

export JPACKAGE_HOME=./${JDK_14_EXTRACTED_PATH}
export PATH=$JAVA_HOME/bin:$PATH
export MAVEN_OPTS=-"Xmx3072m -XX:MaxPermSize=512m -XX:+CMSClassUnloadingEnabled -XX:-UseGCOverheadLimit"

function mvn {
  /c/ProgramData/chocolatey/lib/maven/apache-maven-3.6.2/bin/mvn $@
}
mvn clean package

${JPACKAGE_HOME}/bin/jpackage \
      --type msi \
      -d ./target/jpackage-app \
      --name WereKitten \
      --input ./target/release-directory \
      --description "a game where you control a cat and fight for territory" \
      --app-version 1.0 \
      --license-file ./LICENSE \
      --icon ./src/main/resources/icons/app.ico \
      --copyright "2019 Jett Thompson" \
      --vendor "Jett Thompson" \
      --main-jar WereKitten.jar \
      --module-path ./target/release-directory/lib \
      --add-modules javafx.controls \
      --win-shortcut \
      --win-menu \
      --win-dir-chooser \
      --verbose
