Feature: Login Action
 
Scenario: Successful Login with Valid Credentials
Given User is on Home Page
And User enters "Braffa" and "kelly1233"
Then Full Catalogue Page is rendered
 
Scenario: Successful LogOut
When User LogOut from the Application
Then Home Page is rendered