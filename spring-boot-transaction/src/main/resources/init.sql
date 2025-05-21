CREATE TABLE `student_transaction`
(
    `id`       int NOT NULL AUTO_INCREMENT,
    `username` varchar(255) DEFAULT NULL,
    `password` varchar(11)  DEFAULT NULL,
    `regTime`  varchar(100) DEFAULT NULL,
    PRIMARY KEY (`id`)
)
ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;



CREATE TABLE `teacher_transaction`
(
    `id`       int NOT NULL AUTO_INCREMENT,
    `username` varchar(255) DEFAULT NULL,
    `password` varchar(11)  DEFAULT NULL,
    `regTime`  varchar(100) DEFAULT NULL,
    PRIMARY KEY (`id`)
)
ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;