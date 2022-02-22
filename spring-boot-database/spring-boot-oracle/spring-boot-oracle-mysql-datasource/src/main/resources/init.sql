

-- mysql
CREATE TABLE `student` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
INSERT INTO zhang.student (name,age) VALUES
('zhangsan',18)
,('wangwu',20)
;


-- oracle
CREATE TABLE student(
    id number(10) PRIMARY KEY ,
    name varchar(10) NOT NULL,
    age number(4) NOT NULL
)
COMMENT ON COLUMN student.name IS '姓名';
COMMENT ON COLUMN student.age IS '年龄';
INSERT INTO STUDENT(id,name,age) values(1,'王五',21);

