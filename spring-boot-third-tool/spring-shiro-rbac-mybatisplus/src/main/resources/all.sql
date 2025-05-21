CREATE TABLE `exam_user`
(
    `user_id`  bigint(11)                                              NOT NULL COMMENT '用户ID',
    `username` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '用户名',
    `password` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
    `salt`     varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci  NULL DEFAULT NULL COMMENT '盐值',
    `state`    varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci  NULL DEFAULT NULL COMMENT '状态:NORMAL正常  PROHIBIT禁用',
    PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户表';


CREATE TABLE `exam_role`
(
    `id`         int         NOT NULL COMMENT '角色id',
    `role_name`  varchar(20) NOT NULL DEFAULT '' COMMENT '角色名',
    `is_enable`  tinyint(1)  NOT NULL DEFAULT '0' COMMENT '是否有效  1有效  0无效',
    `created_at` datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='角色表';

CREATE TABLE `exam_permission`
(
    `id`                  int          NOT NULL COMMENT '自定id,主要供前端展示权限列表分类排序使用',
    `menu_code`           varchar(255) NOT NULL DEFAULT '' COMMENT '归属菜单,前端判断并展示菜单使用',
    `menu_name`           varchar(255) NOT NULL DEFAULT '' COMMENT '菜单的中文释义',
    `permission_code`     varchar(255) NOT NULL DEFAULT '' COMMENT '权限的代码/通配符,对应代码中@RequiresPermissions 的value',
    `permission_name`     varchar(255) NOT NULL DEFAULT '' COMMENT '本权限的中文释义',
    `required_permission` tinyint(1)   NOT NULL DEFAULT '0' COMMENT '是否本菜单必选权限, 1.必选 0.非必选 通常是"列表"权限是必选',
    `created_at`          datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`          datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='权限表';

CREATE TABLE `exam_role_permission`
(
    `id`            int        NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `role_id`       int        NOT NULL DEFAULT '0' COMMENT '角色id',
    `permission_id` int        NOT NULL DEFAULT '0' COMMENT '权限id',
    `is_enable`     tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否有限，0、无效，1、有效',
    `created_at`    datetime   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`    datetime   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='角色-权限关联表';

CREATE TABLE `exam_user_role`
(
    `id`          int        NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id`     int        NOT NULL COMMENT '用户ID',
    `role_id`     int        NOT NULL COMMENT '角色ID',
    `is_enable`   tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否有效，0、无效，1、有效',
    `create_time` timestamp  NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp  NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户-角色关系表';


# exam_user
INSERT INTO `exam_user`
VALUES (1, '张三', 'a1bb09ad5dea12e0f94762cb116c447e80c784d8aa2c6625263f7f3436cdd583', 'RvP3UID2n30Q2sycZYvH',
        'NORMAL');
INSERT INTO `exam_user`
VALUES (2, '李四', '376eb5d2698c804ee83594fe8b0217f03ad138a046f7fa42b44c232c2e5e2b38', 'OVlrD37bDUKNcFRB10qG',
        'NORMAL');

# exam_role
INSERT INTO `exam_role`
VALUES (1, 'admin', 1, '2022-09-09 10:56:07', '2022-09-09 10:56:07');
INSERT INTO `exam_role`
VALUES (2, 'user', 1, '2022-09-09 10:56:07', '2022-09-09 10:56:07');

# exam_permission
INSERT INTO `exam_permission`
VALUES (1, 'code001', 'code001', 'sys:user:info', '查看用户列表', 1, '2022-09-09 11:00:26', '2022-09-09 11:00:26');
INSERT INTO `exam_permission`
VALUES (2, 'code002', 'code002', 'sys:role:info', '查看角色列表', 1, '2022-09-09 11:00:26', '2022-09-09 11:00:26');
INSERT INTO `exam_permission`
VALUES (3, 'code003', 'code003', 'sys:menu:info', '查看权限列表', 1, '2022-09-09 11:00:26', '2022-09-09 11:00:26');
INSERT INTO `exam_permission`
VALUES (4, 'code004', 'code004', 'sys:info:all', '查看所有数据', 1, '2022-09-09 11:00:26', '2022-09-09 11:00:26');

# exam_role_permission
INSERT INTO `exam_role_permission`
VALUES (1, 1, 1, 1, '2022-09-09 10:58:40', '2022-09-09 10:58:40');
INSERT INTO `exam_role_permission`
VALUES (2, 1, 2, 1, '2022-09-09 10:58:40', '2022-09-09 10:58:40');
INSERT INTO `exam_role_permission`
VALUES (3, 1, 3, 1, '2022-09-09 10:58:40', '2022-09-09 10:58:40');
INSERT INTO `exam_role_permission`
VALUES (4, 2, 1, 1, '2022-09-09 10:58:40', '2022-09-09 10:58:40');

# exam_user_role
INSERT INTO `exam_user_role`
VALUES (1, 1, 1, 1, '2022-09-09 11:04:14', '2022-09-09 11:04:14');
INSERT INTO `exam_user_role`
VALUES (2, 2, 2, 1, '2022-09-09 11:04:14', '2022-09-09 11:04:14');



