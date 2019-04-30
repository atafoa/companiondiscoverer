JAVAC = javac
JAVA = java
SOURCES = $(wildcard *.java)

main: $(SOURCES)
	$(JAVAC) -cp "lib/mysql-connector-java-8.0.16.jar" DatabaseConnector.java
	$(JAVAC) HTTPThread.java
	$(JAVAC) WebAPI.java
	$(JAVAC) SocketServer.java

runwindows: main
	$(JAVA) -cp ".;./lib/mysql-connector-java-8.0.16.jar" SocketServer

runlinux: main
	$(JAVA) -cp ".:./lib/mysql-connector-java-8.0.16.jar" SocketServer


clean:
	rm *.class
