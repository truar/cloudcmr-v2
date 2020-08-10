import * as firebase from 'firebase'
import axios from 'axios'

export const userService = {
    login,
    logout,
    isAuthenticated,
    getToken,
    getPrincipal
}

async function login(username, password) {
    await firebase
        .auth()
        .signInWithEmailAndPassword(username, password)
    let user = firebase.auth().currentUser
    let token = await user.getIdToken()

    axios.defaults.headers.common['Authorization'] = token

    localStorage.setItem('user', JSON.stringify(user))
    localStorage.setItem('token', JSON.stringify(token))
    return { user, token }
}

function logout() {
    // remove user from local storage to log user out
    localStorage.removeItem('user')
    localStorage.removeItem('token')
}

function isAuthenticated() {
    return localStorage.getItem('user') !== null
}

function getToken() {
    return JSON.parse(localStorage.getItem('token'))
}

function getPrincipal() {
    return JSON.parse(localStorage.getItem('user'))
}
