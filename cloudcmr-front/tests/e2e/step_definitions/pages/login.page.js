export default class LoginPage {

    static goTo() {
        cy.visit("/login");
    }

    static authenticate(username) {
        fillInput("username", username);
        fillInput("password", username);

        clickOnId('login-form');
    }
}

function fillInput(id, value) {
    cy.get(`input[id="${id}"]`).type(value, { force: true });
}

function clickOnId(id) {
    cy.get(`#${id}`).click({ force: true });
}