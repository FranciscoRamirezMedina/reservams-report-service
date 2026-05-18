CREATE DATABASE IF NOT EXISTS reservams_report_db;

USE reservams_report_db;

CREATE TABLE report_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    report_type VARCHAR(80) NOT NULL,
    generated_by_user_id BIGINT NOT NULL,
    description VARCHAR(255) NOT NULL,
    generated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);