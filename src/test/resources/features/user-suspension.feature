Feature: Suspend Creator User
  In order to manage CREATOR accounts
  As an ADMIN
  I want to suspend a CREATOR

  Scenario: Anonymous cannot suspend a creator
    Given There is no registered creator with username "creator1"
    When I register a new creator with username "creator1", email "creator1@test.com" and password "abcd"
    When I attempt to suspend the creator "creator1" as an anonymous user
    Then The error message is "Unauthorized"
    And The creator "creator1" is still enabled

  Scenario: Creator cannot suspend another creator
    Given There is a registered creator with username "creator1" and password "abcd" and email "creator1@test.com"
    And There is a registered creator with username "creator2" and password "abcd" and email "creator2@test.com"
    And I login as "creator2" with password "abcd"
    When I attempt to suspend the creator "creator1"
    Then The response code is 403
    And The creator "creator1" is still enabled

  Scenario: Admin suspends a creator successfully
    Given There is a registered creator with username "creator3" and password "abcd" and email "creator3@test.com"
    And There is a registered admin with username "admin1" and password "1234" and email "admin1@test.com"
    And I login as "admin1" with password "1234"
    When I suspend the creator "creator3"
    Then The creator "creator3" is disabled

 