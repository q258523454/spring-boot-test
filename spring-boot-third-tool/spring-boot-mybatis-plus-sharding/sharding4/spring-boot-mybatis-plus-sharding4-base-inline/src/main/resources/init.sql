# sharding 默认库 (不分片的时候,插入 not_sharding 测试)
use zhang_0;
CREATE TABLE `not_sharding`
(
    `id`   int NOT NULL AUTO_INCREMENT COMMENT 'id',
    `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '姓名',
    `age`  int                                                           DEFAULT NULL COMMENT '年龄',
    PRIMARY KEY (`id`)
)
ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;


# 第一个 分片库
use zhang_0;
CREATE TABLE `student_0`
(
    `id`   int NOT NULL AUTO_INCREMENT COMMENT 'id',
    `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '姓名',
    `age`  int                                                           DEFAULT NULL COMMENT '年龄',
    PRIMARY KEY (`id`)
)
ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

# 第二个 分片库
use zhang_1;
CREATE TABLE `student_1`
(
    `id`   int NOT NULL AUTO_INCREMENT COMMENT 'id',
    `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '姓名',
    `age`  int                                                           DEFAULT NULL COMMENT '年龄',
    PRIMARY KEY (`id`)
)
ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;


# 查询all的时候, 不同的数据库，每个表都要存在，否则会报错
use zhang_0;
CREATE TABLE `student_1`
(
    `id`   int NOT NULL AUTO_INCREMENT COMMENT 'id',
    `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '姓名',
    `age`  int                                                           DEFAULT NULL COMMENT '年龄',
    PRIMARY KEY (`id`)
)
ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;
use zhang_1;
CREATE TABLE `student_0`
(
    `id`   int NOT NULL AUTO_INCREMENT COMMENT 'id',
    `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '姓名',
    `age`  int                                                           DEFAULT NULL COMMENT '年龄',
    PRIMARY KEY (`id`)
)
ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;
