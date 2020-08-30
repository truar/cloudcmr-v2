import axios from 'axios'

export const memberService = {
    fetchAll,
    create
}

async function fetchAll() {
    const rawData = await axios.get('/api/members')
    return rawData.data?.members
}

async function create(lastName, firstName, gender, email, mobile, birthDate) {
    await axios.post('/api/members/create', {
        lastName, firstName, gender, email, mobile
    })
}
