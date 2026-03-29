Feature: Content management

Scenario: Delete an existing Content
Given There is a Content with name "Test Content" and description "Some description"
And There is a registered user with username "maria" and password "passwordmaria" and email "maria@gmail.com"
And I login as "maria" with password "passwordmaria"
And I delete a Content with name "Test Content"
Then Content existsById should return false