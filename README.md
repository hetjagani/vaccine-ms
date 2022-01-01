# Vaccine Management System

This application is a Vaccine Management System which provides the functionalities like making an appointment for taking a vaccine, administrating the vaccines and clinics, and tracking the previous and future vaccines history of the patient. This system allows user login/signup with google and also with email and password. A user can also be an admin if a user logins with sjsu.edu email. Also email verification via link is done. So a user who signs up has to verify email with a link sent to his/her email.

### Functionalities of Patient: 
* See the past appointments to the clinic showing which vaccines have been taken in the past.
* Schedule an appointment for the future required vaccines.
* While scheduling an appointment, Patient cannot book an appointment date which is later than 1 year from the present data.

### Functionalities of Admin:
* Create disease/vaccine/clinic.
* Update disease/vaccine/clinic.
* Delete disease/vaccine/clinic.

## System Architecture
![arch](https://user-images.githubusercontent.com/27214644/147843246-15c6e88b-7ec8-4671-a1d8-62e7c0cb377d.png)

## Build and Run Instructions
* Need to install docker
* To run application stack `docker-compose up`
