Feature: Suspend Creator User
  In order to manage CREATOR accounts
  As an ADMIN
  I want to suspend a CREATOR

  Scenario: Anonymous cannot suspend a creator
    Given there is a registered creator with username "creator1", email "creator1@test.com" and password "abcd"
    When I attempt to suspend the creator "creator1" as an anonymous user
    Then The error message is "Unauthorized"
    And The creator "creator1" is still enabled

  Scenario: Creator cannot suspend another creator
    Given there is a registered creator with username "creator1", email "creator1@test.com" and password "abcd"
    And I login as "creator1" with password "abcd"
    When I attempt to suspend the creator "creator1"
    Then The error message is "Forbidden"
    And The creator "creator1" is still enabled

  Scenario: Admin suspends a creator successfully
    Given there is a registered creator with username "creator1", email "creator1@test.com" and password "abcd"
    And there is a registered admin with username "admin1", email "admin1@test.com" and password "1234"
    And I login as "admin1" with password "1234"
    When I suspend the creator "creator1"
    Then The creator "creator1" is disabled

  Scenario: Admin cannot suspend another admin
    Given there is a registered admin with username "admin2", email "admin2@test.com" and password "abcd"
    And there is a registered admin with username "admin1", email "admin1@test.com" and password "1234"
    And I login as "admin1" with password "1234"
    When I attempt to suspend the creator "admin2"
    Then The error message is "Cannot suspend an admin"
    And The creator "admin2" is still enabled