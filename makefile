JAVAC = javac
JAVA = java

SOURCES = $(wildcard *.java)

main: $(SOURCES)
	$(JAVAC) SocketServer.java

run: main
	$(JAVA) SocketServer

clean:
	rm *.class
