# Beam Phone Service API

Beam Phone Service API is a Spring Boot-based application that manages phone bookings. It provides endpoints for listing all phones, booking a phone, returning a phone, and getting details of a specific phone.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

Before running the application, ensure you have the following installed:

- [Docker](https://www.docker.com/get-started)
- [Docker Compose](https://docs.docker.com/compose/install/)

### Installing and Running with Docker

1. **Clone the Repository**

    ```bash
    git clone https://github.com/abhinav-jain09/beam.git
    cd beam
    ```

2. **Build the Docker Image**

   To build the Docker image for the application, run the following command:

    ```bash
    docker build -t beam .
    ```

   This command builds a Docker image named `beam` using the Dockerfile in the current directory.

3. **Running the Application with Docker Compose**

   Once the image is built, you can run the application using Docker Compose:

    ```bash
    docker-compose up
    ```

   This command starts all services defined in your `docker-compose.yml` file. The application should now be running and accessible.

## API Endpoints

The application exposes the following endpoints:

- `GET /phones`: Retrieve a list of all available phones.
- `POST /phones/{phoneName}/book`: Book a phone by its name.
- `GET /phones/{phoneName}`: Get details of a specific phone by name.
- `POST /phones/{phoneName}/return`: Return a previously booked phone.

## Swagger API Documentation

You can access the Swagger UI for API documentation at:

`http://localhost:8080/swagger-ui/`

This UI provides an interactive way to explore and test the API endpoints.

## Testing with Postman

A Postman collection is available for easier testing of the API endpoints. You can import the collection into Postman using the provided JSON file:

1. Open Postman.
2. Click on the 'Import' button at the top left.
3. Select 'Raw text' and paste the content of the `PhoneServiceAPI_PostmanCollection.json` file.
4. Click 'Continue' and then 'Import'.

## Built With

- [Spring Boot](https://spring.io/projects/spring-boot) - The framework used
- [Maven](https://maven.apache.org/) - Dependency Management
- [Docker](https://www.docker.com/) - Containerization platform

## Authors

- Abhinav Jain


