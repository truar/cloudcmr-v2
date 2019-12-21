import { When, Then } from 'cypress-cucumber-preprocessor/steps';
import { goToHomePage } from './pages/home.page';

When(/^I open the Home page$/, () => {
    goToHomePage()
});

Then(/^I see "([^"]*)" in the main heading$/, msg => {
    cy.contains('h1', msg)
});
