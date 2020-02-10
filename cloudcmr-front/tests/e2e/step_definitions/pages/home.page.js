export default class HomePage {
    static goTo () {
        cy.visit('/')
    }

    static assertIsOnHomePage () {
        cy.location('pathname').should('eq', '/')
    }

    static assertUserInformationAreDisplayedInHeader (username) {
        cy.get('.home').contains(username)
    }
}
