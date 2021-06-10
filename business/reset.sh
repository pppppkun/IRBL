#!/bin/bash
echo "$1"
echo "$2"
cd $1
pwd
echo 'reset repo'
git reset --hard $2
echo 'reset success?'