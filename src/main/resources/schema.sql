create schema if not exists s_share;
use s_share;

create table if not exists account
(
    account_id         bigint unsigned       primary key  auto_increment,
    user_id            varchar(15)           unique       not null,
    user_name          varchar(30)           unique       not null,
    password           char(60)                           not null,
    profile            text                  default '',
    profile_photo_url  varchar(300)          default null,
    updated_at         timestamp(3)          default current_timestamp(3) not null,
    lock_version       int unsigned          default 0    not null,
    status             smallint unsigned     default 0    not null,
    shussha_count      int                   default 0    not null
);

create table if not exists friend
(
    account_id_from    bigint unsigned                    not null,
    account_id_to      bigint unsigned                    not null,
    status             smallint unsigned    default 0     not null,
    lock_version       int unsigned         default 0     not null,
    updated_at         timestamp(3)         default current_timestamp(3) not null,
    primary key(account_id_from, account_id_to),
    foreign key (account_id_from) references account (account_id),
    foreign key (account_id_to) references account (account_id)
);

create table if not exists shussha
(
    shussha_id     bigint unsigned       primary key          auto_increment,
    account_id     bigint unsigned                            not null,
    date           date                  default current_date,
    status         smallint unsigned     default 0            not null,
    lock_version   int unsigned          default 0            not null,
    updated_at     timestamp(3)          default current_timestamp(3) not null,
    foreign key (account_id) references account (account_id),
    unique key (account_id, date)
);
