#!/bin/bash

PREVIOUS=$1
CURRENT=$2

if ! ./gradlew test --info;
 then exit 1;
fi

echo "Bumping from v${PREVIOUS} to V${CURRENT}!"