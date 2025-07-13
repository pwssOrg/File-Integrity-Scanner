CREATE TABLE checksums (
    id SERIAL PRIMARY KEY,
    file_id INTEGER NOT NULL REFERENCES files(id),
    checksum_sha256 TEXT NOT NULL,
    checksum_sha3 TEXT NOT NULL,
    checksum_blake_2b TEXT NOT NULL
);