.PHONY: drop_tables_linux tables_windows drop_tables_windows

# Linux
drop_tables_linux:
	sh drop_all_tables.sh

# Windows 
tables_windows:
	powershell -noexit "& "".\create_all_tables.ps1"""

drop_tables_windows:
	powershell -noexit "& "".\drop_all_tables.ps1"""
