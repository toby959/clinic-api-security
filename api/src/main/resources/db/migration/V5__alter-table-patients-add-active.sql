CREATE TABLE patients(
    id bigint not null auto_increment,
    name varchar(100) not null,
    email varchar(100) not null unique,
    document varchar(8) not null unique,
    phone varchar(20) not null,
    street varchar(100) not null,
    distrit varchar(100) not null,
    addition varchar(100),
    number varchar(20),
    city varchar(100) not null,

    primary key(id)
);