Feature: Content management

Scenario: Create a new Content
Given there are no Contents in the system
When I create a new content with name "Fulp Piction" and description "Directed by Tuentin Qarantino"
Then Content existsById should return true