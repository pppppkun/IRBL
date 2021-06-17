#!/bin/bash
echo "$1"
echo "$2"
echo "$3"
cd $1
echo "begin"
git show $2:$3 > ../result.txt
echo "end"