export MYPIDGIN="."
classpath="$MYPIDGIN/classes"
classpath="$classpath:$MYPIDGIN/lib/java-cup-11a.jar"
classpath="$classpath:$MYPIDGIN/lib/jetty-all.jar"
classpath="$classpath:$MYPIDGIN/lib/servlet-api.jar"
classpath="$classpath:$MYPIDGIN/lib/JSON-java.jar"
classpath="$classpath:$MYPIDGIN/lib/commons-lang3-3.3.2.jar"
classpath="$classpath:$MYPIDGIN/lib/junit-4.10.jar"

java -Xmx16G -Xms16G -classpath $classpath accrue.server.LaunchServer $1