CREATE TABLE scans (
    id SERIAL PRIMARY KEY,
    scan_time TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    status TEXT NOT NULL,
    notes TEXT,
    monitored_directory_id INTEGER NOT NULL REFERENCES monitored_directories(id)
);