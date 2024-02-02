# KATA BANK Application Documentation

Welcome to the KATA BANK application! This documentation provides a comprehensive guide on how to use the application, covering various use cases and features. To interact with the application, we have prepared a set of Postman requests that you can import for testing.

## Table of Contents
1. [Getting Started](#getting-started)
2. [Bank Admin Operations](#bank-admin-operations)
   - [Client List](#client-list)
   - [Create Client](#create-client)
   - [Multi-Criteria Search](#multi-criteria-search)
   - [Batch Processing](#batch-processing)
3. [Client Operations](#client-operations)
   - [Deposit](#deposit)
   - [Withdrawal](#withdrawal)
   - [Account Statement](#account-statement)
   - [Add Account](#add-account)


## 1. Getting Started <a name="getting-started"></a>
### Running the Application
If you already have Java 17 installed, you can clone the repository, build the application, and run it as a regular JAR file.

```bash
git clone [repository-url]
cd katabank
./mvnw clean install
java -jar target/kata-bank-0.0.1-SNAPSHOT.jar

### Importing Postman Requests
1. Download and install [Postman](https://www.postman.com/).
2. Import the provided Postman collection: [KATA BANK Postman Collection](path/to/your/KATA_BANK_Postman_Collection.json).

## 2. Bank Admin Operations <a name="bank-admin-operations"></a>

### 2.1 Client List <a name="client-list"></a>
**User Story (US-4):**  
_In order to check the list of clients  
As a bank Admin  
I want to see the client list with their information_

#### Request:
- **Endpoint:** `/clients`
- **Method:** `GET`
- **Security:** Basic Authentication
  - **Username:** [Admin Email]
  - **Password:** [Admin Password]

### 2.2 Create Client <a name="create-client"></a>
**User Story (Create Client):**  
_In order to add a new client to the application 
As a bank Admin  
I want to create new client

#### Request:
- **Endpoint:** `/clients/save`
- **Method:** `POST`
- **Body (JSON):**
  ```json
  {
    "id": 101,
    "email": "test1@test.com",
    "fullName": "Test Test",
    "password": "test123",
    "roles": [
        "CLIENT"
    ]
}
- **Security:** Basic Authentication
  - **Username:** [Client Email]
  - **Password:** [Client Password]

### 2.3 Multi-Criteria Search <a name="multi-criteria-search"></a>
**User Story (US-5):**  
_In order to search the list of clients  
As a bank Admin  
I want to see the client list with their information based on a multi-criteria search_

**Note:** The query parameters (optional) for `fullName` and `email` should follow the pattern: `(equal|notequal|like|in|notin|under|over) followed by the value of the criteria`.

#### Request:
- **Endpoint:** `/clients/search`
- **Method:** `GET`
- **Parameters (query parameters):**
  - `fullName`: Specify the full name for the search. Example: `equal(someValue)`.
  - `email`: Specify the email for the search. Example: `like(someValue)`. 
- **Security:** Basic Authentication
  - **Username:** [Admin Email]
  - **Password:** [Admin Password]

### 2.4 Batch Processing <a name="batch-processing"></a>
**User Story (Batch Processing):**  
_In order to efficiently process client data  
As a bank Admin  
I want to execute a batch job to handle client information in bulk_

#### Request:
- **Endpoint:** `/clients/batch/start`
- **Method:** `POST`
- **Parameters (form-data):**
  - `file`: Upload a CSV file containing client data.
- **Security:** Basic Authentication
  - **Username:** [Admin Email]
  - **Password:** [Admin Password]

## 3. Client Operations <a name="client-operations"></a>

### 3.1 Deposit <a name="deposit"></a>
**User Story (US-1):**  
_In order to save money  
As a bank client  
I want to make a deposit in my account_

#### Request:
- **Endpoint:** `/accounts/{rib}/credit`
- **Method:** `PUT`
- **Parameters:**
  - `{rib}`: Replace with the account's RIB (Relevé d'Identité Bancaire).
- **Body (form-data):**
  - `amount`: Specify the amount to deposit.
- **Security:** Basic Authentication
  - **Username:** [Client Email]
  - **Password:** [Client Password]

### 3.2 Withdrawal <a name="withdrawal"></a>
**User Story (US-2):**  
_In order to retrieve some or all of my savings  
As a bank client  
I want to make a withdrawal from my account_

#### Request:
- **Endpoint:** `/accounts/{rib}/debit`
- **Method:** `PUT`
- **Parameters:**
  - `{rib}`: Replace with the account's RIB.
- **Body (form-data):**
  - `amount`: Specify the amount to withdraw.
- **Security:** Basic Authentication
  - **Username:** [Client Email]
  - **Password:** [Client Password]

### 3.3 Account Statement <a name="account-statement"></a>
**User Story (US-3):**  
_In order to check my operations  
As a bank client  
I want to see the history (operation, date, amount, balance) of my operations_

#### Request:
- **Endpoint:** `/accounts/operations/{clientId}`
- **Method:** `GET`
- **Parameters:**
  - `{clientId}`: Replace with the client's ID.
- **Security:** Basic Authentication
  - **Username:** [Client Email]
  - **Password:** [Client Password]

### 3.4 Add Account <a name="add-account"></a>
**User Story (US-6):**  
_In order to save a list of clients  
As a bank client  
I want to see the client list with their information based on a multi-criteria search_

#### Request:
- **Endpoint:** `/accounts/save`
- **Method:** `POST`
- **Body (JSON):**
  ```json
  {
    "rib": "123456",
    "balance": 1000.00,
    "client": {
      "id": 1,
      "fullName": "John Doe",
      "email": "john@example.com"
    }
  }
- **Security:** Basic Authentication
  - **Username:** [Client Email]
  - **Password:** [Client Password]

## Conclusion

Congratulations! You've successfully learned how to interact with the KATA BANK application using Postman in the Bank Admin Operations section. Feel free to explore additional features and functionalities based on your needs. If you encounter any issues or have further questions, please refer to the application's documentation or contact the support team.

Happy banking with KATA BANK! 🏦✨
