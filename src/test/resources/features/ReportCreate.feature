Feature: Report management

Scenario: Create a new Report
Given there are no Reports in the system
When I create a Report with reportId 10, contentId 5 and reason "Inappropriate content"
Then Report existsById should return true