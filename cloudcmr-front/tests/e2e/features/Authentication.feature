Feature: A user is able to login 

    Scenario: As a user, I want to be able to login
        Given I am at the Authentication page
        When I authenticate as "user@user.com"
        Then I am authenticated as "user@user.com"
