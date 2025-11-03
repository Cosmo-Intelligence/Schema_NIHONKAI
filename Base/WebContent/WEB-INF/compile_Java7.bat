cd /d "C:\Program Files\Apache Software Foundation\Tomcat 8.0\webapps\rtWebSchema\WEB-INF"
cd classes
del /Q /S com\
cd ..
"C:\Program Files\Java\jdk1.7.0_80\bin\javac"^
 -Xlint:deprecation^
 -Xlint:unchecked^
 -classpath .\lib\log4j-api.jar;^
.\lib\log4j-core.jar;^
..\..\..\lib\servlet-api.jar;^
..\..\..\lib\ojdbc6.jar;^
..\..\..\lib\xalan.jar;^
..\..\..\lib\xml-apis.jar;^
..\..\..\lib\xerces.jar;^
..\..\..\lib\xercesImpl.jar;^
..\..\..\lib\xsltc.jar;^
 -d classes src\com\yokogawa\theraris\logger\*.java^
 src\com\yokogawa\theraris\rtWeb\core\*.java^
 src\com\yokogawa\theraris\rtWeb\app\common\*.java^
 src\com\yokogawa\theraris\rtWeb\app\OrderSearch\*.java^
 src\com\yokogawa\theraris\rtWeb\app\Portal\*.java^
 src\com\yokogawa\theraris\rtWeb\app\summary\*.java^
 src\com\yokogawa\theraris\rtWeb\app\adverseEventRecordSearch\*.java^
 src\com\yokogawa\theraris\rtWeb\app\kansatsuRecordSearch\*.java^
 src\com\yokogawa\theraris\rtWeb\app\irradiationRecordSearch\*.java^
 src\com\yokogawa\theraris\rtWeb\app\interaction\rtReport\*.java^
 src\com\yokogawa\theraris\rtWeb\app\interaction\rtReport\beans\*.java^
 src\com\yokogawa\theraris\rtWeb\app\calendar\beans\*.java^
 src\com\yokogawa\theraris\rtWeb\app\calendar\*.java^
 src\com\yokogawa\theraris\rtWeb\app\ext\*.java^
 src\com\yokogawa\theraris\rtWeb\app\ext\bean\*.java^
 src\com\yokogawa\theraris\rtWeb\app\ext\dbaccess\*.java^
 src\com\yokogawa\theraris\rtWeb\app\ext\dbaccess\rris\*.java^
 src\com\yokogawa\theraris\rtWeb\app\ext\dbaccess\rtris\*.java^
 src\com\yokogawa\theraris\rtWeb\app\ext\servlet\*.java


