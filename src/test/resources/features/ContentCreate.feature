Feature: Content management

Scenario: Create a new Content
Given there are no Contents in the system
And There is a registered user with username "maria" and password "passwordmaria" and email "maria@gmail.com"
And I login as "maria" with password "passwordmaria"
When I create a new content with name "Fulp Piction" and description "Directed by Tuentin Qarantino"
Then Content existsById should return true