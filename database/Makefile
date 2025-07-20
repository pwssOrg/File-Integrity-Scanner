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

# Help target to display command descriptions
help:
	@echo Available targets:

	@echo   drop_tables_linux     - Drop all tables on a Linux system by executing 'drop_all_tables.sh'
	@echo   tables_windows        - Create all tables in Windows using create_all_tables.ps1 script
	@echo   tables_windows_with_mock_data - Create all tables and insert mock data in Windows
	@echo   drop_tables_windows   - Drop all tables on a Windows system by executing drop_all_tables.ps1
	@echo   help                  - Display this help message
