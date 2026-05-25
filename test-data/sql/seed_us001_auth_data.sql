-- Traceability: BN-01 -> BR-001 -> EPIC-001 -> US-001
-- Purpose: Synthetic auth/session seed data for login and session management tests
-- Safety: No production data. No plaintext secrets.

BEGIN;

DELETE FROM auth_session WHERE session_id IN ('sess-valid-001','sess-expired-001','sess-revoked-001','sess-boundary-001');
DELETE FROM user_account WHERE username IN ('customer1','customer2','customer.inactive','customer.expired','customer.revoked','customer.boundary','duplicate.user');

INSERT INTO user_account (username, password_hash, enabled, created_at)
VALUES
('customer1', '$2a$10$3m8qZzQbK0xv8W7f9mZp1eX9C4jWl2vX7yq8U4fW9K2T7Qqk1mYdV2', TRUE, NOW()),
('customer2', '$2a$10$k9LhQeV7N5p8X3bR2tYw9uF8cD1sA4mN7pQ2rT6vW8xY1zB3cD5eF', TRUE, NOW()),
('customer.inactive', '$2a$10$eF4xY2uJ9kL0pQ7wR6tS8vN1mB3cD5fG7hJ9kL0mN2pQ4rS6tU8vW', FALSE, NOW()),
('customer.expired', '$2a$10$uY7tR5eW3qA1sD9fG6hJ8kL0pQ2wE4rT6yU8iO1pA3sD5fG7hJ9kL', TRUE, NOW()),
('customer.revoked', '$2a$10$zX1cV3bN5mQ7wE9rT2yU4iO6pA8sD0fG2hJ4kL6mN8pQ1rS3tV5wX', TRUE, NOW()),
('customer.boundary', '$2a$10$B1nD4rY7pQ0sT3uV6wX9zA2cE5fH8jK1mN4pQ7rT0yU3iO6pA9sD', TRUE, NOW()),
('duplicate.user', '$2a$10$N1uM4bV7cX0zA3sD6fG9hJ2kL5pQ8rT1yU4iO7pA0sD3fG6hJ9kL', TRUE, NOW());

INSERT INTO auth_session (session_id, user_account_id, status, issued_at, last_accessed_at, expires_at, revoked_at)
SELECT 'sess-valid-001', id, 'ACTIVE', NOW(), NOW(), NOW() + INTERVAL '30 minutes', NULL FROM user_account WHERE username = 'customer1';

INSERT INTO auth_session (session_id, user_account_id, status, issued_at, last_accessed_at, expires_at, revoked_at)
SELECT 'sess-expired-001', id, 'EXPIRED', NOW() - INTERVAL '2 hours', NOW() - INTERVAL '2 hours', NOW() - INTERVAL '90 minutes', NULL FROM user_account WHERE username = 'customer.expired';

INSERT INTO auth_session (session_id, user_account_id, status, issued_at, last_accessed_at, expires_at, revoked_at)
SELECT 'sess-revoked-001', id, 'REVOKED', NOW() - INTERVAL '45 minutes', NOW() - INTERVAL '45 minutes', NOW() + INTERVAL '15 minutes', NOW() - INTERVAL '20 minutes' FROM user_account WHERE username = 'customer.revoked';

INSERT INTO auth_session (session_id, user_account_id, status, issued_at, last_accessed_at, expires_at, revoked_at)
SELECT 'sess-boundary-001', id, 'ACTIVE', NOW() - INTERVAL '30 minutes', NOW() - INTERVAL '30 minutes', NOW(), NULL FROM user_account WHERE username = 'customer.boundary';

COMMIT;
