# File-Integrity Scanner

## Overview

The **File-Integrity Scanner** is a powerful tool designed to ensure the integrity of files by using cryptographic
hash functions. This application provides peace of mind that files have not been tampered with, which is crucial
for security and data verification purposes.

## What is a File Integrity Scanner?

A file integrity scanner is a software utility that computes cryptographic hashes of files and monitors them for
changes. This process helps in detecting unauthorized modifications or corruption of critical files on a local
machine.

## Key Features

- **Hashing Algorithms:** Supports three different hashing algorithms:
  - SHA-256
  - SHA-3 (256-bit)
  - BLAKE_2b (512-bit)

- **Web Interface / GUI:** A Spring application with a user-friendly web interface where you can start and stop
scans, as well as get live information about the current integrity status of your files.

- **Database Storage:** Uses PostgreSQL to store file hashes along with the date of the scan and other relevant
file metadata.

- **Local Operation:** The scanner runs exclusively on the user's local machine. No remote services are required
or desired, ensuring full control over data integrity for the end-user.

- **No Spyware or Tracking:** The default version contains no spyware or tracking software.

![File Integrity Scanner Image](https://github.com/pwssOrg/File-Integrity-Scanner/blob/master/.github/assets/images/640x486.jpg?raw=true)

---

## Basic Setup Instructions

### Requirements

- **PostgreSQL 17.5**
- **Spring 4.0.0-M1**

### Steps

1. Ensure you have PostgreSQL and Spring installed on your system.
2. Access the database script from the dedicated PWSS repository.
3. Use provided Make scripts to start both the database and Spring application.

**Note:** You need access to the PWSS libraries stored in a private GitHub packages repository, which is
restricted to board members or team members of the PWSS organization.

## Workflow Badges

[![Build Scan](https://github.com/pwssOrg/File-Integrity-Scanner/actions/workflows/build.yml/badge.svg)](https://github.com/pwssOrg/File-Integrity-Scanner/actions/workflows/build.yml)
[![SCA Scan - File Integrity Scanner](https://github.com/pwssOrg/File-Integrity-Scanner/actions/workflows/snyk-scan.yml/badge.svg)](https://github.com/pwssOrg/File-Integrity-Scanner/actions/workflows/snyk-scan.yml)

## Contact Information

For any questions or support, please reach out to:

	@pwgit-create	Peter pwgit-create
	@lilstiffy	Stefan lilstiffy
### Discussion Forum

Please visit our discussion forum for project-related documentation and discussions: [Project Discussion
Forum](https://github.com/orgs/pwssOrg/discussions/categories/file-integrity-scanner)
