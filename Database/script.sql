create database task_base;

select * from task;

CREATE TABLE `task` (
  `id` int NOT NULL,
  `title` varchar(100) NOT NULL,
  `description` varchar(500) DEFAULT NULL,
  `assignee` varchar(30) DEFAULT NULL,
  `status` enum('NO_STATUS','TO_DO','DOING','DONE') NOT NULL Default 'NO_STATUS',
  `createOn` year NOT NULL,
  `updateOn` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

show variables like '%time_zone%';

set time_zone = '+07:00';

select * from mysql.time_zone_name;

INSERT INTO task (id, title, description, assignee, status, createOn, updateOn) VALUES
(1, 'Task 1', 'Description for Task 1', 'John Doe', 'TO_DO', YEAR(NOW()), NOW()),
(2, 'Task 2', 'Description for Task 2', 'Jane Smith', 'DOING', YEAR(NOW()), NOW()),
(3, 'Task 3', 'Description for Task 3', 'Alice Johnson', 'DONE', YEAR(NOW()), NOW());

select * from task;




