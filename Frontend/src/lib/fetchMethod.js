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

export { getData, getDataById }