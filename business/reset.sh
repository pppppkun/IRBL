#!/bin/bash
cd $1
pwd
echo 'reset repo'
git reset --hard $2
echo 'reset success?'