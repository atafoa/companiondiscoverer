JAVAC = javac
JAVA = java

SOURCES = $(wildcard *.java)

main: $(SOURCES)
	$(JAVAC) -classpath lib/mysql-connector-java-8.0.16.jar SocketServer.java

run: main
	$(JAVA) -classpath lib/mysql-connector-java-8.0.16.jar SocketServer

clean:
	rm *.class
