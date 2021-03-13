CREATE TABLE student
(
    id    number(10) PRIMARY KEY,
    name  varchar(10)                not NULL,
    name2 char(10) DEFAULT 'default' NOT NULL,
    age   number(4)
) tablespace TBS_CUR_DAT;

COMMENT ON COLUMN student.name IS '姓名';
COMMENT ON COLUMN student.name2 IS '姓名2';
COMMENT ON COLUMN student.age IS '年龄';


CREATE UNIQUE INDEX STUDENT_IDX_01 on STUDENT (id,name);
CREATE UNIQUE INDEX STUDENT_IDX_02 on STUDENT (age);
