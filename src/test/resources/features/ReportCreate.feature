Feature: Report management

Scenario: Create a new Report
Given there are no Reports in the system
And There is a registered user with username "eric" and password "passworderic" and email "eric@gmail.com"
And I login as "eric" with password "passworderic"
And There is a Content with name "Test Content" and description "Some description"
When I create a Report with contentId 1 and reason "Inappropriate content"
Then Report existsById should return true