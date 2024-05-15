GRANT ALL PRIVILEGES ON *.* TO 'dev1'@'%';

DROP DATABASE IF EXISTS `task_base`;
CREATE SCHEMA `task_base` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
use task_base; 

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
 
--
-- Table structure for table `Task`
--
 
DROP TABLE IF EXISTS `tasks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tasks` (
  `taskId` int NOT NULL AUTO_INCREMENT,
  `title` varchar(100) DEFAULT NULL ,
  `description`varchar(500) DEFAULT NULL ,
  `assignees` varchar(30) DEFAULT NULL,
  `createdOn` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedOn` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`taskId`),
  UNIQUE KEY `id_UNIQUE` (`taskId`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
 
 
USE task_base;

CREATE TABLE statuses (
  statusId INT NOT NULL AUTO_INCREMENT,
  statusName VARCHAR(50) NOT NULL UNIQUE,
  statusDescription VARCHAR(200),
  PRIMARY KEY (statusId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

ALTER TABLE tasks
ADD COLUMN statusId INT not null ,
ADD CONSTRAINT fk_tasks_statuses
FOREIGN KEY (statusId) REFERENCES statuses(statusId);

 
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
 
INSERT INTO statuses (statusName, statusDescription)
VALUES
('No Status', 'The default status'),
    ('To Do', null),
    ('Doing', 'Being worked on'),
    ('Done', 'Finished');

-- INSERT INTO `tasks` VALUES
-- (1,'TaskTitle1TaskTitle2TaskTitle3TaskTitle4TaskTitle5TaskTitle6TaskTitle7TaskTitle8TaskTitle9TaskTitle0','Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti1Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti2Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti3Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti4Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti5','Assignees1Assignees2Assignees3','2024-04-22 09:00:00','2024-04-22 09:00:00', 1),
-- (2,'Repository',null,null,'2024-04-22 09:05:00','2024-04-22 14:00:00', 2),
-- (3,'ดาต้าเบส','ສ້າງຖານຂໍ້ມູນ','あなた、彼、彼女 (私ではありません)','2024-04-22 09:10:00','2024-04-25 00:00:00',3 ),
-- (4,'_Infrastructure_','_Setup containers_','ไก่งวง กับ เพนกวิน','2024-04-22 09:15:00','2024-04-22 10:00:00', 4);

select * from tasks;
select * from statuses ; 