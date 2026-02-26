Feature: Register Creator
  In order to use the app
  As a user
  I want to register myself and get an account

  Scenario: Register new creator
    Given There is no registered creator with username "creator1"
    And I'm not logged in
    When I register a new creator with username "creator1", email "creator1@sample.app" and password "password123"
    Then The response code is 201
    And It has been created a creator with username "creator1" and email "creator1@sample.app", the password is not returned

  Scenario: Register existing username
    Given There is a registered creator with username "creator1" and password "password123" and email "creator1@sample.app"
    And I'm not logged in
    When I register a new creator with username "creator1", email "creator1@sample.app" and password "newpassword123"
    Then The response code is 409
    And It has not been created a creator with username "creator1"

  Scenario: Register creator when already authenticated
    Given I login as "admin1" with password "adminpass"
    When I register a new creator with username "creator2", email "creator2@sample.app" and password "password123"
    Then The response code is 403
    And It has not been created a creator with username "creator2"