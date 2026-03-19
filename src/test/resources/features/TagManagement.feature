Feature: Tag management

  Scenario: Create a new tag
    Given there are no tags in the system
    When I create a tag with name "Futurist" and description "Content related to the future"
    Then Then the tag is created successfully

    Scenario: Delete an existing tag
        Given there is a tag with name "Futurist"
        When I delete the tag with name "Futurist"
        Then the tag should not exist in the system

    Scenario: Create a duplicated tag
        Given there is already a tag with name "Futurist"
        When I create a tag with name "Futurist" and description "Content related to the future"
        Then the tag should not be stored in the system

    Scenario: Create a tag without name
        Given there are no tags in the system
        When I try to create a tag without a name
        Then the tag should not be stored in the system

    Scenario: Creator assigns tag to their own content
        Given There is a registered user with username "creator1" and password "pass" and email "c1@test.com"
        And I can login with username "creator1" and password "pass"
        And there is a tag with name "Futurist"
        And there is a content "Post1" created by "creator1"
        When I assign the tag "Futurist" to content "Post1"
        Then The response status is 204
        And content "Post1" should contain tag "Futurist"

    Scenario: Creator cannot assign tag to another user's content
        Given There is a registered user with username "creator1" and password "pass" and email "c1@test.com"
        And There is a registered user with username "creator2" and password "pass" and email "c2@test.com"
        And I can login with username "creator1" and password "pass"
        And there is a tag with name "Futurist"
        And there is a content "Post1" created by "creator2"
        When I assign the tag "Futurist" to content "Post1"
        Then The response status is 403
