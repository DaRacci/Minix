#!/usr/bin/env bash
# TODO -> Move to dotter and make universal for all projects / Create inputs for repo name and so on
# TODO -> Yummy rust cli tool.... maybe....

# If previous version and version arent set then exit
if [ -z "$1" ] || [ -z "$2" ]; then
  echo "Previous version or version not set"
  exit 1
fi

if [ -f temp ]; then
  echo "Removing temp file"
  rm temp
fi

git push origin v"${2}" || exit 1 # Push the new version tag for the release

./gradlew clean build test

URL="https://github.com/DaRacci/Minix/compare/v$1..v$2"
grep -Poz "(?s)(?<=## \\[v$2\\]\\(${URL}\\) - ....-..-..\n).*?(?=- - -)" CHANGELOG.md >> ./.templog.md
head -n -1 .templog.md > .temp ; mv .temp .templog.md # Remove that weird null line

SEMIPATH=build/libs/Minix
gh release create "v$2" -F ./.templog.md -t "Minix release $2" $SEMIPATH-$2.jar Minix-API/$SEMIPATH-API-$2-sources.jar Minix-Core/$SEMIPATH-Core-$2.jar
rm ./.templog.md

gh workflow run "docs.yml" # Generate the documentation

./gradlew :Minix-API:publish # Publish from the API module
./gradlew :Minix-Core:publish # Public from the Core module
./gradlew :minix-core:core-integrations:publish # Publish from the Core Integrations module

git fetch --tags origin # Fetch the tags from the origin
sed -i "s/version=.*/version=$3/" ./gradle.properties # We now in snapshot

# Update Minix-Conventions
cd ../Minix-Conventions || exit 1
git checkout main || exit 1
git pull || exit 1
sed -i "s/^minix = .*/minix = \"$2\"/" ./gradle/libs.versions.toml
git add ./gradle/libs.versions.toml
cog commit chore "Update Minix version from $1 to $2" deps
git push || exit 1
