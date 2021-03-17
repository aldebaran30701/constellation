#!/bin/bash
set -euo pipefail

source githubutilities/functions.sh

title "update-dependencies-clean-build"
echo $PWD
apt update
apt-get install tree
echo "before updatedeps"
tree -a /root
ant \
  -Dnbplatform.active.dir=/usr/local/netbeans \
  -Dnbplatform.default.netbeans.dest.dir=/usr/local/netbeans \
  -Dnbplatform.default.harness.dir=/usr/local/netbeans/harness \
  -Dupdate.dependencies=true \
  -Dbuild.compiler.debug=true update-dependencies-clean-build

echo "after updatedeps"
tree -a /root