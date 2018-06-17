mvn install:install-file -Dfile=lib/commapi/comm.jar -DgroupId=javax.comm -DartifactId=comm -Dversion=2.0.3 -Dpackaging=jar
./mvnw install:install-file -Dfile=lib/sqljdbc4.jar -DgroupId=com.microsoft -DartifactId=sqlserver -Dversion=3.0.1301 -Dpackaging=jar

