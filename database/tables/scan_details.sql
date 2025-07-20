CREATE TABLE scan_details (
    id BIGSERIAL PRIMARY KEY,
    scan_id bigint NOT NULL REFERENCES scans(id),
    file_id bigint NOT NULL REFERENCES files(id),
    checksum_id INTEGER NOT NULL REFERENCES checksums(id)
);