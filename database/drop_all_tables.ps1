## Import PWSS Powershell utility Module

Import-Module ".\functions_ps\pwss_powershell_utility.psm1"

## Drop all tables and sequences from the file integrity scanner databse

## Check if environment variables for database user and password are set
if (-not $env:INTEGRITY_HASH_DB_USER -or -not $env:INTEGRITY_HASH_DB_PASSWORD) {
    Write-Error "Environment variables INTEGRITY_HASH_DB_USER and/or INTEGRITY_HASH_DB_PASSWORD are not set."
    exit 1
}

try {
    $DBConnectionString = "Driver={PostgreSQL UNICODE};Server=localhost;Port=26556;Database=integrity_hash;Uid=$env:INTEGRITY_HASH_DB_USER;Pwd=$env:INTEGRITY_HASH_DB_PASSWORD;"
    $DBConn = New-Object System.Data.Odbc.OdbcConnection;
    $DBConn.ConnectionString = $DBConnectionString;
    $DBConn.Open();
    $DBCmd = $DBConn.CreateCommand();
    ## Drop tables if they exist
    $DBCmd.CommandText = @"
DROP TABLE IF EXISTS checksums CASCADE;
DROP TABLE IF EXISTS files CASCADE;
DROP TABLE IF EXISTS scan_details CASCADE;
DROP TABLE IF EXISTS scans CASCADE;
DROP TABLE IF EXISTS monitored_directories CASCADE;
"@
    $rowsAffected = $DBCmd.ExecuteNonQuery();
    Write-Output "$rowsAffected rows affected by DROP statements."
    $DBConn.Close();

    Write-Output "Success! The file integrity scan database is now free of all tables and sequences." | Green
}
catch {
    Write-Output "$($_.Exception.Message)" | Red
    Write-Output "An error occurred. Contact snow_900@outlook.com or stefan-201@hotmail.com for support!" | Red
}