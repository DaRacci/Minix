#!/usr/bin/env bash

PREVIOUS=$(grep -o '^[0-9]\+\.[0-9]\+\.[0-9]\+' temp | head -n 1)
CURRENT=$(grep -o '[0-9]\+\.[0-9]\+\.[0-9]\+' temp | tail -n 1)

if ! ./gradlew test --info;
 then exit 1;
fi

echo "Bumping from v${PREVIOUS} to v${CURRENT}!"