#!/bin/bash

if [[ $TRAVIS_PULL_REQUEST == “false” ]] && [[ $TRAVIS_BRANCH == “master” ]]
then
    echo ""
    mvn clean deploy --settings .travis/settings.xml -B -U -P release
else
    echo ""
    mvn clean package -B -U
fi