use irbl;

drop table if exists `Repo`;

create table `Repo`(
    `id` int(11) not null auto_increment,
    `description` varchar(255) default null,
    `gitUrl` varchar(255) default null,
    `status` int(11) default null,
    `start_time` datetime default null,
    `end_time` datetime default null,
    primary key (`id`)
)engine=InnoDB default charset=utf8;

drop table if exists `CommitPreprocess`;

create table `CommitPreprocess`(
    `id` int(11) not null auto_increment,
    `repoCommitId` varchar(255) default null,
    `processCommitId` varchar(255) default null,
    `filePath` varchar(255) default null,
    primary key (`id`)
)engine=InnoDB default charset=utf8;