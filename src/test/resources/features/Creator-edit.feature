Feature: Edit creator profile
In order to manage their information
  As a creator
  I want to edit my profile

Scenario: Creator can edit its profile
Given There is a registered creator with username "creator1" and password "abcd" and email "creator@sample.app"
And creator "creator1" has profile "description"
And I login as "creator1" with password "abcd"
When "creator1" edits its profile
Then The response code is 201


Scenario: Creator1 cannot edit Creator2 profie
Given There is a registered creator with username "creator1" and password "abcd" and email "creator1@sample.app"
Given There is a registered creator with username "creator2" and password "abcd" and email "creator2@sample.app"
And creator "creator1" has profile "description"
And creator "creator2" has profile "description"
When creator "creator2" edits "creator1" profile
Then The response code is 401