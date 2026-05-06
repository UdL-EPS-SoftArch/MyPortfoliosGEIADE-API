Feature: Register User
  In order to use the app
  As a user
  I want to register myself and get an account

  Scenario: Register new creator
    Given There is no registered creator with username "creator1"
    And I'm not logged in
    When I register a new creator with username "creator1", email "creator1@sample.app" and password "password123"
    Then The response code is 201
    And It has been created a creator with username "creator1" and email "creator1@sample.app", the password is not returned
    And I can login with username "creator1" and password "password123"

  Scenario: Register existing username
    Given There is a registered creator with username "creator2" and password "existingpass" and email "creator2@sample.app"
    And I'm not logged in
    When I register a new creator with username "creator2", email "creator2@sample.app" and password "newpassword123"
    Then The response code is 409
    And There is still only one creator with username "creator2"

  Scenario: Register creator when already authenticated
    Given I login as "admin1" with password "adminpass"
    When I register a new creator with username "creator2", email "creator2@sample.app" and password "password123"
    Then The response code is 401

  Scenario: Register creator with empty password
    Given I'm not logged in
    When I register a new creator with username "creator4", email "creator4@sample.app" and password ""
    Then The response code is 400
    And The error message is "must not be blank"
    And There is no creator with username "user"

  Scenario: Register creator with empty email
    Given I'm not logged in
    When I register a new creator with username "creator5", email "" and password "password123"
    Then The response code is 400
    And The error message is "must not be blank"
    And There is no creator with username "user"

  Scenario: Register creator with invalid email
    Given I'm not logged in
    When I register a new creator with username "creator6", email "creator6asample.app" and password "password123"
    Then The response code is 400
    And The error message is "must be a well-formed email address"
    And There is no creator with username "user"

  Scenario: Register creator with short password
    Given I'm not logged in
    When I register a new creator with username "creator7", email "creator7@sample.app" and password "pass"
    Then The response code is 400
    And The error message is "length must be between 8 and 256"
    And There is no creator with username "user"

  Scenario: Register creator with existing email
    Given There is a registered creator with username "creator8" and password "password123" and email "creator-common@sample.app"
    And I'm not logged in
    When I register a new creator with username "creator9", email "creator-common@sample.app" and password "password456"
    Then The response code is 409
    And I can login with username "creator8" and password "password123"