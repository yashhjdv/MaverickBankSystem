# Maverick Bank System – Backend Documentation

---

## 1. Project Overview

The Maverick Bank System is a secure, modular, and scalable backend application for managing banking operations such as customer accounts, transactions, loans, beneficiaries, and reporting. It is designed for both individual and business customers, supporting robust role-based access for customers, employees, and administrators.

**Technologies & Frameworks:**
- Java 17+
- Spring Boot
- Spring Data JPA (Hibernate)
- MySQL
- Maven
- Swagger UI (OpenAPI)
- (Previously: Spring Security & JWT, now open APIs for easy testing)

**System Architecture:**
- Layered architecture: Controller → Service → Repository → Database
- RESTful API design
- DTOs for request/response payloads
- Modular package structure

**[Insert system architecture diagram or textual representation here]**

---

## 2. Setup Instructions

**System Requirements:**
- Java 17 or higher
- Maven 3.6+
- MySQL 8.x

**Installation Guide:**
1. Clone the repository.
2. Configure your MySQL server and create a database named `MaverickBank`.
3. Update `src/main/resources/application.properties` with your DB credentials.
4. Build the project:
   ```
   mvn clean install
   ```
5. Run the backend:
   ```
   mvn spring-boot:run
   ```
6. Access Swagger UI at:  
   `http://localhost:9090/swagger-ui.html`

**Environment Variables / Properties:**
- `spring.datasource.url` – MySQL connection string
- `spring.datasource.username` – DB username
- `spring.datasource.password` – DB password
- `server.port` – Port for backend (default: 9090)
- (JWT properties are present but not used in the current open API version)

**[Insert screenshot of application.properties or environment setup here]**

---

## 3. API Documentation

All APIs are RESTful and accept/return JSON.  
Swagger UI provides interactive documentation and example payloads.

**Endpoints Overview:**

| HTTP Method | URL Path                                 | Description                        | Request Params/Body         | Response Example | Status Codes |
|-------------|------------------------------------------|------------------------------------|-----------------------------|------------------|--------------|
| POST        | /api/accounts/open                       | Open a new account                 | JSON body                   | Account JSON     | 200, 400     |
| GET         | /api/accounts/user/{userId}              | Get all accounts for a user        | Path: userId                | List<Account>    | 200, 404     |
| GET         | /api/accounts/{accountNumber}            | Get account by account number      | Path: accountNumber         | Account JSON     | 200, 404     |
| POST        | /api/transactions/deposit                | Deposit funds                      | JSON body                   | Transaction JSON | 200, 400     |
| POST        | /api/transactions/withdraw               | Withdraw funds                     | JSON body                   | Transaction JSON | 200, 400     |
| POST        | /api/transactions/transfer               | Transfer funds                     | JSON body                   | Transaction JSON | 200, 400     |
| GET         | /api/transactions/account/{accountId}    | Get all transactions for account   | Path: accountId             | List<Transaction>| 200, 404     |
| POST        | /api/loans                              | Apply for a loan                   | JSON body                   | Loan JSON        | 200, 400     |
| GET         | /api/loans/user/{userId}                 | Get all loans for a user           | Path: userId                | List<Loan>       | 200, 404     |
| GET         | /api/loans/{loanId}                      | Get loan by ID                     | Path: loanId                | Loan JSON        | 200, 404     |
| PUT         | /api/loans/{loanId}/status               | Update loan status                 | Path: loanId, JSON body     | Loan JSON        | 200, 400     |
| POST        | /api/beneficiaries/add                   | Add a beneficiary                  | JSON body                   | Beneficiary JSON | 200, 400     |
| GET         | /api/beneficiaries/user/{userId}         | Get all beneficiaries for a user   | Path: userId                | List<Beneficiary>| 200, 404     |

**[Insert screenshot of Swagger UI showing endpoints here]**

---

## 4. Database Schema

**Main Tables:**
- `users` (id, username, password, name, email, contactNumber, address)
- `roles` (id, name)
- `accounts` (id, accountNumber, accountType, ifscCode, branchName, branchAddress, balance, user_id)
- `transactions` (id, type, amount, date, account_id, description)
- `loans` (id, amount, interestRate, tenureMonths, status, applicationDate, user_id)
- `beneficiaries` (id, user_id, name, accountNumber, bankName, branchName, ifscCode)

**Relationships:**
- User 1—* Account
- Account 1—* Transaction
- User 1—* Loan
- User 1—* Beneficiary
- User *—* Role

**[Insert ER diagram or database schema screenshot here]**

---

## 5. Authentication & Authorization

- **Current Version:** All APIs are open for easy testing (no authentication).
- **Planned/Previous:** JWT-based authentication, role-based access (CUSTOMER, EMPLOYEE, ADMIN).
- **Token Structure:** (If enabled) JWT with userId, roles, expiration.
- **Role-based Access:** Endpoints can be restricted by role using annotations.

**[Insert screenshot of login/register API or JWT token structure if available]**

---

## 6. Business Logic / Modules

### User & Role Module
- Register, login, assign roles.
- Admin can manage users and employees.

### Account Module
- Open new account, view accounts by user/account number.
- Account types: savings, checking, business, etc.

### Transaction Module
- Deposit, withdraw, transfer funds.
- All transactions linked to accounts.
- Transaction history by account.

### Loan Module
- Apply for loan, view loans by user, update loan status.
- Loan status transitions:  
  - APPLIED → APPROVED/REJECTED  
  - APPROVED → DISBURSED  
  - REJECTED/DISBURSED are final

### Beneficiary Module
- Add beneficiary, view by user.

### Reporting Module
- Generate account statements, transaction history, financial reports.

**[Insert flow diagrams, sequence diagrams, or pseudo-code snippets here]**

---

## 7. Error Handling

- All errors returned as JSON with user-friendly messages.
- HTTP status codes: 400 (bad request), 404 (not found), 500 (server error), etc.
- Example error response:
  ```json
  {
    "timestamp": "2025-06-27T12:00:00",
    "status": 404,
    "error": "Not Found",
    "message": "Account not found",
    "path": "/api/accounts/123"
  }
  ```
- Backend logs technical details for debugging.

---

## 8. Testing

- **Tools:** JUnit, Spring Boot Test, Swagger UI, Postman
- **How to run tests:**
  ```
  mvn test
  ```
- **Coverage:** Service and controller layer tests.
- **Manual Testing:** All endpoints can be tested via Swagger UI/Postman.

**[Insert screenshot of test results or coverage report here]**

---

## 9. Deployment

- **Build:**  
  ```
  mvn clean install
  ```
- **Run:**  
  ```
  mvn spring-boot:run
  ```
- **Production Setup:**  
  - Configure `application.properties` for production DB and server.
  - Use Docker for containerization (optional).
  - CI/CD: Integrate with Jenkins, GitHub Actions, or similar for automated builds and deployments.

**[Insert screenshot of deployment pipeline or Docker setup here]**

---

## 10. Versioning & Changelog

- **API Versioning:** (Planned) Use `/api/v1/` style versioning for future updates.
- **Changelog:** Track updates in `CHANGELOG.md` or project management tool.

---

## 11. Contributing Guidelines

- **Folder Structure:**  
  - `controller/` – REST controllers  
  - `service/` – Business logic  
  - `repository/` – JPA repositories  
  - `entity/` – JPA entities  
  - `config/` – Configuration classes

- **Naming Conventions:**  
  - Classes: PascalCase  
  - Variables/Methods: camelCase

- **Code Style:**  
  - Follow Java and Spring Boot best practices.
  - Use meaningful commit messages.

- **Pull Requests:**  
  - Branch naming: `feature/`, `bugfix/`, `hotfix/`
  - Submit PRs with clear descriptions.

---

## 12. FAQs / Troubleshooting

- **Common Issues:**
  - DB connection errors: Check `application.properties`.
  - Port conflicts: Change `server.port`.
  - "Can't parse JSON": Check for circular references in entities.

- **Resetting DB:**  
  - Drop and recreate the `MaverickBank` database.

- **Regenerate tokens:**  
  - (If JWT enabled) Re-login to get a new token.

- **Contact:**  
  - [Add contact info or support channel here]

---

## 13. References

- Full problem statement (see below)
- [SBI Reference](https://www.sbi.com/)

---

## 14. Problem Statement (Reference)

*(Paste your full problem statement here for context, as provided above.)*

---

**[Add screenshots for each section as you build your documentation]**

