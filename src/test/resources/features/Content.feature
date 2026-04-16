Feature: Content management


Scenario: Create a new Content
Given there are no Contents in the system
And There is a registered user with username "maria" and password "passwordmaria" and email "maria@gmail.com"
And I login as "maria" with password "passwordmaria"
When I create a new content with name "Fulp Piction" and description "Directed by Tuentin Qarantino"
Then Content existsById should return true


Scenario: Delete an existing Content
Given There is a Content with name "Test Content" and description "Some description"
And There is a registered user with username "maria" and password "passwordmaria" and email "maria@gmail.com"
And I login as "maria" with password "passwordmaria"
And I delete a Content with name "Test Content"
Then Content existsById should return false


Scenario: Create a Content that already exists
Given There is a Content with name "Test Content" and description "Some description"
And There is a registered user with username "neus" and password "passwordneus" and email "neus@gmail.com"
And I login as "neus" with password "passwordneus"
When I create a duplicate Content with name "Test Content" and description "Some description"
Then The Content creation should fail with status 409


Scenario: Create a Content with an empty name should fail
Given There is a registered user with username "maria" and password "passwordmaria" and email "maria@gmail.com"
And I login as "maria" with password "passwordmaria"
When I create an Empty Name Content with name "" and description "Some description"
Then The Content creation should fail with status 400

