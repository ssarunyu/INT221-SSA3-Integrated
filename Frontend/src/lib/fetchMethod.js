async function getData(url) {
    try {
        const res = await fetch(url, {
            method: "GET",
            redirect: "follow"
        })
        const result = res.json()
        return result
    } catch (error) {
        console.log(error)
    }
}

async function getDataById(url, id) {
    const finalURL = `${url}/${id}`
    try {
        const res = await fetch(finalURL, {
            method: "GET",
            redirect: "follow"
        })
        const result = res.json()
        return result
    } catch (error) {
        console.log(error)
    }  
}

async function postData(url) {
    try {
        const res = await fetch(url, {
            method: "POST",
            redirect: "follow"
        })
        console.log(res)
    } catch (error) {
        console.log(error)
    }  
}

async function deleteData(url, id) {
    const finalURL = `${url}/${id}`
    try {
        const res = await fetch(finalURL, {
            method: "DELETE",
            redirect: "follow"
        })
        console.log(res)
    } catch (error) {
        console.log(error)
    }  
}

export { getData, getDataById, postData, deleteData }