@echo off

cd ..

if not exist tmpclasses mkdir tmpclasses
if not exist classes mkdir classes

echo *** Compiling source files...
javac -g:none -bootclasspath c:\WTK21\lib\cldcapi11.zip;c:\WTK21\lib\midpapi20.zip -d tmpclasses -classpath tmpclasses src\*.java

echo *** Preverifying class files...
c:\WTK21\bin\preverify -classpath c:\WTK21\lib\cldcapi11.zip;c:\WTK21\lib\midpapi20.zip;tmpclasses -d classes tmpclasses

echo *** Packaging JAR file...
jar cmf bin\manifest.mf bin\Henway.jar -C classes .
jar umf bin\manifest.mf bin\Henway.jar -C res .

echo *** Please update the JAR file size in the JAD file.
cd bin
