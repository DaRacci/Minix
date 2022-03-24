#!/usr/bin/env bash

PREVIOUS=$1
CURRENT=$2
TEST=$3

if [ "$TEST" = "true" ]; then
  echo "Cancelling pre-bump"
  exit 0
fi

if ! ./gradlew test --info;
 then exit 1;
fi

echo "Bumping from v${PREVIOUS} to v${CURRENT}!"