#!/bin/bash

if [[ $TRAVIS_EVENT_TYPE == “push” ]] && [[ $TRAVIS_BRANCH == “master” ]] && [[ $TRAVIS_PULL_REQUEST == “false” ]]
then
    echo ""
    mvn clean deploy --settings .travis/settings.xml -B -U -P release
else
    echo ""
    mvn clean package -B -U
fi