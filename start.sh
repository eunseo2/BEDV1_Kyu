#!/bin/sh

./gradlew build
./gradlew asciidoctor

cp ./build/docs/asciidoc/index.html ./build/resources/main/templates/docs.html

nohup java -jar build/libs/kyu-0.0.1-SNAPSHOT.jar & > /dev/null
