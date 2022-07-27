#!/bin/bash 

set -e 

echo "OK"

if [ -n "$1" ]; then
MY_FILE=$1
else
  echo "usage: file required"
  exit -1
fi

groovy VocabGame.groovy $MY_FILE

echo "Ready."
