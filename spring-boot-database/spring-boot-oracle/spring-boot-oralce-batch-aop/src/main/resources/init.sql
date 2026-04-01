CREATE TABLE student
(
    id NUMBER (10) PRIMARY KEY,
    name  varchar(10) NOT NULL,
    name2 char(10) DEFAULT 'default',
    age NUMBER (4) NOT NULL
) TABLESPACE TBS_CUR_DAT;

COMMENT ON COLUMN student.name IS '姓名';
COMMENT ON COLUMN student.name2 IS '姓名2';
COMMENT ON COLUMN student.age IS '年龄';