# tree
tree bloc note app to take notes structured as a tree

## Pré requis
* Java
* Maven  
* Serveur SMTP (maildev) + Mysql 5 + Elasticsearch 

## configure the application properties

### without elastic-saerch and maildev 
it is possible to start the application without elasticsearch and maildev for that you simply have to remove the annotation
@EventListener() of the file *[src/main/java/malo/block/tree/quartz/schedulers/GenericScheduler.java](https://github.com/TKouchoanou/tree/blob/065aad908b6f6516a2860ec2c6fc402efa78dc61/src/main/java/malo/bloc/tree/quartz/schedulers/GenericScheduler.java#L25)

### mysql 
replace your sql host, databasename, username and password in file *[application.properties](https://github.com/TKouchoanou/tree/blob/065aad908b6f6516a2860ec2c6fc402efa78dc61/src/main/resources/application.properties#L2)

### use another sql databases
if you want to use another sql database you can change the configuration in the application properties file 
here are sql to create schema for *[postgreSQL](https://github.com/TKouchoanou/tree/blob/master/src/main/resources/db/migration/TableWithPostgreSQL.sql)

##### Theophane Malo Kouchoanou
