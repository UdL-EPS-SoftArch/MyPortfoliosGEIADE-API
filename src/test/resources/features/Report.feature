Feature: Report management


Scenario: Create a new Report
Given there are no Reports in the system
And there are no Contents in the system
And There is a registered user with username "eric" and password "passworderic" and email "eric@gmail.com"
And I login as "eric" with password "passworderic"
And There is a Content with name "Test Content" and description "Some description"
When I create a Report with the last created content and reason "Inappropriate content"
Then Report existsById should return true


Scenario: Delete an existing Report
Given there are no Reports in the system
And there are no Contents in the system
And There is a registered user with username "eric" and password "passworderic" and email "eric@gmail.com"
And I login as "eric" with password "passworderic"
And There is a Content with name "Test Content" and description "Some description"
And I create a Report with the last created content and reason "Spam"
When I delete the last created Report
Then Report existsById should return false


# -------- WE'LL WAIT UNTIL USER IS CORRECTLY RELATED TO A REPORT ---------
# Scenario: Create a duplicate Report should fail
# Given there are no Reports in the system
# And there are no Contents in the system
# And There is a registered user with username "eric" and password "passworderic" and email "eric@gmail.com"
# And I login as "eric" with password "passworderic"
# And There is a Content with name "Test Content" and description "Some description"
# And I create a Report with the last created content and reason "Spam"
# When I try to create a duplicate Report with the last created content and reason "Spam"
# Then The Report creation should fail with status 409


Scenario: Create a Report with an empty reason should fail
Given there are no Reports in the system
And there are no Contents in the system
And There is a registered user with username "eric" and password "passworderic" and email "eric@gmail.com"
And I login as "eric" with password "passworderic"
And There is a Content with name "Test Content" and description "Some description"
When I try to create a Report with the last created content and reason ""
Then The Report creation should fail with status 400
