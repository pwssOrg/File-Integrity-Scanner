.PHONY: drop_tables_linux tables_windows tables_windows_with_mock_data drop_tables_windows

# Linux
drop_tables_linux:
	sh drop_all_tables.sh

# Windows 
tables_windows:
	powershell -noexit "& "".\create_all_tables.ps1"""

tables_windows_with_mock_data:
	powershell -noexit "& "".\create_all_tables.ps1 -InsertTestData $true"""

drop_tables_windows:
	powershell -noexit "& "".\drop_all_tables.ps1"""
