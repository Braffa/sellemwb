Feature: Login Action

Scenario: At Start of test
Then set up the data
 
Scenario: Successful Login with Valid Credentials
Given User is on Home Page
And User enters "Braffa" and "amanda33"
Then Full Catalogue Page is rendered
 
Scenario: Successful LogOut
When User LogOut from the Application
Then Home Page is rendered

Scenario: Quit Test
When The tests have finished
Then shutdown