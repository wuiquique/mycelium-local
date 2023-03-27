#!/bin/sh

cd /app

while sleep 0.1; do
    find ./src | ENTR_INOTIFY_WORKAROUND=1 entr -rd -n -s './gradlew test'
done
