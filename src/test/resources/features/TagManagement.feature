Feature: Tag Management

    Scenario: Create a new tag
        Given there are no tags in the system
        When I create a tag with name "Futurist" and description "Content related to the future"
        Then Then the tag is created successfully

    Scenario: Create a tag without name
        Given there are no tags in the system
        When I try to create a tag without a name
        Then There should be 0 tags stored

    Scenario: Create a duplicated tag
        Given I create a tag with name "Futurist" and description "Content related to the future"
        When I create a tag with name "Futurist" and description "More content related to the future"
        Then There should be 1 tags stored
        
    Scenario: Delete an existing tag
        Given there is a tag with name "Futurist"
        When I delete the tag with name "Futurist"
        Then the tag should not exist in the system
