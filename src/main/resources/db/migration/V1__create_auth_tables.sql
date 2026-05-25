create table if not exists user_account (
    id bigserial primary key,
    username varchar(100) not null unique,
    password_hash varchar(255) not null,
    enabled boolean not null default true,
    created_at timestamp with time zone not null default now()
);

create table if not exists auth_session (
    id bigserial primary key,
    session_id varchar(64) not null unique,
    user_account_id bigint not null references user_account(id),
    status varchar(20) not null,
    issued_at timestamp with time zone not null,
    last_accessed_at timestamp with time zone not null,
    expires_at timestamp with time zone not null,
    revoked_at timestamp with time zone null
);

create index if not exists idx_auth_session_user_account_id on auth_session(user_account_id);
create index if not exists idx_auth_session_expires_at on auth_session(expires_at);
