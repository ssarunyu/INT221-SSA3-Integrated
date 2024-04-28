class TaskManagement {
    constructor() {
        this.allTasks = []
    }
    addTask(item) {
        this.allTasks.push(item)
    }
    addAllTask(items) {
        items.forEach((i) => {
            this.allTasks.push(i)
        })
    }
    getAllTask() {
        return this.allTasks
    }
}

export { TaskManagement }