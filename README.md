#Steps to Run this application in your local

1) ./gradlew clean build //Build the project and create .war file in Builds/libs folder of the project
2) Move it to webapps of Tomcat cp build/libs/demo-0.0.1-SNAPSHOT.war /Users/pavana.m/Documents/apache-tomcat-11.0.
   9/webapps Go to bin folder of tomcat and give edit access chmod +x startup.sh to startup, shutdown and Catalina files if
   needed
3) Start application ./startup.sh
4) Stop application ./shutdown.sh
5) Check catalina logs tail -n 100 /Users/pavana.m/Documents/apache-tomcat-9.0.90/logs/catalina.out
6) Access the application in browser http://localhost:8080/demo-0.0.1-SNAPSHOT/employee/list?dept=Engineering 