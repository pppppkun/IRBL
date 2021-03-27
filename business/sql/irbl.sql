use irbl;

drop table if exists `Repo`;

create table `Repo`
(
    `id`          int(11) not null auto_increment,
    `description` varchar(255) default null,
    `gitUrl`      varchar(255) default null,
    `state`      int(11)      default null,
    `queryNum`    int(11)      default null,
    `startTime`   datetime     default null,
    `endTime`     datetime     default null,
    primary key (`id`)
) engine = InnoDB
  default charset = utf8;

drop table if exists `User`;
create table `User`
(
    `id`       int(11) not null auto_increment,
    `username` varchar(255) default null,
    `password` varchar(255) default null,
    `role`     varchar(15)  default null,
    `queryNum` int(11)      default null,
    primary key (`id`)
) engine = InnoDB
  default charset = utf8;

drop table if exists `CommitRepo`;

drop table if exists `UserRepo`;

drop table if exists `CommitPreprocess`;

create table `CommitPreprocess`
(
    `id`              int(11) not null auto_increment,
    `repoCommitId`    varchar(255) default null,
    `processCommitId` varchar(255) default null,
    `filePath`         varchar(255) default null,
    primary key (`id`)
) engine = InnoDB
  default charset = utf8;
