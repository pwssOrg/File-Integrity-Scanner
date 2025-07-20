CREATE TABLE scan_details (
    id SERIAL PRIMARY KEY,
    scan_id INTEGER NOT NULL REFERENCES scans(id),
    file_id bigint NOT NULL REFERENCES files(id),
    checksum_id bigint NOT NULL REFERENCES checksums(id)
);