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
    deleteTask(id) {
        const find = this.getAllTask.find(item => item.id === id)
        this.allTasks.splice(find, 1)
    }
    getAllTask() {
        return this.allTasks
    }
}

export { TaskManagement }