# REST Assured Mongodb JUnit5 example framework ![CI](https://github.com/richardhendricksen/rest-assured-mongodb-junit5-example-framework/workflows/CI/badge.svg)

This repository contains an example testframework for testing REST based services using REST Assured.  
Custom written MongoAssert framework is used to validate the state of the embedded MongoDB during tests. All the tests and boilerplate are written using JUnit5.

## App under test

The repository also includes an example service that is used as the app under test. It is found in the src folder and it a simple Todo service written in Java using Spring-boot.
The endpoints are:

|Method |Endpoint         | Description              |
|------ |-----------------|--------------------------|
| GET   |`\api\posts`     |retrieve list of todos    |
| GET   |`\api\posts\{id}`|add todo                  | 
| POST  |`\api\posts`     |retrieve single todo by id| 
| PUT   |`\api\posts\{id}`|edit todo by id           | 
| DELETE|`\api\posts\{id}`|delete todo by id         | 

You can run the service using `mvn spring-boot:run`. It expects a Mongodb database on port `27017`.

## Running tests
Run the tests using `mvn verify`
