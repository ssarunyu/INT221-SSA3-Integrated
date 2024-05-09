class TaskManagement {
    constructor() {
        this.allTasks = []
        this.allStatus = []
    }
    addTask(item) {
        this.allTasks.push(item)
    }
    addAllTask(items) {
        items.forEach((i) => {
            this.allTasks.push(i)
        })
    }
    updateTask(task, id) {
        const index = this.allTasks.findIndex(item => item.id === id)
        this.allTasks[index] = task
    }
    deleteTask(id) {
        const index = this.allTasks.findIndex(item => item.id === id)
        this.allTasks.splice(index, 1)
    }
    getAllTask() {
        return this.allTasks
    }
    getAllStatus(){
        return this.allStatus
    }
}

export { TaskManagement }