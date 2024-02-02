# KATA BANK Application Documentation

Welcome to the KATA BANK application! This documentation provides a comprehensive guide on how to use the application, covering various use cases and features. To interact with the application, we have prepared a set of Postman requests that you can import for testing.

## Table of Contents
1. [Getting Started](#getting-started)
2. [Client Operations](#client-operations)
   - [Deposit](#deposit)
   - [Withdrawal](#withdrawal)
   - [Account Statement](#account-statement)
   - [Add Account](#add-account)
3. [Bank Admin Operations](#bank-admin-operations)
   - [Client List](#client-list)
   - [Multi-Criteria Search](#multi-criteria-search)
   - [Batch Processing](#batch-processing)

## 1. Getting Started <a name="getting-started"></a>

### Importing Postman Requests
1. Download and install [Postman](https://www.postman.com/).
2. Import the provided Postman collection: [KATA BANK Postman Collection](path/to/your/KATA_BANK_Postman_Collection.json).

## 2. Client Operations <a name="client-operations"></a>

### 2.1 Deposit <a name="deposit"></a>
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

### 2.2 Withdrawal <a name="withdrawal"></a>
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

### 2.3 Account Statement <a name="account-statement"></a>
**User Story (US-3):**  
_In order to check my operations  
As a bank client  
I want to see the history (operation, date, amount, balance) of my operations_

#### Request:
- **Endpoint:** `/accounts/operations/{clientId}`
- **Method:** `GET`
- **Parameters:**
  - `{clientId}`: Replace with the client's ID.

### 2.4 Add Account <a name="add-account"></a>
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
## 3. Bank Admin Operations <a name="bank-admin-operations"></a>

### 3.1 Client List <a name="client-list"></a>
**User Story (US-4):**  
_In order to check the list of clients  
As a bank Admin  
I want to see the client list with their information_

#### Request:
- **Endpoint:** `/clients`
- **Method:** `GET`

### 3.2 Multi-Criteria Search <a name="multi-criteria-search"></a>
**User Story (US-5):**  
_In order to search the list of clients  
As a bank Admin  
I want to see the client list with their information based on a multi-criteria search_

#### Request:
- **Endpoint:** `/clients/search`
- **Method:** `GET`
- **Parameters (query parameters):**
  - `fullName`: Specify the full name for the search.
  - `email`: Specify the email for the search.

### 3.3 Batch Processing <a name="batch-processing"></a>
**User Story (Batch Processing):**  
_In order to efficiently process client data  
As a bank Admin  
I want to execute a batch job to handle client information in bulk_

#### Request:
- **Endpoint:** `/clients/batch/start`
- **Method:** `POST`
- **Parameters (form-data):**
  - `file`: Upload a CSV file containing client data.

## Conclusion

Congratulations! You've successfully learned how to interact with the KATA BANK application using Postman in the Bank Admin Operations section. Feel free to explore additional features and functionalities based on your needs. If you encounter any issues or have further questions, please refer to the application's documentation or contact the support team.

Happy banking with KATA BANK! 🏦✨
