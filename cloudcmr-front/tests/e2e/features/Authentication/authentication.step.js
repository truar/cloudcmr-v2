import { given, then, when } from 'cypress-cucumber-preprocessor/steps'
import LoginPage from '../../step_definitions/pages/login.page'
import HomePage from '../../step_definitions/pages/home.page'

given(/^I am at the Authentication page$/, () => {
    LoginPage.goTo()
})

when(/^I authenticate as "([^"]*)"$/, email => {
    LoginPage.authenticate(email, 'password')
})

then(/^I am authenticated as "([^"]*)"$/, username => {
    HomePage.assertIsOnHomePage()
    HomePage.assertUserInformationAreDisplayedInHeader(username)
})
