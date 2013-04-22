set MAVEN_OPTS=%MAVEN_OPTS% -Xmx1024m
call mvn clean:clean
call mvn package -U -Dmaven.test.skip=true -DfailIfNoTests=false
@pause
