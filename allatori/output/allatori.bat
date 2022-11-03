@echo off
set ProjectPath=D:\develop\wokspace\guoshun\web-api
set OutPutPath=%ProjectPath%\allatori\output
set WebApiWebPath=%OutPutPath%\web-api-web

echo "=================================mvn================================="
cd %ProjectPath%
call mvn clean package -P allatori -Dmaven.test.skip=true

if exist %WebApiWebPath% (
  rd /s/q %WebApiWebPath%
)
md %WebApiWebPath%

cd %WebApiWebPath%

echo "=================================��ѹwar================================="
jar -xf %ProjectPath%/web-api-web/target/web-api-web.war

echo "=================================����jar================================="
xcopy /Y %OutPutPath%\web-api-controller-1.0-SNAPSHOT.jar %WebApiWebPath%\WEB-INF\lib

xcopy /Y %OutPutPath%\web-api-service-1.0-SNAPSHOT.jar %WebApiWebPath%\WEB-INF\lib

xcopy /Y %OutPutPath%\web-api-manage-1.0-SNAPSHOT.jar %WebApiWebPath%\WEB-INF\lib

echo "=================================���´��war================================="
jar -cf web-api-web.war *

echo "=================================�ƶ�war================================="
move %WebApiWebPath%\web-api-web.war %OutPutPath%

cd ..
rd /s/q %WebApiWebPath%
del %OutPutPath%\web-api-controller-1.0-SNAPSHOT.jar
del %OutPutPath%\web-api-service-1.0-SNAPSHOT.jar
del %OutPutPath%\web-api-manage-1.0-SNAPSHOT.jar

exit
