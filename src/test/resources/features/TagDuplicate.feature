Feature: Tag management

Scenario: Create a duplicated tag
Given there is already a tag with name "Futurist"
When I try to create another tag with name "Futurist" and description "Duplicate"
Then the tag should not be stored in the system