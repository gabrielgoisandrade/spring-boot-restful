CREATE TABLE IF NOT EXISTS TB_USER
(
    USER_ID   SERIAL PRIMARY KEY,
    NAME      VARCHAR(30)         NOT NULL,
    LAST_NAME VARCHAR(50)         NOT NULL,
    EMAIL     VARCHAR(100) UNIQUE NOT NULL,
    PASSWORD  VARCHAR(100)        NOT NULL
);