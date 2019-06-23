This project is built using Apache Ant. It is known to work with Ant
version 1.9.6. It requires Java 1.7.


Summary of directory structure:

+ bin/         executable scripts

   + launchServer.sh: launches the server for a given file. That is,
     it takes an argument of a PDG file to use, and starts up a web
     server on port 8080. This requires the environment variable
     $PIDGIN to be set to this directory.

   + pldiQuery.sh: script to run the queries for the PLDI 2014 paper (requires OS X).

+ build.xml    Ant build file

+ classes/     Java .class files. This directory is created by running ant.

+ cup/         CUP and JFlex files for the Pidgin query langauge

+ lib/         Java jar files needed by the tool

+ queries/     Queries we have used on PDG files

+ src/         Java source files

+ test/        Some test code. Contains Java source code.
