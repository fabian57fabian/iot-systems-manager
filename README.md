# iot-systems-manager
A simple iot system data manager for Advanced Programming Techniques @ unifi.

This code with be the official project for the exam involving:
- Java project
- Using Maven
- Using TDD - Unit tests / Integration tests / e2e tests
- Coveralls
- Continous integration with Github Actions

**Continous Integration** workflow:

[![Java CI with Maven in Linux](https://github.com/fabian57fabian/iot-systems-manager/actions/workflows/maven.yml/badge.svg)](https://github.com/fabian57fabian/iot-systems-manager/actions/workflows/maven.yml)

**Coveralls** coverage percentage:

[![Coverage Status](https://coveralls.io/repos/github/fabian57fabian/iot-systems-manager/badge.svg?branch=master)](https://coveralls.io/github/fabian57fabian/iot-systems-manager?branch=master)

**SonarCloud** Code quality states:

[![Quality gate](https://sonarcloud.io/api/project_badges/quality_gate?project=fabian57fabian_iot-systems-manager)](https://sonarcloud.io/summary/new_code?id=fabian57fabian_iot-systems-manager)

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=fabian57fabian_iot-systems-manager&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=fabian57fabian_iot-systems-manager)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=fabian57fabian_iot-systems-manager&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=fabian57fabian_iot-systems-manager)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=fabian57fabian_iot-systems-manager&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=fabian57fabian_iot-systems-manager)
</br>
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=fabian57fabian_iot-systems-manager&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=fabian57fabian_iot-systems-manager)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=fabian57fabian_iot-systems-manager&metric=bugs)](https://sonarcloud.io/summary/new_code?id=fabian57fabian_iot-systems-manager)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=fabian57fabian_iot-systems-manager&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=fabian57fabian_iot-systems-manager)
</br>
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=fabian57fabian_iot-systems-manager&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=fabian57fabian_iot-systems-manager)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=fabian57fabian_iot-systems-manager&metric=sqale_index)](https://sonarcloud.io/summary/new_code?id=fabian57fabian_iot-systems-manager)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=fabian57fabian_iot-systems-manager&metric=duplicated_lines_density)](https://sonarcloud.io/summary/new_code?id=fabian57fabian_iot-systems-manager)
</br>
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=fabian57fabian_iot-systems-manager&metric=coverage)](https://sonarcloud.io/summary/new_code?id=fabian57fabian_iot-systems-manager)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=fabian57fabian_iot-systems-manager&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=fabian57fabian_iot-systems-manager)

# Requirements

- Java 8
- Maven
- Docker

# Usage

From [REPO_DIR]/iotsystem/ directory:

This app needs a mongoDb database. If you don't have an installed db, you can launch it with docker:

```bash
docker run -p 27017:27017 --rm mongo:4.4.3
```

Then, in a separate terminal, we can build a fatjar with following command:

```bash
mvn package
```

Now you can launch the builded jar inside **target** directory called **iotsystem-[version]-jar-with-dependencies.jar** :

```bash
java -jar target/iotsystem-0.2-SNAPSHOT-jar-with-dependencies.jar
```

# Tests

Unit tests, Integration tests, E2E tests and Mutation testing with pit can be launched by following command:

```bash
mvn clean verify -Ppit
```
