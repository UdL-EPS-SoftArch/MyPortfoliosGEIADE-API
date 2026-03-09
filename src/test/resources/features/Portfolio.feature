Feature: Portfolio / Profile access control
  In order to manage profiles as portfolios
  As different types of users
  I want the profile endpoints to respect role restrictions

  Scenario: Creator can create a profile
    Given There is a registered creator with username "creatorProfile" and password "password123" and email "creatorProfile@test.com"
    And I login as "creatorProfile" with password "password123"
    When I create a new profile
    Then The response code is 201

  Scenario: Anonymous cannot create a profile
    Given I'm not logged in
    When I create a new profile
    Then The response code is 401

  Scenario: Admin cannot create a profile
    Given There is a registered admin with username "adminProfile" and password "adminpass" and email "adminProfile@test.com"
    And I login as "adminProfile" with password "adminpass"
    When I create a new profile
    Then The response code is 403

  Scenario: Anyone can list profiles
    Given I'm not logged in
    When I list public profiles
    Then The response code is 200

