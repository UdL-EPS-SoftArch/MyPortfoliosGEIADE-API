Feature: Tag management

Scenario: Create a duplicated tag
Given there is already a tag with name "Futurist"
    When I create a tag with name "Futurist" and description "Content related to the future"
Then the tag should not be stored in the system