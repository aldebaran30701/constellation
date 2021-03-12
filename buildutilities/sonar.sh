#!/bin/bash

source buildutilities/functions.sh

title "Run Sonar Scanning"

if [ github.event.number != "false" ]; then
  if [ $1 != "aldebaran30701/constellation" ]; then
    echo $1
    echo "skipping running sonar-scanner"
  else
    echo "in else"
    SONAR_PULLREQUEST_BRANCH="$(echo "${TRAVIS_PULL_REQUEST_SLUG}" | awk '{split($0,a,"/"); print a[1]}')/${TRAVIS_PULL_REQUEST_BRANCH}"
    sonar-scanner \
      -Dsonar.login="${SONAR_TOKEN}" \
      -Dsonar.pullrequest.key="${TRAVIS_PULL_REQUEST}" \
      -Dsonar.pullrequest.branch="${SONAR_PULLREQUEST_BRANCH}" \
      -Dsonar.pullrequest.base="${TRAVIS_BRANCH}"
  fi
else
  sonar-scanner \
    -Dsonar.login="${SONAR_TOKEN}" \
    -Dsonar.branch.name="${TRAVIS_BRANCH}"
fi
