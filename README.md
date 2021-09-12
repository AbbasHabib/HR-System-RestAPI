# HR-System-Using-RestAPI

this project is an HR system where the of it is managing employees teams, departments, salaries...and so on. System is able to apply CRUD operation on the DB

#### The project will be fully driven by [JavaSpringBoot](https://www.tutorialspoint.com/spring_boot/spring_boot_introduction.htm) ​Framework, and MySql Database

## Current TodoList
- [ ] Test for addNewDayData doesn't work
- [x] Basic Authorization with every request
- [ ] Add test cases for all attendance table apis
- [x] Rest Api to get absences through the year till specific month
- [x] Rest Api to add day with details: [absence, bonusInSalary] + dayDate
- [x] Rest Api to get employee salary with calculating deduction[**through the year**] and bonuses[**monthly**]
- [x] Api to check deduction at specific month [checking absences through year till specific month]
- [ ] Deal with working years [workingYears increase when he has a new year stored]
- [x] Rest Api to show salary history In a month

## Improving Code _Todo List_

- [ ] Auto generate getters and setters using -> **_(Project Lombok)_**
- [x] Handle Exceptions at client side
- [x] Use Enums to replace constants
- [x] Migration of MySQl DB using **_flyway_**
- [x] Improve Testing using **_DBunit_**
- [x] mapping data from DTO to main entities delete all boilerplate code.
- [x] **Remove the beautiful magic numbers ✨**



## ~~ToDo List~~ 

- [x] Add Team to Employee
- [x] Deal with Employee salary logic
- [x] Rest API to add Employee
- [x] Rest API to get employee Info
- [x] Rest API to modify employee
- [x] Rest API to delete employee
- [x] Dealing with manager deletion logic
- [x] Rest API to get Employee salary info
- [x] Rest API to get All employees under a specific manager
- [x] Rest API to get All employees under a specific manager ***>> recursively <<***
- [x] Rest Api to get All Employees in some team
- [x] Primary key sequence for each table

## Currently working on
- #### improving code stability and adding comments
