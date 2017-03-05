# MessageBoard


```diff
- _______________________INFO_________________________
- 'Neustart_Issue' und 'gui' wurden in 'dev' gemerged.
- ____________________________________________________
```

master | 
-------|
[![Build Status](https://travis-ci.com/makohn/MessageBoard.svg?token=z1kyyNNo3nk7k9bTgxPq&branch=master)](https://travis-ci.com/makohn/MessageBoard)|

##How to build
- Install Java 8 [JDK8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html).
- Install [maven](http://maven.apache.org/).
- Clone the project `git@github.com:makohn/MessageBoard.git` or download the [zip](https://github.com/makohn/MessageBoard/archive/master.zip) file. 
- Change dir to the **inner** MessageBoard directory and type `mvn install` at the console.

##How to run
- Change dir to the **inner** MessageBoard directory
- Type or copy the following command for starting the server 

```
java -cp Remote/target/Remote-0.0.1-SNAPSHOT.jar:Server/target/Server-0.0.1-SNAPSHOT.jar de.htwsaar.wirth.server.Main root 1099
```

- Accordingly type the following at the console for starting the client

```
java -cp Remote/target/Remote-0.0.1-SNAPSHOT.jar:Client/target/Client-0.0.1-SNAPSHOT.jar de.htwsaar.wirth.client.gui.Main
```

:bangbang: **_Note that this can and will be automated in a future version_**
