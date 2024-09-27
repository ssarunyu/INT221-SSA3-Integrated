import router from '@/router';
import { jwtDecode } from "jwt-decode";

async function getData(url) {
    const token = JSON.parse(localStorage.getItem('token'))
    try {
        const response = await fetch(url, {
            method: "GET",
            headers: {
                'Authorization': `Bearer ${token.access_token}`
            }
        })
        if(response.status === 401) {
            localStorage.removeItem('token')
            localStorage.removeItem('payload')
            router.push({ name: 'Login' })
        }
        const result = await response.json()
        return result
    } catch (error) {
        console.log(error)
    }
}

async function postBoard(url, newBoard) {
    const token = JSON.parse(localStorage.getItem('token'))
    console.log(token.access_token)
    try {
        const response = await fetch(url, {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token.access_token}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(newBoard)
        })
        const result = await response.json();
        return { status: response.status, data: result };
    } catch (error) {
        console.error('Error in postBoard:', error);
        throw error;
    }
}

async function getDataById(url, id) {
    const finalURL = `${url}/${id}`
    const token = JSON.parse(localStorage.getItem('token'))
    try {
        const response = await fetch(finalURL, {
            method: "GET",
            headers: {
                'Authorization': `Bearer ${token.access_token}`
            }
        })
        if(response.status === 401) {
            localStorage.removeItem('token')
            localStorage.removeItem('payload')
            router.push({ name: 'Login' })
        }
        const result = await response.json()
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
        if(response.status === 401) {
            localStorage.removeItem('token')
            localStorage.removeItem('payload')
            router.push({ name: 'Login' })
        }
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
        if(response.status === 401) {
            localStorage.removeItem('token')
            localStorage.removeItem('payload')
            router.push({ name: 'Login' })
        }
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
        if(response.status === 401) {
            localStorage.removeItem('token')
            localStorage.removeItem('payload')
            router.push({ name: 'Login' })
        }
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
        if(response.status === 401) {
            localStorage.removeItem('token')
            localStorage.removeItem('payload')
            router.push({ name: 'Login' })
        }
        return response
    } catch (error) {
        console.log(error)
    }  
}

// NOTE: Change to use user stores
import { useUserStore } from "@/stores/UserStore"
async function postLogin(url, obj) {
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
            const token = await response.json()
            console.log(token)
            // NOTE: Set to localstorage
            localStorage.setItem('token', JSON.stringify(token))
            localStorage.setItem('payload', JSON.stringify(jwtDecode(token.access_token)))
        }
        return response
    } catch (error) {
        console.log(error) 
    }
}

export { getData, getDataById, postData, updateData, deleteData, transferData, postLogin, postBoard}
