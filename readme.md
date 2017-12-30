## Synopsis
This project is demo project for dygraph which is created on top of spring-boot, JSP, JSTL, JPA, H2 Database, bootstrap, jquery. The Graph is drawn with random data and we can change the range of the data. In addition, this project has the complete JUnit examples which tests Spring boot web application.

## Installation
You can run this demo with the following command. 

```sh
$ cd /go/to/project
$ mvn spring-boot:run
```

After build with .war file in the project, you can run it with java command as follows.
```sh
$ cd /go/to/project
$ mvn clean package
$ cd target
$ 
```
or
```sh
$ npm start
```

You can see chat app on your browser http://localhost:3000.

## API Reference



- message : echo will return the same message sent from client with 'message' event.
- getPost : return post object with 'getPost' event
```javascript
    {
        id : 'id',
        title : 'title',
        body : 'test body',
        userId : 1
    }
```
- connected : return username with index.

## Contributors
Park, KyoungWook (Kevin) / sirius00@paran.com

## License
This project is licensed under the MIT License