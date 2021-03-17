#!/bin/bash
set -euo pipefail

source githubutilities/functions.sh

title "update-dependencies-clean-build"
echo $PWD
sudo apt update
sudo apt-get install tree
echo "before updatedeps"
tree -a ~/
ant \
  -Dupdate.dependencies=true \

echo "after updatedeps"
tree -a ~/