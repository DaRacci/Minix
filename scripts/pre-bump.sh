#!/usr/bin/env bash

# If previous version and version arent set then exit
if [ -z "$1" ] || [ -z "$2" ]; then
  echo "Previous version or current not set"
  exit 1
fi

if ! ./gradlew test --info;
 then
   echo "Tests failed, aborting"
   exit 1;
fi

echo "Bumping from v${1} to v${2}!"
