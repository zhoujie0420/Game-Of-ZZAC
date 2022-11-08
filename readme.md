#### 一个伟大的史诗级巨著

## sql
```
create table bot
(
    id          int auto_increment
        primary key,
    user_id     int            not null,
    title       varchar(100)   null,
    description varchar(300)   null,
    content     varchar(10000) null,
    createtime  datetime       null,
    modifytime  datetime       null,
    constraint id
        unique (id)
);

create table comment
(
    id         int auto_increment
        primary key,
    foreign_id int          not null,
    user_id    int          null,
    username   varchar(20)  not null,
    content    varchar(255) null,
    pid        int          not null,
    target     varchar(255) null,
    createtime datetime     not null,
    constraint id
        unique (id)
);

create table post
(
    id         int auto_increment
        primary key,
    username   varchar(100)  not null,
    photo      varchar(1000) not null,
    user_id    int           not null,
    content    text          not null,
    reply      int default 0 not null,
    createtime datetime      not null,
    constraint id
        unique (id)
);

create table record
(
    id         int auto_increment,
    a_id       int           null,
    a_sx       int           null,
    a_sy       int           null,
    b_id       int           null,
    b_sx       int           null,
    b_sy       int           null,
    a_steps    varchar(1000) null,
    b_steps    varchar(1000) null,
    map        varchar(1000) null,
    loser      varchar(10)   null,
    createtime datetime      null,
    constraint id
        unique (id)
);

create table user
(
    id       int auto_increment
        primary key,
    username varchar(100)     null,
    password varchar(100)     null,
    photo    varchar(1000)    null,
    rating   int default 1500 null,
    constraint id
        unique (id)
);



```

设计流程图



![Pic](redeme_image\设计流程.png "Pic")