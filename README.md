
# GWO-SA: Gray Wolf Optimization with Simulated Annealing (Java Gradle Project)

This repository implements the Gray Wolf Optimization (GWO) algorithm combined with Simulated Annealing (SA) in Java using Gradle as the build tool.

## Prerequisites

To build and run the project, you need the following installed on your system:

- **Java Development Kit (JDK)**: Version 8 or later. [Download here](https://www.oracle.com/java/technologies/javase-downloads.html).
- **Gradle** (Optional): Although this project includes a Gradle Wrapper, having Gradle installed can be useful. [Download here](https://gradle.org/releases/).

---

## Building the Project

1. **Clone the Repository**:
   Open a terminal and run:
   ```bash
   git clone https://github.com/mgolfam/gwo-sa.git
   cd gwo-sa
   ```

2. **Build the Project**:
   Use the Gradle Wrapper to build the project:
    - On **Unix-based systems**:
      ```bash
      ./gradlew build
      ```
    - On **Windows**:
      ```bash
      gradlew.bat build
      ```

   This will:
    - Compile the source code.
    - Run the tests.
    - Package the application into a JAR file.

---

## Running the Application

To execute the main application:
- On **Unix-based systems**:
  ```bash
  ./gradlew run
  ```
- On **Windows**:
  ```bash
  gradlew.bat run
  ```

This will run the `main` class specified in the `build.gradle` file.

---

## Project Structure

- **`src/main/java`**: Contains the source code.
- **`src/test/java`**: Contains unit tests for the project.
- **`build.gradle`**: Build script defining dependencies, tasks, and configurations.

---

## Additional Information

### Gradle Wrapper
The Gradle Wrapper (`gradlew` and `gradlew.bat`) allows you to run Gradle tasks without needing a local Gradle installation. It ensures consistent builds by using the Gradle version specified in the project.

### Troubleshooting
If you encounter issues:
- Ensure `JAVA_HOME` is correctly set to your JDK directory.
- Verify that `PATH` includes the directory containing Java binaries.
- Check permissions for executing Gradle Wrapper scripts.

For more about Gradle, see the [Gradle User Manual](https://docs.gradle.org/current/userguide/userguide.html).
