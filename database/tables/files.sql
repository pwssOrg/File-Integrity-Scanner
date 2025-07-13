CREATE TABLE files (
    id SERIAL PRIMARY KEY,
    path TEXT NOT NULL UNIQUE,
    basename TEXT NOT NULL,
    directory TEXT NOT NULL,
    size BIGINT NOT NULL,
    mtime TIMESTAMPTZ NOT NULL
);