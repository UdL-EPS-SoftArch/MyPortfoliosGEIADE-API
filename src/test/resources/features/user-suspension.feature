Feature: Suspend Creator User
  In order to manage CREATOR accounts
  As an ADMIN
  I want to suspend a CREATOR

  Scenario: Suspend a CREATOR successfully
    Given There is a registered user with username "creator1" and password "password123" and email "creator1@sample.app"
    When I suspend the user "creator1"
    Then The user "creator1" is disabled
    And I cannot login with username "creator1" and password "password123"

  Scenario: Suspend an ADMIN (should fail)
    Given There is a registered admin with username "admin1" and password "adminpass" and email "admin1@sample.app"
    When I attempt to suspend the user "admin1"
    Then I get an error "Only Creators can be suspended"
    And The user "admin1" is still enabled