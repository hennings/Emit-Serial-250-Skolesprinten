# Emit-Serial-250-Skolesprinten

**EPT:**
IL Tyrving har arrangert skolesprint for skolene i Bærum siden ca 1995.

De siste årene har vi brukt en løsning med anonyme emit-brikker, og strekkoder.

```shell
mvn install:install-file -Dfile=lib/commapi/comm.jar -DgroupId=javax.comm -DartifactId=comm -Dversion=2.0.3 -Dpackaging=jar
```


For å kjøre versjonen for emitag: `java net.spjelkavik.emit.emitag.AnonEmitagApp COM50`

For å kjøre for vanlige brikker: `java net.spjelkavik.emit.ept.AnonEmitTradApp COM12`

Kopier `javax.comm.properties` til `%JAVA_HOME%/lib` !
...og `jre/lib`

Data source: (odbc 32 or odbc 64)
`jdbc:odbc:etime-java`



**Emitag:**
Start with `-DRELAY` to enable Night Hawk mode.





**Next generation:**
Test: jSerialComm or jrxtx
Test: ucanaccess

