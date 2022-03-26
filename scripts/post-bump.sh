#!/usr/bin/env bash

# Check if the temp file exists
if [ ! -f "${TEMP_FILE}" ]; then
  echo "Temp file does not exist. Exiting..."
  exit 1
fi

PREVIOUS=$(grep -o '^[0-9]\+\.[0-9]\+\.[0-9]\+' temp | head -n 1)
VERSION=$(grep -o '[0-9]\+\.[0-9]\+\.[0-9]\+' temp | tail -n 1)

# If previous version and version arent set then exit
if [ -z "$PREVIOUS" ] || [ -z "$VERSION" ]; then
  echo "Previous version or version not set"
  exit 1
fi

rm temp # remove temp file

sed -i "s/version=.*/version=${VERSION}/" ./gradle.properties

# Add the modified properties file to the version change commit
git add ./gradle.properties
git commit --amend -C HEAD

git push
git push origin v"${VERSION}" # Push the new version tag

./gradlew clean build

URL="https://github.com/DaRacci/Minix/compare/v${PREVIOUS}..v${VERSION}"
grep -Poz "(?s)(?<=## \\[v${VERSION}]\\(${URL}) - ....-..-..\n).*?(?=- - -)" CHANGELOG.md >> ./.templog.md

SEMIPATH=build/libs/Minix
gh release create "v${VERSION}" -F ./.templog.md -t "Minix release ${VERSION}" $SEMIPATH-$VERSION.jar Minix-API/$SEMIPATH-API-$VERSION-sources.jar
rm ./.templog.md

gh workflow run "docs.yml" # Generate the documentation

./gradlew :Minix-API:publish # Publish from the API module

git fetch --tags origin # Fetch the tags from the origin

