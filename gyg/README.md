# Objective

## Key Points

- We want to assess your skills as a generalist software engineer.
- We are looking for people who are able to work on a variety of projects and technologies.
- We are not explicitly looking for expertise in a specific technology or domain. An ideal candidate would be able to work with a variety of technologies.

We would like you to build a web application that allows users to search for activities.
The project is meant to be a simple application that you can build in a few hours.
We are not looking for a production-ready application but we will assess your project both in terms of quality (use of best practices/robust architecture) and potential (scalability).

In this repository you will find a list of activities that you can use to build your application (please find the file `activities.json` in the `spring-boot-service/src/main/resources/static` folder).

We bootstrapped in advance 2 skeleton applications for the back end and the front end: you will find a skeleton SpringBoot backend application and a VueJS frontend application.

You can use them to speed up the implementation, or you can start from scratch choosing the technologies you are most familiar with, or you think most appropriate to fulfill the requirements.

# Requirements

- You need to provide a backend application meant to expose an API (REST or GraphQL).
- You need to provide a client application consuming the API exposed by the previous application and implement a UI.
- The client should display the list of activities to the user and allow the user to search for activities by title by
  providing an input field.
  - The fields to be displayed are:
    - title
    - price with currency
    - rating
    - whether it has a special offer or not
    - the activity's supplier name and their location
- You need to decide if the filtering logic should be implemented on the client or on the server: please make sure you implement this business logic on the application that you would like to be reviewed.
- Please ensure that the FULL project can be run locally with the given Docker compose file with `docker compose up --build`. Modify, add any dependencies as required
- Add notes on your architectural decisions.
- Please also document any features/improvements you left out in favor of time, preferably with sufficient details on "what, why & how".
- Additionally, make sure any assumptions you made are noted down.

# Prerequisites
* JDK version 17
* Build Tools
    * Gradle: Install Gradle for managing backend dependencies and building the application.
    * npm: Install npm for managing frontend dependencies

# Features
- Search activities by title
- Paginated results for large datasets
- Reads and stores activities and suppliers information from JSON files into an H2 in-memory database for rapid development and easy testing
- Integration with frontend frameworks like Vue.js to enhance user interface and user experience

# Assumptions
- **Data Consistency**: Assumes JSON files containing activities and suppliers are structured and consistent for loading into the database.
- **H2 Database**: Assumes H2 in-memory database is suitable for development and testing phases, chosen for ease and rapid development. Acknowledges that H2 may not scale well for large datasets or advanced search functionalities like full-text search.
- **Scalability Considerations**: Acknowledges that for a large-scale system requiring features such as full-text search and extensive filtering capabilities, alternative technologies like Elasticsearch (ES), Apache Solr, or other dedicated search engines would be preferred.
- **Error Handling During Data Loading**: If the data loader encounters errors while loading activities and suppliers from JSON files into the database, the application logs the error messages. Monitoring systems are configured to detect and alert for such issues to ensure timely resolution, prioritizing application availability and user experience.

# Architectural Decisions

## RESTful API Design
**Decision**: Design RESTful APIs for accessing activities.

**Rationale**: RESTful APIs are widely used and understood, allowing for easy integration with frontend frameworks and other services. They provide a standardized way to interact with the application.

## Use of H2 Database for Development
**Decision**: Use H2 in-memory database for development and testing.

**Rationale**: H2 is easy to set up and use, provides fast access, and is ideal for development environments. However, it's acknowledged that H2 is not suitable for production-scale applications due to its limitations in handling large datasets and advanced search functionalities.

## Potential Migration to Scalable Databases
**Decision**: Plan for potential migration to more scalable databases like Elasticsearch or Apache Solr.

**Rationale**: For production environments and larger datasets, a more robust and scalable database solution is necessary to handle full-text search, extensive filtering, and high availability requirements.

## Logging and Error Handling
**Decision**: Implement basic logging and error handling.

**Rationale**: Logging is essential for monitoring and troubleshooting. Basic error handling ensures that the application can log errors and continue running without crashing, improving user experience. Future enhancements will include more comprehensive logging and error handling mechanisms.

## Modular Service Design
**Decision**: Design services in a modular manner with clear interfaces.

**Rationale**: Modular design allows for easier maintenance and scalability. Clear interfaces between services facilitate future migrations, such as moving from H2 to Elasticsearch, without major changes to the overall architecture.

## Error Handling During Data Loading
**Decision**: Log errors during data loading and continue running the application.

**Rationale**: Logging errors instead of terminating the application ensures that users can still access available functionality, even if some data fails to load. This approach prioritizes application availability and user experience. Monitoring systems should be in place to detect and alert for such issues.

## Use of Spring Data JPA and Specifications
**Decision**: Use Spring Data JPA for database interactions and Specifications for dynamic queries.

**Rationale**: Spring Data JPA simplifies database operations and improves developer productivity. Specifications provide a flexible way to build dynamic queries, making it easier to add new search and filter criteria in the future.

# Setup
Run the Application
````
make up
````

Stop the Application
````
make down
````

Clean (Remove Containers and Volumes)
````
make clean
````


# Features/Improvements Left Out
- Improve logging


# How to submit your work

Please clone this repository and send us a zip file containing your work.

Also, please avoid bundling `node_modules` or other installed dependency directories in your zip file.

⚠️ **Make sure not to publish your work on a public repository.** ⚠️

We will grade your project after submission and eventually invite you for a remote live interview with our engineers.

Depending on the role you are applying for and your level of expertise, you have the flexibility to focus on implementing either the backend or the client application.
We welcome submissions with basic functionality on the side of the stack you are less familiar with, allowing us to assess your skills in your preferred domain.
If you opt for this approach, please specify in your notes which application you would like us to thoroughly review.
Alternatively, if you feel confident in both the backend and client application, we encourage you to state clearly in your notes that you would like a comprehensive review of both.
We are excited to thoroughly evaluate your work across the entire stack.

# Presentation

- We will ask you to present your work. Please be prepared to present your work, demo it, and explain your choices.
- We will ask clarifying questions about your code and your design choices.
- In addition to the presentation we will ask you to refactor the code, add features, or fix bugs:
  please be prepared to share your screen during the interview and to code live with us.
