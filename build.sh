#!/bin/bash

type mvn >/dev/null 2>&1 || { echo >&2 "I require maven but it's not installed.  Aborting."; exit 1; }
cd MessageBoard
mvn clean install