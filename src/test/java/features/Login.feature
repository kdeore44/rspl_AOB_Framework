
@Login
Feature: Login Functionality
  

  @ValidLogin
  Scenario: Successful login with valid credentials
    Given I open the application in "chrome" browser
    When I click on Microsoft Sign In button
    And I enter username "Username"
    And I click on Next button
    And I enter password "Password"
    And I click on Sign In button
    And I select the checkbox and click Yes
    Then I should be logged in successfully


    Examples: 
      | Username                     |     Password  |
      | sneh.solanki@rishabhsoft.com |     Ss@082025 |
      
