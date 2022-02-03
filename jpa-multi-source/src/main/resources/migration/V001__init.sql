CREATE TABLE `user`(
    `id`   int unsigned NOT NULL AUTO_INCREMENT,
    `age`  int DEFAULT NULL,
    `name` varchar(100) DEFAULT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `message`(
    `id`      int unsigned NOT NULL AUTO_INCREMENT,
    `title`   varchar(100)  DEFAULT NULL,
    `message` text,
    PRIMARY KEY (`id`)
) ;