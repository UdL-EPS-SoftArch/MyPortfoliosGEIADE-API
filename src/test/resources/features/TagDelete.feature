Feature: Tag management

    Scenario: Delete an existing tag
    Given there is a tag with name "Futurist"
    When I delete the tag with name "Futurist"
    Then the tag should not exist in the system