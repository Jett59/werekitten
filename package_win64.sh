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
      --name WereKitten \
      --input ./target/release-directory \
      --description "A game where you control a cat and go on deadly quests." \
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


#${JPACKAGE_HOME}/bin/jpackage --type msi \
#    -d ./target/release-directory \
#    -n WereKitten \
#    --description "A game where you control a cat and go on deadly quests." \
#    --resource-dir ./src/main/deploy/jpackage \
#    -i ./target/jpackage-app \
#    --icon ./src/main/resources/icons/app.ico \
#    --main-class com.mycodefu.werekitten.Start \
#    --main-jar WereKitten.jar \
#    --app-version 1.0 \
#    --license-file ./LICENSE \
#    --temp ./target/jpackage \
#    --runtime-image "${JAVA_HOME}" \
#    --copyright "2019 Jett Thompson" \
#    --vendor "Jett Thompson" \
#    --verbose \
#    --win-shortcut \
#    --win-menu \
#    --win-dir-chooser
