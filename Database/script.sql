CREATE DATABASE task_base;

USE task_base;

CREATE TABLE `task` (
  `id` int NOT NULL,
  `title` varchar(100) NOT NULL,
  `description` varchar(500) DEFAULT NULL,
  `assignees` varchar(30) DEFAULT NULL,
  `status` enum('NO_STATUS','TO_DO','DOING','DONE') NOT NULL Default 'NO_STATUS',
  `createdOn` datetime NOT NULL,
  `updatedOn` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO task (id, title, description, assignees, status, createdOn, updatedOn) VALUES
(1, 'TaskTitle1TaskTitle2TaskTitle3TaskTitle4TaskTitle5TaskTitle6TaskTitle7TaskTitle8TaskTitle9TaskTitle0',
'Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti1Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti2Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti3Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti4Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti5',
'Assignees1Assignees2Assignees3',
'NO_STATUS',
'2024-04-22 16:00:00+07:00',
'2024-04-22 16:00:00+07:00');

INSERT INTO task (id, title, status, createdOn, updatedOn) VALUES
(2, 'Repository','TO_DO', '2024-04-22 16:05:00+07:00', '2024-04-22 21:00:00+07:00');

INSERT INTO task (id, title, description, assignees, status, createdOn, updatedOn) VALUES
(3, 'ดาต้าเบส', 'ສ້າງຖານຂໍ້ມູນ', 'あなた、彼、彼女 (私ではありません)', 'DOING', '2024-04-22 16:10:00+07:00', '2024-04-25 07:00:00+07:00');

INSERT INTO task (id, title, description, assignees, status, createdOn, updatedOn) VALUES
(4, '_Infrastructure_', '_Setup containers_', 'ไก่งวง กับ เพนกวิน', 'DONE', '2024-04-22 16:15:00+07:00', '2024-04-22 17:00:00+07:00');

select * from task; 