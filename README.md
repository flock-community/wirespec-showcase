# Wirespec showcase

The goal of this showcase project is to show the benefits of using wirespec, in a semi-realistic situation.

We take 4 services, that interact with one another, and model their interaction with wirespec:


![Wirespec showcase.png](Wirespec%20showcase.png)


- A small frontend application, Move-Money FE that allows a user to visualise transactions, and do transactions to others
- A backend application, Move-Money BE to handle the incoming requests from the frontend, but doesn't do the actual transaction
- A backend application Payments BE, that is responsible for transactions
- An audit service that listens to Kafka events on everything that happened


WIP
