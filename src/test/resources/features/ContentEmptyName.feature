Feature: Content empty name validation

  Scenario: Create a Content with an empty name should fail
    Given There is a registered user with username "maria" and password "passwordmaria" and email "maria@gmail.com"
    And I login as "maria" with password "passwordmaria"
    When I create an Empty Name Content with name "" and description "Some description"
    Then The Content creation should fail with status 400
