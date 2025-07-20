-- Insert mock monitored directories with their status and metadata
INSERT INTO monitored_directories (path, is_active, added_at, last_scanned, notes, baseline_established) VALUES
('/var/logs', TRUE, NOW() - INTERVAL '10 days', NOW() - INTERVAL '1 day', 'System log directory', TRUE),
('/home/user/docs', TRUE, NOW() - INTERVAL '5 days', NULL, 'User documents', FALSE),
('/etc', FALSE, NOW() - INTERVAL '20 days', NOW() - INTERVAL '5 days', 'Config files', TRUE);

-- Insert mock scan records for the monitored directories
INSERT INTO scans (scan_time, status, notes, monitored_directory_id) VALUES
(NOW() - INTERVAL '1 day', 'completed', 'Daily scan', 1),
(NOW() - INTERVAL '2 days', 'failed', 'Disk error', 1),
(NOW() - INTERVAL '3 days', 'completed', 'First scan of docs', 2);

-- Insert mock files found in the monitored directories
INSERT INTO files (path, basename, directory, size, mtime) VALUES
('/var/logs/syslog', 'syslog', '/var/logs', 1048576, NOW() - INTERVAL '1 day'),
('/var/logs/auth.log', 'auth.log', '/var/logs', 524288, NOW() - INTERVAL '2 days'),
('/home/user/docs/resume.pdf', 'resume.pdf', '/home/user/docs', 204800, NOW() - INTERVAL '1 day');

-- Insert mock checksums for the files
INSERT INTO checksums (file_id, checksum_sha256, checksum_sha3, checksum_blake_2b) VALUES
(1, 'd2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2', 'e3e3e3e3e3e3e3e3e3e3e3e3e3e3e3e3e3e3e3e3e3e3e3e3e3e3e3e3e3e3e3e3', 'b4b4b4b4b4b4b4b4b4b4b4b4b4b4b4b4b4b4b4b4b4b4b4b4b4b4b4b4b4b4b4b4'),
(2, 'a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1', 'c2c2c2c2c2c2c2c2c2c2c2c2c2c2c2c2c2c2c2c2c2c2c2c2c2c2c2c2c2c2c2c2', 'f5f5f5f5f5f5f5f5f5f5f5f5f5f5f5f5f5f5f5f5f5f5f5f5f5f5f5f5f5f5f5f5'),
(3, 'b3b3b3b3b3b3b3b3b3b3b3b3b3b3b3b3b3b3b3b3b3b3b3b3b3b3b3b3b3b3b3b3', 'd4d4d4d4d4d4d4d4d4d4d4d4d4d4d4d4d4d4d4d4d4d4d4d4d4d4d4d4d4d4d4d4', 'e6e6e6e6e6e6e6e6e6e6e6e6e6e6e6e6e6e6e6e6e6e6e6e6e6e6e6e6e6e6e6e6');

-- Insert mock scan details linking scans, files, and checksums
INSERT INTO scan_details (scan_id, file_id, checksum_id) VALUES
(1, 1, 1),
(1, 2, 2),
(3, 3, 3);
