#!/bin/bash

if [[ $TRAVIS_EVENT_TYPE == “push” ]] && [[ $TRAVIS_BRANCH == “master” ]] && [[ $TRAVIS_PULL_REQUEST == “false” ]]
then
    echo "before_deploy.sh is running!"

    mvn help:evaluate -N -Dexpression=project.version|grep -v '\['
    export PROJECT_VERSION=$(mvn help:evaluate -N -Dexpression=project.version|grep -v '\[')
    export TRAVIS_TAG=$PROJECT_VERSION

    echo "PROJECT_VERSION: ${PROJECT_VERSION}"
    echo "TRAVIS_TAG: ${TRAVIS_TAG}"

    git config --local user.name "CI"
    git config --local user.email "oleg.strunevskiy@gmail.com"
    git tag $TRAVIS_TAG
    git push origin $TRAVIS_TAG
else
    echo "before_deploy.sh is skiped!"
fi