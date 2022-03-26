#!/usr/bin/env bash

# Check if the temp file exists
if [ ! -f temp ]; then
  echo "Temp file does not exist. Exiting..."
  exit 1
fi

PREVIOUS=$(grep -o '^[0-9]\+\.[0-9]\+\.[0-9]\+' temp | head -n 1)
CURRENT=$(grep -o '[0-9]\+\.[0-9]\+\.[0-9]\+' temp | tail -n 1)

# If previous version and version arent set then exit
if [ -z "$PREVIOUS" ] || [ -z "$CURRENT" ]; then
  echo "Previous version or current not set"
  exit 1
fi

rm temp # Make sure we clean up after ourselves

if ! ./gradlew test --info;
 then
   echo "Tests failed, aborting"
   exit 1;
fi

echo "Bumping from v${PREVIOUS} to v${CURRENT}!"

