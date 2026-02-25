Feature: Tag management

  Scenario: Create a new tag
  Given there are no tags in the system
  When I create a tag with name "Futurist" and description "Content related to the future"
  Then existsById should return true