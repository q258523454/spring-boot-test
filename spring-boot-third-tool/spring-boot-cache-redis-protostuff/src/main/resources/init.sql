

use zhang;

CREATE DATABASE IF NOT EXISTS zhang;

CREATE TABLE `student` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `name` varchar(255) DEFAULT NULL,
    `age` int(11) DEFAULT NULL,
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


BEGIN
    INSERT INTO zhang.student(id, name, age) VALUES (1, 'zhangsan', 18);
    INSERT INTO zhang.student(id, name, age) VALUES (2, 'wangwu', 20);
END