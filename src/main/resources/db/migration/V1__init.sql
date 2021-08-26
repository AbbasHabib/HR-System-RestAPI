create table department
(
    id   bigint NOT NULL    AUTO_INCREMENT
        primary key,
    name varchar(255) not null
);
create table team
(
    id   bigint NOT NULL    AUTO_INCREMENT
        primary key,
    team_name varchar(255) null
);

create table employee
(
    id   bigint NOT NULL    AUTO_INCREMENT
        primary key,
    birth_date      date         null,
    gender          varchar(255) null,
    graduation_date date         null,
    gross_salary    float        null,
    name            varchar(255) not null,
    net_salary      float        null,
    department_id   bigint       null,
    manager_id      bigint       null,
    team_id         bigint       null,
    constraint FK8d7lrsr6kwirr93rx0tafnoqa
        foreign key (team_id) references team(id),
    constraint FKbejtwvg9bxus2mffsm3swj3u9
        foreign key (department_id) references department(id),
    constraint FKou6wbxug1d0qf9mabut3xqblo
        foreign key (manager_id) references employee(id)
);


