Feature: Creat a project
    In order to manage projects
    As a user
    I want to be able to create and post projects


  Scenario: Creat a project correctly
    Given that I want to create a project with the name "Java Project" and the description "Project that uses Java"
    And I assign it the "PUBLIC" visibility
    When I send the create request
    Then The project should be saved correctly
    And the data of creation "created" should be correct
