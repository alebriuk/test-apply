# Java Back End Developer Test

**Context**: We would like you to build a small API to test your knowledge of Java Back End Development and related technologies.

Every hour, the server should automatically make a request to the Algolia API to retrieve the data of recently published articles 
about [Java on Hacker News](https://hn.algolia.com/api/v1/search_by_date?query=java). 
It should insert the data from this API into a database and also define a REST API that the client (e.g. Postman) will use to retrieve the data.

The service should return paginated results with a maximum of 5 items, be filterable by author, _tags, title, 
and also be searchable by month word (e.g. september) using the "created_at" field. It should also allow the user to remove items and not have those
should not reappear when the application is restarted.

An authorization parameter with a JWT must be sent in the headers to access the endpoints.

## STACK
You must use the following technologies to build the app:
* JAVA 8 > + Active LTS version of Spring Boot.
* Maven or Gradle.
* Database: PostgreSQL or MongoDB.
* Open JPA or Hibernate.
* Docker.

## CONSIDERATIONS
* The entire project must be uploaded to this repository.
* At least 30% test coverage (statements) for the server component.
* The artifacts (server API) must be Dockerized.
* There should be a docker compose to start the project, using at least the server and database images.
* Tests and linters should run on Github Actions.
* Good use of Spring Boot.
* Dependency injection with beans should be used.
* API Doc: Swagger, should be exposed at /api/docs.
* Use conventional commit and gitflow.

Other than that, you are free to use any other dependencies that are suitable for you.

When you finish, write an email to `<recruiter email>` to let us know.

Include a README that explains everything we need to do to run the demo application,
such as setting up a database, forcing a data refresh to populate the DB for the first time, assumptions, choices, etc.

If you have any questions about the task, please let us know.

Once again, thank you for your time and we are looking forward to seeing the results!