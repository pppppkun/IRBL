#!/bin/bash
echo "$1"
echo "$2"
echo "$3"
cp -r $3 $1
cd $1
pwd
echo 'reset repo'
git reset --hard $2
echo 'reset success?'