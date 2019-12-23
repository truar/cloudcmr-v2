import { when, then } from 'cypress-cucumber-preprocessor/steps';
import HomePage from './pages/home.page';

when(/^I open the Home page$/, () => {
    HomePage.goTo();
});

then(/^I see "([^"]*)" in the main heading$/, msg => {
    cy.contains('h1', msg)
});
