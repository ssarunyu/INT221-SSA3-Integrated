async function getData(url) {
    try {
        const response = await fetch(url, {
            method: "GET",
        })
        const result = response.json()
        return result
    } catch (error) {
        console.log(error)
    }
}

async function getDataById(url, id) {
    const finalURL = `${url}/${id}`
    try {
        const response = await fetch(finalURL, {
            method: "GET",
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
                {username: obj.username, password: obj.password}
            )
        })
        if(response.status === 200) {
            const token = await response.text()
            const tokenArr = token.split('.')
            const tokenPayload = JSON.parse(atob(tokenArr[1]))
            userStore.setToken(token) 
            userStore.setPayload(tokenPayload)
            // log user
            console.log(userStore.getUser())
        }
      return response
    } catch (error) {
        console.log(error) 
    }
}

export { getData, getDataById, postData, updateData, deleteData, transferData, postLogin}
