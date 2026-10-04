IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'app_users')
BEGIN
    CREATE TABLE app_users (
        id BIGINT IDENTITY(1,1) PRIMARY KEY,
        username VARCHAR(50) NOT NULL UNIQUE,
        password VARCHAR(255) NOT NULL,
        email VARCHAR(100),
        phone VARCHAR(20),
        role VARCHAR(20) NOT NULL,
        account_locked BIT DEFAULT 0 NOT NULL,
        account_enabled BIT DEFAULT 1 NOT NULL,
        password_expired BIT DEFAULT 0 NOT NULL,
        failed_login_attempts INT DEFAULT 0 NOT NULL,
        created_at DATETIME DEFAULT GETDATE() NOT NULL
    );
END;

IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'system_config')
BEGIN
    CREATE TABLE system_config (
        config_key VARCHAR(50) PRIMARY KEY,
        config_value VARCHAR(255) NOT NULL
    );
END;
