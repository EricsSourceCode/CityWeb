rem @echo off

rem Compile Java files.

rem SET JAVA_HOME="C:\Javajdk"
rem SET JDK_HOME=%JAVA_HOME%
rem SET JRE_HOME="C:\Javajdk\jre"

rem SET CLASSPATH=C:\Javajdk\lib;C:\Javajdk\jre\lib;

rem SET PATH=%PATH%;%JAVA_HOME%\bin;

rem Compile all of them.
cd \Eric\Main\EconData
del *.class


rem cls
Javac -Xlint -Xstdout Build.log *.java

rem type Build.log
