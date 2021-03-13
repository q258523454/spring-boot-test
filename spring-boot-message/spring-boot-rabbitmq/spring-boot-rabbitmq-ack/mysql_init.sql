CREATE TABLE `t_order`
(
    `id`         int(255) NOT NULL AUTO_INCREMENT,
    `name`       varchar(128) DEFAULT NULL,
    `message_id` varchar(128) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


CREATE TABLE `broker_message_log`
(
    `message_id`  varchar(128) NOT NULL,
    `message`     varchar(4000) DEFAULT NULL,
    `try_count`   int(4)        DEFAULT '0',
    `status`      varchar(10)   DEFAULT NULL,
    `next_retry`  datetime      DEFAULT NULL,
    `create_time` datetime      DEFAULT NULL,
    `update_time` datetime      DEFAULT NULL,
    PRIMARY KEY (`message_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;