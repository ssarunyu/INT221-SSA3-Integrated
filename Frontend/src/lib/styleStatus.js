function styleStatus(name) {

    const storedColor = localStorage.getItem(`statusColor_${name}`);
    if (storedColor) {
      return `bg-[${storedColor}]`;
    }
    
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
    } else {
        return 'text-white bg-gradient-to-r from-sky-300 to-pink-300'
    }
}

export { styleStatus }