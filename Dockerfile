# Start with a base image containing Java runtime (adoptopenjdk is an example)
FROM maven:3.8.1-openjdk-17
# Set the working directory inside the container
WORKDIR /app

# Copy the project's pom.xml and src code to the container
COPY ./pom.xml ./pom.xml
RUN mvn dependency:go-offline

COPY ./src ./src

# Build and install the project inside the container
RUN mvn package

# Command to run the application using Maven
CMD ["mvn", "exec:java"]
