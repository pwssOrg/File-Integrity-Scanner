CREATE TABLE scans (
    id BIGSERIAL PRIMARY KEY,
    scan_time TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    status TEXT NOT NULL,
    notes TEXT,
    monitored_directory_id bigint NOT NULL REFERENCES monitored_directories(id)
);