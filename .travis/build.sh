#!/bin/bash

if [[ $TRAVIS_EVENT_TYPE == "push" ]] && [[ $TRAVIS_BRANCH == "master" ]] && [[ $TRAVIS_PULL_REQUEST == "false" ]]
then
    echo "${TRAVIS_EVENT_TYPE}"
    echo "${TRAVIS_BRANCH}"
    echo "${TRAVIS_PULL_REQUEST}"
    mvn clean deploy --settings .travis/settings.xml -B -U -P release
else
    echo "${TRAVIS_EVENT_TYPE}"
    echo "${TRAVIS_BRANCH}"
    echo "${TRAVIS_PULL_REQUEST}"
    mvn clean package -B -U
fi