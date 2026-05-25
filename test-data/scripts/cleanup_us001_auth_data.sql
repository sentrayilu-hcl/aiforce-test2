-- Traceability: BN-01 -> BR-001 -> EPIC-001 -> US-001
-- Purpose: Remove synthetic auth/session data after test execution

BEGIN;

DELETE FROM auth_session WHERE session_id IN ('sess-valid-001','sess-expired-001','sess-revoked-001','sess-boundary-001');
DELETE FROM user_account WHERE username IN ('customer1','customer2','customer.inactive','customer.expired','customer.revoked','customer.boundary','duplicate.user');

COMMIT;
