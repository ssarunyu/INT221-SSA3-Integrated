function styleStatus(name) {
    if(name === 'No Status') {
        return 'bg-gray-300'
    }
    if(name === 'To Do') {
        return 'bg-red-300'
    }
    if(name === 'Doing') {
        return 'bg-amber-300'
    }
    if(name === 'Done') {
        return 'bg-green-300'
    }
}

export { styleStatus }