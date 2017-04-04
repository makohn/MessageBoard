## MessageBoard 

<img src="http://www.bilder-upload.eu/upload/2584ea-1490713847.png" width="75" height="75" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="https://www.htwsaar.de/ingwi/logo.png" width="200" height="70" />

*MessageBoard* is a students' project made with Java RMI as part of the distributed systems lecture at htw saar.
Its main purpose is to depict a company's communication hierarchy in a distributed client-server system. Thus, each department of the company is represented as an individual group (server) in the system. Each group, except the root one, is child of a higher-level group, allowing messages to be cascaded top-down, beginning with the root group. Accordingly, the root group is the most public group with publicity being decreased by each level of the hierarchy.
Since each group has an administrator, that very ones are allowed to publish messages into the next higher level as well as adding and deleting users.


master | 
-------|
[![Build Status](https://travis-ci.com/makohn/MessageBoard.svg?token=z1kyyNNo3nk7k9bTgxPq&branch=master)](https://travis-ci.com/makohn/MessageBoard)|

### Downloads
- [Release 1.0.0](https://github.com/makohn/MessageBoard/wiki/Downloads)

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
&nbsp;&nbsp;&nbsp;&nbsp; to connect to an existing server.
<br></br>
- To start the **client**, simply click on the jar file if using a graphical explorer. Otherwise type: 
```
java -jar client.jar
```
### Using the shell scripts

- If you are on a unixoid machine, you can also build and launch the application by using the bash scripts
- The bash scripts are located in the root directory of the project (**outer** MessageBoard directory)
- Make sure, that you have all the rights to execute the scripts. Run
```
chmod +x *sh
```
- To **build** the project, open a terminal and run
```
./build.sh
```
&nbsp;&nbsp;&nbsp;&nbsp; this will create a **MessageBoard/build/** folder containing the jars
- To **launch** the **server.jar**, simply run
```
./server_launcher.sh
```
&nbsp;&nbsp;&nbsp;&nbsp; this will help you to create a server instance on your machine.

## NOTE
To start **multiple client instances** on a OSX machine, you might have to copy the **client.jar**.
