JAVAC = javac
JAVA = java
SOURCES = $(wildcard *.java)

main: $(SOURCES)
	$(JAVAC) -cp "lib/mysql-connector-java-8.0.16.jar" DatabaseConnector.java
	$(JAVAC) HTTPThread.java
	$(JAVAC) WebAPI.java
	$(JAVAC) SocketServer.java

load: $(SOURCES)
	$(JAVAC) -cp "lib/mysql-connector-java-8.0.16.jar" DatabaseLoader.java
	$(JAVAC) -cp "lib/mysql-connector-java-8.0.16.jar" DatabaseInsert.java

runwindows: main
	$(JAVA) -cp ".;./lib/mysql-connector-java-8.0.16.jar" SocketServer

runlinux: main
	$(JAVA) -cp ".:./lib/mysql-connector-java-8.0.16.jar" SocketServer

loadwindos: load
	$(JAVA) -cp ".;./lib/mysql-connector-java-8.0.16.jar" DatabaseLoader
	$(JAVA) -cp ".;./lib/mysql-connector-java-8.0.16.jar" DatabaseInsert

loadlinux: load
	$(JAVA) -cp ".:./lib/mysql-connector-java-8.0.16.jar" DatabaseLoader
	$(JAVA) -cp ".:./lib/mysql-connector-java-8.0.16.jar" DatabaseInsert


clean:
	rm *.class
