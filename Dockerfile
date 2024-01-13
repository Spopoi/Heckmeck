# Use an official OpenJDK runtime as a parent image
FROM dew54/heckmeck:base

# Set the working directory to /Heckmeck
WORKDIR /Heckmeck

# Copy the current directory contents into the container at /Heckmeck
RUN git pull
# Build the application using Gradle
RUN ./gradlew build

# Specify the command to run on container start
CMD ["./gradlew", "run"]