# Buildtool Backend

This repository contains the **backend** for the Buildtool project, a simple tool designed to consume and display data from a Jenkins Pipeline, via the Jenkins API.

The backend is built with **Spring Boot** and provides APIs for displaying pipeline details, build details and console output

---

## Features

- **RESTful APIs**: Exposes endpoints pipeline, build details and console output.

---

## Tech Stack

- **Backend Framework**: Spring Boot

---

## Installation & Setup

### Prerequisites

- Java 17+
- A Jenkins Pipeline/Job
- Maven

### Steps

1. Clone the repository:
   ```bash
   git clone https://github.com/jordantrent/buildtool-api.git
   cd buildtool-api

2. Configure your Jenkins details in application.properties:
   ```bash
   jenkins.url=${JENKINS_URL}
   jenkins.username=${JENKINS_USERNAME}
   jenkins.apiToken=${JENKINS_APITOKEN}

3. Run the application:
   ```bash
   ./mvnw spring-boot:run

4. Access the API at http://localhost:8080
