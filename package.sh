export MAVEN_OPTS=-Xmx1024m
mvn clean:clean
mvn package -U -Dmaven.test.skip=true -DfailIfNoTests=false
