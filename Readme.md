# Cloud CMR App V2

## Overview
This project is to help the CMR to develop and enhance their IT by having a new application. It is composed of 2 modules: 
* cloudcmr-back: the backend, based on Spring boot (2.2.2-RELEASE at this time). RestFul API.
* cloudcmr-front: the frontend, based on VueJS (3 at this time). Using Vuex, Router, EsLint, Mocha + Chai and Cypress

## How to run locally

### Checkout the application
```
git checkout https://github.com/truar/cloudcmr-v2.git
```

### Build with maven
This step builds the javascript files with Babel in cloudcmr-front/target/dist. Then, the cloudcmr-back copy the cloudcmr-front/target/dist into cloudcmr-back/src/main/resources/public.
If you have nothing in your cloudcmr-back/src/main/resources/static, then the content of the publix directory will be accessible.
```
./mvnw clean install
```

### Run 
```
./mvnw --projects cloudcmr-back spring-boot:run
```
Now, you can access in your browser : http://localhost:8080/ (you should see the home page)

## If you want to develop on cloudcmr-front
You can use npm to start a webServer in development. Thanks to cloudcmr-front/vue.config.js, this will start an application listening http://localhost:8088.
Your webserver will be reloaded everytime you change your js files, and improving your development speed.
```
npm run serve
```

### Credit
A big thank you to https://github.com/jonashackt/spring-boot-vuejs, very easy to setup a skeleton project with Spring-boot 2 and VueJS 3.