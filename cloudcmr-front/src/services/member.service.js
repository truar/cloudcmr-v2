import axios from 'axios'

export const memberService = {
    fetchAll
}

async function fetchAll() {
    const rawData = await axios.get('/api/members')
    return rawData.data._embedded.members
}
