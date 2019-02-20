# HMCTS properties volume library

[![Build Status](https://travis-ci.com/hmcts/properties-volume-spring-boot-starter.svg?branch=master)](https://travis-ci.com/hmcts/properties-volume-spring-boot-starter)
[ ![Download](https://api.bintray.com/packages/hmcts/hmcts-maven/properties-volume-spring-boot-starter/images/download.svg) ](https://bintray.com/hmcts/hmcts-maven/properties-volume-spring-boot-starter/_latestVersion)

This is a Spring Boot _starter_ library to read application configuration from Azure Keyvaults mounted as flexvolumes


## Usage
Include the module into the dependency list.
```groovy
 compile 'uk.gov.hmcts.reform:properties-volume-spring-boot-starter:x.x.x'
```

To define which directories or files contain Azure Keyvault entries update application.yaml to define the related paths.
For instance:
```yaml
spring:
  application:
    name: propertiesvolume-example
  cloud:
    propertiesvolume:
      enabled: true
      paths: /kvmnt/this,/kvmnt/that,/kvmount/other
```
If a path is a directory all the contained files are loaded.
For each file found, the file name is the Azure Keyvault key and the file body is the related value. 

### Prerequisites

- [JDK 8](https://www.oracle.com/java)


## Building

The project uses [Gradle](https://gradle.org) as a build tool but you don't have install it locally since there is a
`./gradlew` wrapper script.  

To build project please execute the following command:

```bash
./gradlew build
```

## Developing

### Coding style tests

To run all checks (including unit tests) please execute the following command:

```bash
./gradlew check
```

## Versioning

We use [SemVer](http://semver.org/) for versioning.
For the versions available, see the tags on this repository.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details.
