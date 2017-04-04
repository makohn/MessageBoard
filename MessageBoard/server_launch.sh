#!/bin/bash

BUILD_SCRIPT=build.sh
BUILD_PATH=build/
SERVER=server.jar

# Check whether java is installed on the system
if type -p java &> /dev/null; then
    echo found java executable in PATH
    _java=java
elif [[ -n "$JAVA_HOME" ]] && [[ -x "$JAVA_HOME/bin/java" ]];  then
    echo found java executable in JAVA_HOME     
    _java="$JAVA_HOME/bin/java"
else
    echo "can't find java. Make sure java is installed."
fi

# Check whether java version is at least 1.8
if [[ "$_java" ]]; then
    version=$("$_java" -version 2>&1 | awk -F '"' '/version/ {print $2}')
    echo using java version "$version"
    if [[ "$version" < "1.8" ]]; then
        echo "please upgrade java to version 1.8"
    fi
fi

# Change directory into the build directory
if ! [[ -d "$BUILD_PATH" ]]; then
  echo directory $BUILD_PATH does not exist.
  echo Make sure you run $BUILD_SCRIPT first.
  exit 1
fi

cd $BUILD_PATH

# Check if executable jar exists
if ! [[ -f "$SERVER" ]]; then
	echo file $SERVER does not exist.
	exit 2
fi

# Try to launch server.jar with given cli args
if [[ $# -gt 1 ]]; then
	java -jar $SERVER $@
else
	printf "Do you want to start a root server? (Y/n): "
	read root
	printf "Groupname: "
	read GROUP
	printf "Port: "
	read PORT
	if [[ $root == "Y" ]]; then
		java -jar $SERVER -g $GROUP -p $PORT
	else 
		printf "IP address of parent: "
		read PARENT_IP
		printf "Groupname of parent: "
		read PARENT_GROUP
		java -jar $SERVER -g $GROUP -p $PORT -ph $PARENT_IP -pg $PARENT_GROUP
	fi
fi

exit 0