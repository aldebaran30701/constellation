#!/bin/bash

source buildutilities/functions.sh

title "Run Sonar Scanning"

cd /tmp || exit
echo "Downloading sonar-scanner....."
if [ -d "/tmp/sonar-scanner-cli-3.2.0.1227-linux.zip" ];then
    sudo rm /tmp/sonar-scanner-cli-3.2.0.1227-linux.zip
fi
wget -q https://binaries.sonarsource.com/Distribution/sonar-scanner-cli/sonar-scanner-cli-4.0.0.1744-linux.zip
echo "Download completed."

echo "Unziping downloaded file..."
unzip sonar-scanner-cli-4.0.0.1744-linux.zip
echo "Unzip completed."
rm sonar-scanner-cli-4.0.0.1744-linux.zip

echo "Installing to opt..."
if [ -d "/var/opt/sonar-scanner-4.0.0.1744-linux" ];then
    rm -rf /var/opt/sonar-scanner-4.0.0.1744-linux
fi
mv sonar-scanner-4.0.0.1744-linux /var/opt/sonar-scanner
export PATH="$PATH:/var/opt/sonar-scanner/bin"
source ~/.bashrc
echo $PATH
cd /root/
echo $(ls)
echo "Installation completed successfully."

if [ ! -z $2 ]; then
  if [ $1 != "aldebaran30701/constellation" ]; then
    echo "skipping running sonar-scanner"
  else
    echo "This is a Pull Request"
    SONAR_PULLREQUEST_BRANCH="$(echo $1 | awk '{split($0,a,"/"); print a[1]}')/$4"
    sonar-scanner \
      -Dsonar.login="${SONAR_TOKEN}" \
      -Dsonar.pullrequest.key=$2 \
      -Dsonar.pullrequest.branch="${SONAR_PULLREQUEST_BRANCH}" \
      -Dsonar.pullrequest.base=$3
  fi
else
echo "Not a Pull Request"
  sonar-scanner \
    -Dsonar.login="${SONAR_TOKEN}" \
    -Dsonar.branch.name=$3
fi
