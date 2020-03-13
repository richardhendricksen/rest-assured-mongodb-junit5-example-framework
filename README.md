# REST Assured Mongodb JUnit5 example framework ![CI](https://github.com/richardhendricksen/rest-assured-mongodb-junit5-example-framework/workflows/CI/badge.svg)

This repository contains an example testframework for testing REST based services using REST Assured.  
Custom written MongoAssert framework is used to validate the state of the embedded MongoDB during tests. All the tests and boilerplate are written using JUnit5.

## App under test

The repository includes an example service that is used as the app under test. It a simple Todo service written in Java using Spring Boot.
The endpoints are:

|Method |Endpoint         | Description              |
|------ |-----------------|--------------------------|
| GET   |`\api\posts`     |retrieve list of todos    |
| GET   |`\api\posts\{id}`|retrieve single todo by id| 
| POST  |`\api\posts`     |add todo                  | 
| PUT   |`\api\posts\{id}`|edit todo by id           | 
| DELETE|`\api\posts\{id}`|delete todo by id         | 

You can run the service using `mvn spring-boot:run`. It expects a Mongodb database on port `27017`.

## Test framework
The REST Assured tests use their own embedded mongodb for the tests. The framework has methods for inserting testdata in the database to creat the needed state for the test.  
Between tests the collections are cleared.  
MongoAssured is used to validate the state of the database during test.  

### Running test
Run the tests using `mvn clean verify`.  

## Set state in database
The framework has methods to insert testdata into the database to create the needed state. Example usage:
```java
insertTestdata("todos", "singleTodo");
```
Which refers to collection `todo` and resourcefile `src\test\resources\testdata\todos\singleTodo.json`.

## Validating database state
For validating the state of the database a custom MongoAssured framework is used. Example usage:
```java
database("test")
        .collection("todos")
        .find()
        .body("size()", is(1))
        .body("[0]._id", is(not(nullValue())))
        .body("[0].title", is("Test"))
        .body("[0].completed", is(false))
        .body("[0].createdAt", is(not(nullValue())));
```
The `find()` method supports MongoDB BSON queries.  
The `body()` method is overloaded and supports JSONPath with a Hamcrest matcher or only a Hamcrest matcher.  
For the latter it will try to match the whole JSON body. For example `.body(is(not(nullValue())))`  
