package dev.scopped.zenLifesteal.database;

public interface DatabaseQuery {

    String CREATE_USER_TABLE = """
            CREATE TABLE IF NOT EXISTS `lifesteal_users` (
                `id`        INT             PRIMARY KEY AUTO_INCREMENT,
                `uuid`      VARCHAR(36)     NOT NULL,
                `name`      VARCHAR(16)     NOT NULL    UNIQUE
            )
            """;

    String CREATE_USER_STATISTICS_TABLE = """
            CREATE TABLE IF NOT EXISTS `lifesteal_user_statistics` (
                `id`              INT                           PRIMARY KEY AUTO_INCREMENT,
                `user_id`         INT                           NOT NULL,
                `kills`           BIGINT                        NOT NULL    DEFAULT 0,
                `deaths`          BIGINT                        NOT NULL    DEFAULT 0,
                `hearts`          BIGINT                        NOT NULL    DEFAULT 0,
                `playtime`        BIGINT                        NOT NULL    DEFAULT 0,
                `first_join`      BIGINT                        NOT NULL    DEFAULT 0,
                `last_join`       BIGINT                        NOT NULL    DEFAULT 0,
                FOREIGN KEY (`user_id`) REFERENCES `lifesteal_users`(`id`)
                    ON DELETE CASCADE
                    ON UPDATE CASCADE
            )
            """;

    String GET_USER_BY_UUID = """
            SELECT `id`, `name`
            FROM `lifesteal_users`
            WHERE `uuid` = ?
            """;
    String GET_USER_BY_NAME = """
            SELECT `id`, `uuid`
            FROM `lifesteal_users`
            WHERE `name` = ?
            """;

    String INSERT_USER = """
            INSERT INTO `lifesteal_users` (`uuid`, `name`)
            VALUES (?, ?)
            """;

    String INSERT_USER_STATISTICS = """
            INSERT INTO `lifesteal_user_statistics` (`user_id`)
            VALUES (?)
            """;

    String GET_USER_STATISTICS = """
            SELECT *
            FROM `lifesteal_user_statistics`
            WHERE `user_id` = ?
            """;

    String UPDATE_STATISTICS = """
            UPDATE `lifesteal_user_statistics`
            SET `%s` = ?
            WHERE `user_id` = ?
            """;

    String ADD_STATISTIC = """
            UPDATE `lifesteal_user_statistics`
            SET `%s` = `%s` + ?
            WHERE `user_id` = ?
            """;

    String REMOVE_STATISTIC = """
            UPDATE `lifesteal_user_statistics`
            SET `%s` = `%s` - ?
            WHERE `user_id` = ?
            """;

    String GET_LEADERBOARD = """
            SELECT u.`name` AS name, s.`%s` AS amount
            FROM `lifesteal_user_statistics` s
            INNER JOIN `lifesteal_users` u ON u.`id` = s.`user_id`
            ORDER BY amount DESC
            LIMIT ?
            """;

}
