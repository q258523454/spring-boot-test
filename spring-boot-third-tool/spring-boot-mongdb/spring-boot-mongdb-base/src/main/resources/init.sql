db.getCollection("bak_student").drop();
db.createCollection("bak_student");
db.getCollection("bak_student").createIndex({
    sid: NumberInt("1")
}, {
    name: "idx_sid",
    background: true
});

db.getCollection("bak_student").insert([
    {sid: 1,name: '小明',age: 1},
    {sid: 2,name: '张三',age: 2},
    {sid: 3,name: '李四',age: 3},
    {sid: 4,name: '王五',age: 4},
    {sid: 5,name: '赵六',age: 5}
]);
