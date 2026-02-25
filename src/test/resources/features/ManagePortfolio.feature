Feature: Manage Portfolio
  In order to organize my projects
  As a registered user
  I want to create and manage my portfolios

  Scenario: Create a portfolio
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And I login as "user" with password "password"
    When I create a new portfolio with name "My Portfolio"
    Then The response code is 201
    And The new portfolio is owned by "user"

  Scenario: Get my portfolios
      Given There is a registered user with username "user" and password "password" and email "user@sample.app"
      And I login as "user" with password "password"
      And I create a new portfolio with name "Portfolio1"
      When I request my portfolios
      Then The response code is 200
      And The list contains a portfolio named "Portfolio1"