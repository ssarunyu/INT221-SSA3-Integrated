async function getData(url) {
    try {
        const response = await fetch(url, {
            method: "GET",
            redirect: "follow"
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
            redirect: "follow"
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
            redirect: "follow",
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
            redirect: "follow",
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
            redirect: "follow"
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
            redirect: "follow"
        })
        return response
    } catch (error) {
        console.log(error)
    }  
}

export { getData, getDataById, postData, updateData, deleteData, transferData}