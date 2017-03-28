## MessageBoard 

<img src="http://www.bilder-upload.eu/upload/2584ea-1490713847.png" width="75" height="75" />&nbsp;&nbsp;&nbsp;             <img src="https://www.htwsaar.de/ingwi/logo.png" width="210" height="65" />

*MessageBoard* is a students' project made with Java RMI as part of the distributed systems lecture at htw saar.
Its main purpose is to depict a company's communication hierarchy in a distributed client-server system. Thus, each department of the company is represented as an individual group (server) in the system. Each group, except the root one, is child of a higher-level group, allowing messages to be cascaded top-down, beginning with the root group. Accordingly, the root group is the most public group with publicity being decreased by each level of the hierarchy.
Since each group has an administrator, that very ones are allowed to publish messages into the next higher level as well as adding and deleting users.


master | 
-------|
[![Build Status](https://travis-ci.com/makohn/MessageBoard.svg?token=z1kyyNNo3nk7k9bTgxPq&branch=master)](https://travis-ci.com/makohn/MessageBoard)|



### How to build
- Install Java 8 [JDK8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html).
- Install [maven](http://maven.apache.org/).
- Clone the project `git@github.com:makohn/MessageBoard.git` or download the [zip](https://github.com/makohn/MessageBoard/archive/master.zip) file. 
- Navigate into the **inner** MessageBoard directory and type `mvn clean install` at the console. 
- This should create a build folder containing two executable jar files.
  
### How to run
- Navigate into the **build/** directory
- Decide which program you want to execute. Usually it is quite reasonable to start the server first, but if there is a server already you're good to go with starting a client instance.
- To start the **server**, type
```
java -jar server.jar -g <groupname> -p <port>
```
&nbsp;&nbsp;&nbsp;&nbsp; if you want to start a root instance, or:
```
java -jar server.jar -g <groupname> -p <port> -ph <parent_ip_adr> -pg <parentgroup>
```
&nbsp;&nbsp;&nbsp;&nbsp; to connect to a existing server.
<br></br>
- To start the **client**, simply click on the jar file when using a graphical explorer. Otherwise type: 
```
java -jar client.jar
```
