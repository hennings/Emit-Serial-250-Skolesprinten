EPT:
IL Tyrving har arrangert skolesprint for skolene i B�rum siden ca 1995.

De siste �rene har vi brukt en losning med anonyme emit-brikker, og strekkoder.

mvn install:install-file -Dfile=lib/commapi/comm.jar -DgroupId=javax.comm -DartifactId=comm -Dversion=2.0.3 -Dpackaging=jar


For � kj�re versjonen for emitag:
java net.spjelkavik.emit.emitag.AnonEmitagApp COM50

For � kj�re for vanlige brikker:
java net.spjelkavik.emit.ept.AnonEmitTradApp COM12

Kopier javax.comm.properties til %JAVA_HOME%/lib !
...og jre/lib

Data source: (odbc 32 or odbc 64)
jdbc:odbc:etime-java



Emitag:
Start with -DRELAY to enable Night Hawk mode.





Next generation:
Test: jSerialComm or jrxtx
Test: ucanaccess

