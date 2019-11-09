set -e
set +x

JDK_14_EXTRACTED_PATH=jdk-14.jdk

export JPACKAGE_HOME=./${JDK_14_EXTRACTED_PATH}

export MAVEN_OPTS=-"Xmx3072m -XX:MaxPermSize=512m -XX:+CMSClassUnloadingEnabled -XX:-UseGCOverheadLimit"
mvn clean package

${JPACKAGE_HOME}/Contents/Home/bin/jpackage --package-type pkg \
    -d ./target/jpackage-app \
    -n WereKitten \
    --description "A game where you control a cat and go on deadly quests." \
    --resource-dir ./src/main/deploy/jpackage \
    -i ./target/jpackage-app \
    --icon ./src/main/resources/icons/app.ico \
    --main-class com.mycodefu.start.Start \
    --main-jar WereKitten.jar \
    --app-version 1.0 \
    --license-file ./LICENSE \
    --temp ./target/jpackage \
    --runtime-image "${JAVA_HOME}" \
    --copyright "2019 Jett Thompson" \
    --vendor "Jett Thompson" \
    --verbose
