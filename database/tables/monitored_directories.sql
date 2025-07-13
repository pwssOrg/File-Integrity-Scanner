CREATE TABLE monitored_directories (
    id SERIAL PRIMARY KEY,
    path TEXT NOT NULL UNIQUE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    added_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    last_scanned TIMESTAMPTZ,
    notes TEXT,
    baseline_established BOOLEAN NOT NULL DEFAULT FALSE
);