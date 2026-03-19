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

  Scenario: Cannot access another user's portfolio
    Given There is a registered user with username "user1" and password "password" and email "user1@sample.app"
    And There is a registered user with username "user2" and password "password" and email "user2@sample.app"
    And I login as "user1" with password "password"
    And I create a new portfolio with name "Private Portfolio"
    And I logout
    And I login as "user2" with password "password"
    When I request portfolios of "user1"
    Then The response code is 403

  Scenario: Update my portfolio
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And I login as "user" with password "password"
    And I create a new portfolio with name "Old Name"
    When I update the portfolio name to "New Name"
    Then The response code is 200
    And The portfolio name is "New Name"

  Scenario: Delete my portfolio
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And I login as "user" with password "password"
    And I create a new portfolio with name "PortfolioToDelete"
    When I delete the portfolio "PortfolioToDelete"
    Then The response code is 204
    And The portfolio "PortfolioToDelete" does not exist

  Scenario: Cannot delete another user's portfolio
    Given There is a registered user with username "user1" and password "password" and email "user1@sample.app"
    And There is a registered user with username "user2" and password "password" and email "user2@sample.app"
    And I login as "user1" with password "password"
    And I create a new portfolio with name "User1Portfolio"
    And I logout
    And I login as "user2" with password "password"
    When I try to delete portfolio "User1Portfolio"
    Then The response code is 403

  Scenario: Create portfolio with empty name
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And I login as "user" with password "password"
    When I create a new portfolio with name ""
    Then The response code is 400

  Scenario: Cannot create portfolio without login
    Given I'm not logged in
    When I create a new portfolio with name "My Portfolio"
    Then The response code is 401

  Scenario: Cannot update another user's portfolio
    Given There is a registered user with username "user1" and password "password" and email "user1@sample.app"
    And There is a registered user with username "user2" and password "password" and email "user2@sample.app"
    And I login as "user1" with password "password"
    And I create a new portfolio with name "User1Portfolio"
    And I logout
    And I login as "user2" with password "password"
    When I try to update the portfolio name to "HackedPortfolio"
    Then The response code is 403

  Scenario: Cannot list portfolios without login
    Given I'm not logged in
    When I request my portfolios
    Then The response code is 401

  Scenario: Anonymous can access public portfolios
    Given There is a registered user with username "user1" and password "password" and email "user1@sample.app"
    And I login as "user1" with password "password"
    And I create a new portfolio with name "Public Portfolio" and visibility "PUBLIC"
    And I logout
    When I request public portfolios
    Then The response code is 200
    And The list contains a portfolio named "Public Portfolio"

  Scenario: User can access another user's public portfolio
    Given There is a registered user with username "user1" and password "password" and email "user1@sample.app"
    And There is a registered user with username "user2" and password "password" and email "user2@sample.app"
    And I login as "user1" with password "password"
    And I create a new portfolio with name "Public Portfolio" and visibility "PUBLIC"
    And I logout
    And I login as "user2" with password "password"
    When I request portfolios of "user1"
    Then The response code is 200

  Scenario: Portfolio is private by default
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And I login as "user" with password "password"
    When I create a new portfolio with name "My Portfolio"
    Then The portfolio visibility is "PRIVATE"
