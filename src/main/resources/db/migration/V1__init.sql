create table department
(
    id   bigint auto_increment
        primary key,
    name varchar(255) not null
);



create table team
(
    id        bigint auto_increment
        primary key,
    team_name varchar(255) null
);



create table attendance_table
(
    id                    bigint auto_increment
        primary key,
    initial_working_years int null
);



create table employee
(
    id                  bigint auto_increment
        primary key,
    salary_raise        float        null,
    birth_date          date         null,
    degree              varchar(255) null,
    first_name          varchar(255) not null,
    gender              varchar(255) not null,
    graduation_date     varchar(255) not null,
    gross_salary        float        not null,
    last_name           varchar(255) not null,
    national_id         varchar(255) not null,
    net_salary          float        null,
    role                varchar(255) not null,
    years_of_experience int          null,
    attendance_table_id bigint       null,
    department_id       bigint       null,
    manager_id          bigint       null,
    team_id             bigint       null,
    constraint UK_59epvmnko5yc9qssb60euk5q2
        unique (national_id),
    constraint FK8d7lrsr6kwirr93rx0tafnoqa
        foreign key (team_id) references team (id),
    constraint FKbejtwvg9bxus2mffsm3swj3u9
        foreign key (department_id) references department (id),
    constraint FKou6wbxug1d0qf9mabut3xqblo
        foreign key (manager_id) references employee (id),
    constraint FKs9qsdavx3eugepb5ija1ykw4u
        foreign key (attendance_table_id) references attendance_table (id)
);




create table user_credentials
(
    user_name   varchar(255) not null
        primary key,
    password    varchar(255) null,
    user_role   varchar(255) null,
    employee_id bigint null,
    constraint FKsxkqqiix7w2r3t9ct3hf5lm0c
        foreign key (employee_id) references employee (id)
);



create table day_details
(
    id                  bigint auto_increment
        primary key,
    absent              bit not null,
    bonus_in_salary     float null,
    date                date null,
    attendance_table_id bigint null,
    constraint FKqctjowluhmubu5ndv399ak0td
        foreign key (attendance_table_id) references attendance_table (id)
);



create table month_details
(
    id                    int auto_increment
        primary key,
    absences              int null,
    bonuses               float null,
    date                  date null,
    attendance_table_id   bigint null,
    gross_salary_of_month float null,
    constraint FKrygebn9ic30vgr1s8t7a82p0s
        foreign key (attendance_table_id) references attendance_table (id)
);

