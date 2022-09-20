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

sed -i "s/version=.*/version=$2/" ./gradle.properties

# Add the modified properties file to the version change commit
git add gradle.properties

echo "Bumping from v${1} to v${2}!"
