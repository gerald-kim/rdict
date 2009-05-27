#!/bin/bash


if [ ! -n "$1" ]
then
  echo "Usage: `basename $0`  dictionary_db_dir"
  exit 1
fi  

rm -f enwiktionary-lastest.db
ln -s $1 enwiktionary-lastest.db