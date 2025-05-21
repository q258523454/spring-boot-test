-- mysql
CREATE TABLE `student`
(
    `id`          int         NOT NULL AUTO_INCREMENT COMMENT 'id',
    `name`        varchar(255)         DEFAULT NULL COMMENT '姓名',
    `age`         int                  DEFAULT NULL COMMENT '年龄',
    `created_at`  datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `update_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
    PRIMARY KEY (`id`)
)
ENGINE = InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARSET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

INSERT INTO spring_boot_mybatis_plus.student (name, age)
VALUES ('zhangsan', 18)
     , ('wangwu', 20)
;