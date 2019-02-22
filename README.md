# HMCTS properties volume library

[![Build Status](https://travis-ci.com/hmcts/properties-volume-spring-boot-starter.svg?branch=master)](https://travis-ci.com/hmcts/properties-volume-spring-boot-starter)
[ ![Download](https://api.bintray.com/packages/hmcts/hmcts-maven/properties-volume-spring-boot-starter/images/download.svg) ](https://bintray.com/hmcts/hmcts-maven/properties-volume-spring-boot-starter/_latestVersion)

This is a Spring Boot _starter_ library to read application configuration properties from files using the file name as key 
and the file content as value. This can be used in applications that have Azure Keyvaults mounted as flexvolumes, but there 
are probably other valid uses.


## Usage
Include the module into the dependency list.
```groovy
 compile 'uk.gov.hmcts.reform:properties-volume-spring-boot-starter:x.x.x'
```

To define which directories or files contain Azure Keyvault entries update *bootstrap.yaml* to define the related paths.
For instance:
```yaml
spring:
  application:
    name: properties-volume-example
  cloud:
    propertiesvolume:
      enabled: true
      prefixed: true
      paths: /mnt/secrets/this,/mnt/secrets/that,/mnt/secrets/other
```
By default both _enabled_ and _prefixed_ are true. This means that properties are
loaded from the given paths (i.e. enabled) and the file parent directory is used 
as prefix in the property name. 
If a path is a directory all the contained files are loaded.
For each file found:
- the parent directory is the Azure Keyvault name
- the file name is the Azure Keyvault key 
- the file body is the related value 
For instance given the following path:
```
/mnt/secrets/draft-store/primary-encryption-key
```
a property named as follows would be created:

```
draft-store.primary-encryption-key
```

If _enabled_ is explicitly set to false (it is true if omitted) then this is instead:
```
primary-encryption-key
```


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

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
