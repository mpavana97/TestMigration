# TestMigration

Steps to startup:

1) ./gradlew clean build
2) Move it to webapps of Tomcat cp build/libs/demo-0.0.1-SNAPSHOT.war /Patch to TOmcat/apache-tomcat-9.0.107/webapps (Tomcat 
   folder varies before an after upgrade)
3)  Give edit access  chmod +x startup.sh to startup, shutdown and Catalina files if needed
4) Start application ./startup.sh (Run in bin folder of tomcat)
5) Check catalina logs: tail -n 100 /Users/pavana.m/Documents/apache-tomcat-9.0.90/logs/catalina.out

The changes are on Hibernate 6 and Spring 5, Tomcat 10.1
If needed to check the Hibernate 5.x version, drop the last commit having Hibernate 6 changes
Endpoint:
http://localhost:8080/demo-0.0.1-SNAPSHOT/ad/listById?id=123
