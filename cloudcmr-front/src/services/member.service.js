import axios from 'axios'

export const memberService = {
    fetchAll,
    create,
    fetchMember,
    changeAddress
}

async function fetchAll(page, pageSize, sortBy, isDesc) {
    let url = `/api/members?page=${page}&pageSize=${pageSize}`
    if (sortBy !== undefined) {
        url += `&sortBy=${sortBy}`
    }
    if (isDesc !== undefined) {
        const sortOrder = isDesc ? 'DESC' : 'ASC'
        url += `&sortOrder=${sortOrder}`
    }
    const rawData = await axios.get(url)
    return rawData.data
}

async function create(lastName, firstName, gender, email, mobile, birthDate) {
    await axios.post('/api/members/create', {
        lastName, firstName, gender, email, mobile, birthDate
    })
}

async function fetchMember(memberId) {
    const rawData = await axios.get(`/api/members/${memberId}`)
    return rawData.data
}

async function changeAddress(memberId, address) {
    await axios.post(`/api/members/${memberId}/changeAddress`, {
        ...address
    })
}
