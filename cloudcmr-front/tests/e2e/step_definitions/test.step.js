import { When, Then } from 'cypress-cucumber-preprocessor/steps';
import { HomePage } from './pages/home.page';

When(/^I open the Home page$/, () => {
    let homePage = new HomePage();
    homePage.goTo();
});

Then(/^I see "([^"]*)" in the main heading$/, msg => {
    cy.contains('h1', msg)
});
