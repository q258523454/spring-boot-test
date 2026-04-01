CREATE TABLE student
(
    id NUMBER (10) PRIMARY KEY,
    name  varchar(10)                NOT NULL,
    name2 char(10) DEFAULT 'default' NOT NULL,
    age NUMBER (4)
) TABLESPACE TBS_CUR_DAT;

COMMENT ON COLUMN student.name IS '姓名';
COMMENT ON COLUMN student.name2 IS '姓名2';
COMMENT ON COLUMN student.age IS '年龄';


CREATE UNIQUE INDEX STUDENT_IDX_01 ON STUDENT (id, name);
CREATE UNIQUE INDEX STUDENT_IDX_02 ON STUDENT (age);
