#!/usr/bin/env bash

# Check if the temp file exists
if [ ! -f temp ]; then
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

sed -i "s/version=.*/version=$VERSION/" ./gradle.properties

# Add the modified properties file to the version change commit
git add ./gradle.properties
git commit --amend -C HEAD

git push || exit 1 # There were remote changes not present in the local repo
git push origin v"${VERSION}" # Push the new version tag

./gradlew clean build

URL="https://github.com/DaRacci/Minix/compare/v$PREVIOUS..v$VERSION"
grep -Poz "(?s)(?<=## \\[v${VERSION}\\]\\(${URL}\\) - ....-..-..\n).*?(?=- - -)" CHANGELOG.md >> ./.templog.md
head -n -1 .templog.md > .temp ; mv .temp .templog.md # Remove that weird null line

SEMIPATH=build/libs/Minix
gh release create "v$VERSION" -F ./.templog.md -t "Minix release $VERSION" $SEMIPATH-$VERSION.jar Minix-API/$SEMIPATH-API-$VERSION-sources.jar Minix-Core/$SEMIPATH-Core-$VERSION.jar
rm ./.templog.md

gh workflow run "docs.yml" # Generate the documentation

./gradlew :Minix-API:publish # Publish from the API module
./gradlew :Minix-Core:publish # Public from the Core module

git fetch --tags origin # Fetch the tags from the origin
sed -i "s/version=.*/version=$VERSION-SNAPSHOT/" ./gradle.properties # We now in snapshot

# Update Minix-Conventions
cd ../Minix-Conventions || exit 1
git checkout main || exit 1
git pull || exit 1
sed -i "s/minix = .*/minix = \"$VERSION\"/" ./gradle/libs.versions.toml
git add ./gradle/libs.versions.toml
cog commit chore "Update Minix version from $PREVIOUS to $VERSION" deps
git push || exit 1
