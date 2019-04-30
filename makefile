JAVAC = javac
JAVA = java
SOURCES = $(wildcard *.java)
WIN_CLASSPATH = ".;./lib/mysql-connector-java-8.0.16.jar;./lib/json-20180813.jar"
NIX_CLASSPATH = ".:./lib/mysql-connector-java-8.0.16.jar:./lib/json-20180813.jar"

win_main: $(SOURCES)
	$(JAVAC) -cp $(WIN_CLASSPATH) $(SOURCES)

nix_main: $(SOURCES)
	$(JAVAC) -cp $(NIX_CLASSPATH) $(SOURCES)

runwindows: win_main
	$(JAVA) -cp $(WIN_CLASSPATH) SocketServer

runlinux: nix_main
	$(JAVA) -cp $(NIX_CLASSPATH) SocketServer

clean:
	rm *.class
