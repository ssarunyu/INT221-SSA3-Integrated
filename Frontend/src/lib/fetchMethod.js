async function getData(url) {
    const token = JSON.parse(localStorage.getItem('token'))
    console.log(token.access_token)
    try {
        const response = await fetch(url, {
            method: "GET",
            headers: {
                'Authorization': `Bearer ${token.access_token}`
            }
        })
        const result = await response.json()
        return result
    } catch (error) {
        console.log(error)
    }
}

async function getDataById(url, id) {
    const finalURL = `${url}/${id}`
    const token = JSON.parse(localStorage.getItem('token'))
    try {
        const response = await fetch(finalURL, {
            method: "GET",
            headers: {
                Authorization: `${token.access_token}`
            }
        })
        const result = response.json()
        return result
    } catch (error) {
        console.log(error)
    }  
}

async function postData(url, task) {
    try {
        const response = await fetch(url, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(task)
        })
        return response
    } catch (error) {
        console.log(error)
    }  
}

async function updateData(url, task, id) {
    const finalURL = `${url}/${id}`
    try {
        const response = await fetch(finalURL, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(task)
        })
        return response
    } catch (error) {
        console.log(error)
    }  
}

async function deleteData(url, id) {
    const finalURL = `${url}/${id}`
    try {
        const response = await fetch(finalURL, {
            method: "DELETE",
        })
        return response
    } catch (error) {
        console.log(error)
    }  
}

async function transferData(url, oldId, newId) {
    const finalURL = `${url}/${oldId}/${newId}`
    try {
        const response = await fetch(finalURL, {
            method: "DELETE",
        })
        return response
    } catch (error) {
        console.log(error)
    }  
}

// User stores
import { useUserStore } from "@/stores/UserStore"
async function postLogin(url, obj) {
const userStore = useUserStore()
    try {
        const response = await fetch(url, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(
                {userName: obj.username, password: obj.password}
            )
        })
        if(response.status === 200) {
            const token = await response.text()
            const tokenArr = token.split('.')
            const tokenPayload = JSON.parse(atob(tokenArr[1]))
            // NOTE: payload replace token should change to collect both
            userStore.setToken(token) 
            userStore.setPayload(tokenPayload)
            // NOTE: Set to localstorage
            localStorage.setItem('token', token)
            localStorage.setItem('payload', JSON.stringify(tokenPayload))
        }
        return response
    } catch (error) {
        console.log(error) 
    }
}

export { getData, getDataById, postData, updateData, deleteData, transferData, postLogin}
