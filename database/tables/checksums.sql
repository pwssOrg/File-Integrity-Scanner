CREATE TABLE checksums (
    id SERIAL PRIMARY KEY,
    file_id INTEGER NOT NULL REFERENCES files(id),
    checksum_type TEXT NOT NULL,
    value TEXT NOT NULL
);