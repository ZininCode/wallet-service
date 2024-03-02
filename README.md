# Wallet microservice

## How to use the service:
Examples of requests to the service are created with Postman and could be found here:

https://documenter.getpostman.com/view/2855941/2sA2rCTMAw

## Description:

A monetary account holds the current balance for a player. The balance can be modified by registering transactions on the account, either debit transactions (removing funds) or credit transactions (adding funds). REST API implementation that fulfils the requirements detailed below:

## Functional requirements:

* Current balance per player

* Debit /Withdrawal per player A debit transaction will only succeed if there are sufficient funds on the account (balance - debit amount >= 0).

* The caller will supply a transaction id that must be
unique for all transactions. If the transaction id is not unique, the operation
must fail.

* Credit per player. The caller will supply a transaction id that must be unique for all transactions. If the transaction id is not unique, the operation must fail.

* Transaction history per player

### Custom exceptions
* If transaction id is not unique

* If Player's Id is not found in DB

* If balance is less than the debit amount in case of a debit transaction

## Non-functional requirements:

* The API uses a h2 database.

* The solution persist data across restarts.

## Also focus at:

* Design

* Clean code

* Testability