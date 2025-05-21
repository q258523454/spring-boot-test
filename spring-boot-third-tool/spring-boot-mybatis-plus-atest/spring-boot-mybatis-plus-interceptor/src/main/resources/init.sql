-- mysql
CREATE TABLE `student`
(
    `id`   int NOT NULL AUTO_INCREMENT COMMENT 'id',
    `name` varchar(255) DEFAULT NULL COMMENT '姓名',
    `age`  int          DEFAULT NULL COMMENT '年龄',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

INSERT INTO zhang.student (name, age)
VALUES ('zhangsan', 18)
     , ('wangwu', 20)
;