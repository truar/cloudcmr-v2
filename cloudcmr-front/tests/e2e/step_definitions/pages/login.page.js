export default class LoginPage {
    static goTo() {
        cy.visit('/login')
    }

    static authenticate(email, password) {
        fillInput('email', email)
        fillInput('password', password)

        clickOnId('login-form')
    }
}

function fillInput(id, value) {
    cy.get(`input[id="${id}"]`).type(value, { force: true })
}

function clickOnId(id) {
    cy.get(`#${id}`).submit()
}
