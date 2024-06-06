# Kerberos Protocol Authentication with Spring Boot(#kerberos-protocol-authentication-with-spring-boot)

## Project's Title
Kerberos Protocol Authentication with Spring Boot and React

## Project Description
This project is a comprehensive implementation of the Kerberos protocol for authentication, developed using Spring Boot for the backend and React for the frontend. It includes a custom-built Authentication Server (AS), Ticket Granting Server (TGS), and a dummy service to demonstrate the protocol. The React app serves as a test interface to verify the functionality of the authentication system.

### What the Application Does
The application simulates a Kerberos-based authentication system. Users can authenticate through the React app, which interacts with the Spring Boot backend to request and receive authentication tickets, demonstrating secure communication and identity verification.

### Why These Technologies Were Used
- **Spring Boot**: Chosen for its robust and scalable framework, making it easier to develop the authentication servers and services.
- **React**: Selected for its powerful, efficient, and flexible JavaScript library for building user interfaces, providing a responsive test application.
- **Kerberos Protocol**: Implemented to showcase a well-established, secure method for mutual authentication in distributed systems.

### Challenges Faced and Future Features
- **Challenges**: Implementing the Kerberos protocol from scratch, ensuring secure ticket exchanges, and managing session states.
- **Future Features**: Enhancing security features, adding more services for authentication, and integrating with a real-world database.

## Table of Contents
1. [Project's Title](#kerberos-protocol-authentication-with-spring-boot)
2. [Project Description](#project-description)
3. [How to Install and Run the Project](#how-to-install-and-run-the-project)
4. [How to Use the Project](#how-to-use-the-project)

## How to Install and Run the Project

### Prerequisites
- Java Development Kit (JDK) 11 or later
- Node.js and npm (for the React app)
- Maven (for building the Spring Boot application)

### Backend (Spring Boot)
1. Clone the repository:
   ```bash
   git clone "https://github.com/DevMahdiTR/kerbospringreact-implementation.git"
2. Navigate to the project directory:
   ```bash
    cd kerbospringreact-implementation
3. Build the project using Maven:
   ```bash
   mvn clean install
4. Configure The Database name and password in all 3 Backends [resources -> application.yml]:
   ```bash
   datasource:
     url: jdbc:postgresql://localhost:5432/database-name
     username: database-username
     password: database-password
     driver-class-name: org.postgresql.Driver
### Frontend (React)
1. Navigate to the React app directory:
   ```bash
    cd kerbospringreact-implementation\my-app
2. Install the dependencies:
   ```bash
   npm install
3. Start the React application:
   ```bash
   npm start
## How to Use the Project
### Authentication Flow
1. Open your browser and navigate to http://localhost:3000 to access the React app.
2. Register a new user or log in with existing credentials.
3. The React app will request authentication tickets from the Authentication Server (AS).
4. The AS validates the user credentials and issues a Ticket Granting Ticket (TGT).
5. The React app uses the TGT to request service tickets from the Ticket Granting Server (TGS).
6. The TGS issues service tickets which the app uses to access the dummy service.
   
### Visual Aids
1. Login Page:
2. Authentication Flow Diagram: 




   
