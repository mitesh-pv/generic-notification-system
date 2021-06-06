# Generic Notification System

This repository contains a project to demonstrate centralized generic notification service that can be used by variety consuming applications to get notifications across multiple platforms (eg. email, slack etc... )

The project is developed using **java-11**, **spring boot 2.5** and **Apache Maven** build tool.\
It Uses **Kafka** as a message broker.


### High Level Design

This system makes use of **RESTful** API for accepting **createNewNotification** requests.
The **createNewNotification** API sends the new notification events to **Kafka brokers**.
There is a Consumer watcher that continuously polls the kafka topics to search for new event, as a new notification is added to the kafka topic, consumer receives it and uses that to multicast the message to multiple communication platforms.


<img width="933" alt="Screen Shot 2021-06-06 at 2 27 53 PM" src="https://user-images.githubusercontent.com/28482024/120918714-7186f080-c6d3-11eb-990e-09d3da47ed84.png">

### Sequence Diagram

Following sequence diagram depicts the flow of events from and to the notification system.


![Untitled Diagram](https://user-images.githubusercontent.com/28482024/120919465-55854e00-c6d7-11eb-82f8-3bd06114f2b3.png)

### Steps to start the application in local

#### 1. Setup Kafka Cluster

* Download Kafka -> 
https://kafka.apache.org/downloads

* Start up zookeeper server

```bash
./bin/zookeeper-server-start.sh ./config/zookeeper.properties
```  
* Configure kafka brokers

Create 3 copies of kafka server.properties files in ./conf dir as server-1.properties, server-2.properties and server-3.properties and 
edit following properties 

server-1.properties
```groovy
broker.id=0
listeners=PLAINTEXT://localhost:9092
log.dirs=/tmp/kafka-logs-1
```

server-2.properties
```groovy
broker.id=1
listeners=PLAINTEXT://localhost:9093
log.dirs=/tmp/kafka-logs-2
```

server-3.properties
```groovy
broker.id=2
listeners=PLAINTEXT://localhost:9094
log.dirs=/tmp/kafka-logs-3
```
* Start Zookeeper
```groovy
./bin/zookeeper-server-start.sh ./config/zookeeper.properties
```

* Start the above 3 kafka servers
```groovy
./bin/kafka-server-start.sh ./config/server-1.properties
```
```groovy
./bin/kafka-server-start.sh ./config/server-2.properties
```
```groovy
./bin/kafka-server-start.sh ./config/server-3.properties
```

* Create 2 kafka topics

```groovy
./bin/kafka-topics.sh --create --topic client-1-events -zookeeper localhost:2181 --replication-factor 1 --partitions 4
```
```groovy
./bin/kafka-topics.sh --create --topic client-2-events -zookeeper localhost:2181 --replication-factor 1 --partitions 4
```

#### 2. Start Producer and Consumer Spring boot application

Use maven command 

```groovy
mvn clean install spring-boot:run
```

### Use the Following request format to test the application

```cURL
curl --location --request POST 'http://localhost:8080/notification-system/v1/new-event/' \
--header 'Content-Type: application/json' \
--data-raw '{
    "notificationEventId": 1,
    "clientId" : 456,
    "message" : {
        "subject": "status change",
        "from" : "ex@example.com",
        "to" : "ex@example.com",
        "body" : "Status of your ticket changed from verify to complete"
    }
}'
```

### Assumptions considered

#### Assumption 1
For sending email notification, **sendGrid** API is used.\
For security reasons the api key is removed from the code base so email will not be received in real time, but **logs** can be helpful to debug the flow.

#### Assumption 2 
The Client needs to be registered with the service in order to get the notification.\
In this case only 2 client ID's have been registered with the service i.e. **456** and **290**.\
So the clientId field in the request must contain either of the 2, otherwise it'll throw 500 exception

#### Assumption 3
The consumer currently implements only email handler so only email notifications are sent.
But it can be easily scaled to implement other channels also (eg slack etc...)


### Screenshots of the results

#### 1. Successfully notification request

<img width="1590" alt="Screen Shot 2021-06-06 at 12 08 37 PM" src="https://user-images.githubusercontent.com/28482024/120920434-5ff61680-c6dc-11eb-953b-659338ca05b2.png">

#### 2. Successfully notification request

Email notification received

<img width="1357" alt="Screen Shot 2021-06-06 at 12 05 47 PM" src="https://user-images.githubusercontent.com/28482024/120920456-81ef9900-c6dc-11eb-9d81-977f362e4ed4.png">

#### 3. Logs 

1. Producer

<img width="1636" alt="Screen Shot 2021-06-06 at 12 07 39 PM" src="https://user-images.githubusercontent.com/28482024/120920489-b19ea100-c6dc-11eb-8b8b-a0bcb879b0d5.png">


2. Consumer

<img width="1638" alt="Screen Shot 2021-06-06 at 12 07 58 PM" src="https://user-images.githubusercontent.com/28482024/120920494-b8c5af00-c6dc-11eb-9c3e-adb3679b53a9.png">






















