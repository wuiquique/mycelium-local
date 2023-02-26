#!/bin/sh

cd /app

find ./src | ENTR_INOTIFY_WORKAROUND=1 entr -r -n -s './gradlew buildLayers && java -jar ./build/docker/main/layers/application.jar'