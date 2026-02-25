Feature: Tag management

Scenario: Create a tag without name
Given there are no tags in the system
When I try to create a tag without a name
Then the tag should not be stored in the system