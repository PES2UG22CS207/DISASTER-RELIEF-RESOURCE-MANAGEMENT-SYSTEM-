# Relief System

A Spring Boot web application for managing disaster relief operations. It includes modules for users, camps, requests, warehouses, inventory, allocations, deliveries, and reports.

## Tech Stack

- Java 21
- Spring Boot 4
- Spring MVC
- Spring Data JPA
- Spring Security
- Thymeleaf
- MySQL
- H2 for testing
- Bootstrap and custom CSS

## Features

- Role-based web application for admin, camp manager, logistics, and warehouse workflows
- Request creation and approval flow
- Warehouse and inventory management
- Resource management with expiry date support
- Allocation and delivery tracking
- Dashboard pages for different user roles

## Project Structure

- `src/main/java/com/disaster/relief_system/controller` - web and API controllers
- `src/main/java/com/disaster/relief_system/service` - business logic
- `src/main/java/com/disaster/relief_system/repository` - JPA repositories
- `src/main/java/com/disaster/relief_system/entity` - database entities
- `src/main/java/com/disaster/relief_system/strategy` - allocation strategy classes
- `src/main/java/com/disaster/relief_system/factory` - factory classes
- `src/main/java/com/disaster/relief_system/observer` - observer support
- `src/main/java/com/disaster/relief_system/decorator` - notification decorator classes
- `src/main/resources/templates` - Thymeleaf HTML pages
- `src/main/resources/static` - CSS and static assets

## Prerequisites

- Java 21
- Maven
- MySQL server running locally

## Database Setup

Create a MySQL database named `disaster_relief_db`.

Update the database credentials in `src/main/resources/application.properties` if needed.

## Run Locally

```powershell
git clone https://github.com/PES2UG22CS207/DISASTER-RELIEF-RESOURCE-MANAGEMENT-SYSTEM-.git
cd DISASTER-RELIEF-RESOURCE-MANAGEMENT-SYSTEM-
./mvnw.cmd spring-boot:run
```

The application runs on port `8085` by default.

## Configuration

Main application settings are in `src/main/resources/application.properties`.

Key settings include:

- MySQL datasource URL
- Database username and password
- Hibernate auto-update
- Thymeleaf cache disabled for development
- Server port `8085`

## Notes

- Do not commit real database passwords to a public repository.
- If you want to change the port, update `server.port` in `application.properties`.

## Build

```powershell
./mvnw.cmd clean package
```

## License

No license has been added yet.