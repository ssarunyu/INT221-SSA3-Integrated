import router from '@/router';
import { jwtDecode } from "jwt-decode";

async function getData(url) {
    const token = JSON.parse(localStorage.getItem('token'))
    if(token) {
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
            if(response.status === 403) {
                window.alert('Access denied, you do not have permission to view this page.')
                router.push({ name: 'Login' })
            }
            const result = await response.json()
            return result
        } catch (error) {
            console.log(error)
        }
    } else {
        router.push({ name: 'Login' })
    }
}

async function postBoard(url, newBoard) {
    const token = JSON.parse(localStorage.getItem('token'))
    if(token) {
        try {
            const response = await fetch(url, {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${token.access_token}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(newBoard)
            })
            if(response.status === 401) {
                localStorage.removeItem('token')
                localStorage.removeItem('payload')
                router.push({ name: 'Login' })
            }
            const result = await response.json();
            return { status: response.status, data: result };
        } catch (error) {
            console.error('Error in postBoard:', error);
            throw error;
        }
    } else {
        router.push({ name: 'Login' })
    }
}

async function postBoardId(url,boardId,id){
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

async function getBoardId(url, id) {
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

async function deleteBoardId(url, id) {
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

async function updateBoardId(url, task, id) {
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
    const token = JSON.parse(localStorage.getItem('token'))
    try {
        const response = await fetch(url, {
            method: "POST",
            headers: {
                'Authorization': `Bearer ${token.access_token}`,
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
    const token = JSON.parse(localStorage.getItem('token'))
    const finalURL = `${url}/${id}`
    if(token) {
        try {
            const response = await fetch(finalURL, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json",
                    'Authorization': `Bearer ${token.access_token}`
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
    } else {
        router.push({ name: 'Login' })
    }
}

async function deleteData(url, id) {
    const token = JSON.parse(localStorage.getItem('token'))
    const finalURL = `${url}/${id}`
    try {
        const response = await fetch(finalURL, {
            method: "DELETE",
            headers: {
                'Authorization': `Bearer ${token.access_token}`
            }
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
    const token = JSON.parse(localStorage.getItem('token'))
    const finalURL = `${url}/${oldId}/${newId}`
    if(token) {
        try {
            const response = await fetch(finalURL, {
                method: "DELETE",
                headers: {
                    'Authorization': `Bearer ${token.access_token}`
                }
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
    } else {
        router.push({ name: 'Login' })
    }
}

async function patchVisi(url, boardId, type) {
    const token = JSON.parse(localStorage.getItem('token'))
    const finalURL = `${url}/v3/boards/${boardId}`
    if(token) {
        try {
            const response = await fetch(finalURL, {
                method: "PATCH",
                headers: {
                    'Authorization': `Bearer ${token.access_token}`,
                    "Content-Type": "application/json"
                },
                // Type is plain text 'public' and 'private' control in child element
                body: JSON.stringify({ visibility: type })
            })
            if(response.status === 401) {
                localStorage.removeItem('token')
                localStorage.removeItem('payload')
                router.push({ name: 'Login' })
            }
            console.log(await response.json())
            return response
        } catch (error) {
            console.log(error)
        }  
    } else {
        router.push({ name: 'Login' })
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
                { userName: obj.username, password: obj.password }
            )
        })
        if(response.status === 200) {
            const token = await response.json()
            // NOTE: Set to localstorage
            localStorage.setItem('token', JSON.stringify(token))
            localStorage.setItem('payload', JSON.stringify(jwtDecode(token.access_token)))
        }
        return response
    } catch (error) {
        console.log(error) 
    }
}

export { getData, getDataById, postData, updateData, deleteData, transferData, postLogin, postBoard ,postBoardId ,getBoardId ,deleteBoardId, patchVisi }
