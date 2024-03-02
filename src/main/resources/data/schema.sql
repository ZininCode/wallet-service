drop table if exists wallet CASCADE;
drop table if exists transaction CASCADE;

create table wallet
(
    id      bigint not null,
    balance decimal(19, 2),
    player_id varchar (50),
    primary key (id)
);

create table transaction
(
    id         varchar(50) not null,
    amount     decimal(19, 2),
    time timestamp,
    type varchar(20),
    wallet_id        bigint not null,
    primary key (id),
    foreign key (wallet_id) references wallet
);

insert into wallet (id, player_id, balance) values (1, '123001', 1000);
