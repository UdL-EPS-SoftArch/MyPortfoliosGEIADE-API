Feature: add admin

Scenario: Admin creates another admin
  Given There is a registered admin with username "admin1" and password "1234" and email "admin1@test.com"
  And I login as "admin1" with password "1234"
  And There is no registered user with username "admin2"
  When I register a new admin with username "admin2", email "admin2@test.com" and password "abcd"
  Then The response code is 201


Scenario: Creator cannot create an admin
    Given There is a registered user with username "creator1" and password "1234" and email "creator@test.com"
    And I login as "creator1" with password "1234"
    And There is no registered user with username "admin3"
    When I register a new admin with username "admin3", email "admin3@test.com" and password "abcd"
    Then The response code is 403