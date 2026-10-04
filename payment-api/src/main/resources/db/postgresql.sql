CREATE TABLE IF NOT EXISTS app_users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(20),
    role VARCHAR(20) NOT NULL,
    account_locked BOOLEAN DEFAULT FALSE NOT NULL,
    account_enabled BOOLEAN DEFAULT TRUE NOT NULL,
    password_expired BOOLEAN DEFAULT FALSE NOT NULL,
    failed_login_attempts INT DEFAULT 0 NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS system_config (
    config_key VARCHAR(50) PRIMARY KEY,
    config_value VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS payment_transaction (
    id BIGSERIAL PRIMARY KEY,
    transaction_id VARCHAR(100) NOT NULL UNIQUE,
    provider VARCHAR(50) NOT NULL,
    handler_id VARCHAR(100),
    amount NUMERIC(15,2) NOT NULL,
    currency VARCHAR(10) NOT NULL,
    payer_id VARCHAR(100),
    status VARCHAR(50) NOT NULL,
    gateway_tx_id VARCHAR(100),
    description VARCHAR(255),
    created_by VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS maker_checker_request (
    id BIGSERIAL PRIMARY KEY,
    request_type VARCHAR(50) NOT NULL,
    maker_user VARCHAR(50) NOT NULL,
    checker_user VARCHAR(50),
    status VARCHAR(20) NOT NULL,
    payload_json TEXT,
    remarks VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS audit_log (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50),
    action VARCHAR(100) NOT NULL,
    details VARCHAR(500),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);
