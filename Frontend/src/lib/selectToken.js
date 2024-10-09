// Function control to use access token or refresh token
function selectToken() {
    const token = JSON.parse(localStorage.getItem('token'))
    const payload = JSON.parse(localStorage.getItem('payload'))
    // Time in select
    const nowTime = Math.floor(Date.now() / 1000)
    if(token) {
        if(payload.exp < nowTime) {
            if(token.refresh_token) {
                console.log(token.refresh_token)
                return token.refresh_token
            } else {
                console.error('BE error cant find refresh token')
            }
        } else {
            console.log(token.access_token)
            return token.access_token
        }
    } else {
        console.error('401 doesnt have token')
    }
}

export { selectToken }