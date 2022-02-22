DROP TABLE STUDENT ;
CREATE TABLE student
(
    id    number(20) PRIMARY KEY,
    name  varchar(20)                not NULL,
    name2 char(20) DEFAULT 'default' NOT NULL,
    age   number(5)
) tablespace TBS_CUR_DAT;

COMMENT ON COLUMN student.name IS '姓名';
COMMENT ON COLUMN student.name2 IS '姓名2';
COMMENT ON COLUMN student.age IS '年龄';

INSERT INTO TEST.STUDENT (ID, NAME, NAME2, AGE) VALUES(1, '1', '1', 1);


