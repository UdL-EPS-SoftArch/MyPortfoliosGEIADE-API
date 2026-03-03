Feature: Manage Project
  In order to manage projects
  As a user
  I want to be able to create, retrieve, update and delete projects

  # ---- CREATE ----

  Scenario: Create a project successfully
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And I login as "user" with password "password"
    And I prepare a project with name "Java Project" and description "Uses Java" and visibility "PUBLIC"
    When I send the create project request
    Then The response code is 201
    And The project name is "Java Project"
    And The project creation date should be set

  Scenario: Create a project without authentication
    Given I'm not logged in
    And I prepare a project with name "Unauth Project" and description "No auth" and visibility "PUBLIC"
    When I send the create project request
    Then The response code is 401

  Scenario: Create a project with empty name
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And I login as "user" with password "password"
    And I prepare a project with name "" and description "Some description" and visibility "PUBLIC"
    When I send the create project request
    Then The response code is 400

  # ---- READ ----

  Scenario: Retrieve an existing project
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And I login as "user" with password "password"
    And There is a project with name "Existing Project" and description "desc" and visibility "PUBLIC"
    When I retrieve the project named "Existing Project"
    Then The response code is 200
    And The project name is "Existing Project"

  Scenario: Retrieve a non-existing project
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And I login as "user" with password "password"
    When I retrieve the project with id 99999
    Then The response code is 404

  # ---- UPDATE ----

  Scenario: Update a project name
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And I login as "user" with password "password"
    And There is a project with name "Old Name" and description "desc" and visibility "PUBLIC"
    When I update the project named "Old Name" with new name "New Name"
    Then The response code is 200
    And The project name is "New Name"

  Scenario: Update a project without authentication
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And There is a project with name "Some Project" and description "desc" and visibility "PUBLIC"
    And I'm not logged in
    When I update the project named "Some Project" with new name "Hacked"
    Then The response code is 401

  # ---- DELETE ----

  Scenario: Delete a project successfully
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And I login as "user" with password "password"
    And There is a project with name "To Delete" and description "desc" and visibility "PUBLIC"
    When I delete the project named "To Delete"
    Then The response code is 204

  Scenario: Delete a project without authentication
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And There is a project with name "Protected Project" and description "desc" and visibility "PUBLIC"
    And I'm not logged in
    When I delete the project named "Protected Project"
    Then The response code is 401