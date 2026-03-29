Feature: Content duplicate management

  Scenario: Create a Content that already exists
    Given There is a Content with name "Test Content" and description "Some description"
    And There is a registered user with username "neus" and password "passwordneus" and email "neus@gmail.com"
    And I login as "neus" with password "passwordneus"
    When I create a duplicate Content with name "Test Content" and description "Some description"
    Then The Content creation should fail with status 409
