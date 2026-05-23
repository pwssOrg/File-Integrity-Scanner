# File-Integrity Scanner Backend (FIM Engine)
This repository contains the backend service for the File Integrity Scanner system. It provides file integrity verification using cryptographic hash functions and detects unauthorized modifications and file corruption, ensuring reliable integrity checks for local systems.

## What is a File Integrity Scanner?
A file integrity scanner computes cryptographic hashes of files and monitors them for changes over time. It detects tampering, corruption, or unauthorized modifications to critical system and user files.

## What is a Cryptographic Hash?
A cryptographic hash is a one-way mathematical function that converts data into a fixed-size value known as a hash or digest. Even the smallest change to a file results in a completely different hash value, making hashes useful for integrity verification and tamper detection.

## Key Features
Different hashing algorithms are supported to allow flexibility between performance and cryptographic strength depending on use case requirements.

- **Hashing Algorithms:**
  - SHA-256
  - SHA-3 (256-bit)
  - BLAKE2b (512-bit)

- **Database Storage:** Uses PostgreSQL to store file hashes along with the date of the scan and other relevant file metadata.

- **Local Operation:** The scanner runs exclusively on the user's local machine. No remote services are required, ensuring full local control over data and integrity verification.


![File Integrity Scanner Image](https://github.com/pwssOrg/File-Integrity-Scanner/blob/master/.github/assets/images/640x486.jpg?raw=true)

---

🛡️ **Zero spyware. Zero tracking. Full respect for your privacy.**

## Basic Setup Instructions (for developers)

### Requirements

- **PostgreSQL**
- **SSL password**


### Technology Stack

- Spring Framework 4.0.6
- PostgreSQL

### Steps

1. Install and configure PostgreSQL on your system.
2. Get the database scripts from the [PWSS DB repository](https://github.com/pwssOrg/File-Integrity-Database).
3. Start the database and the Spring application using the provided Make scripts.

## Workflow Badges

[![Build Scan](https://github.com/pwssOrg/File-Integrity-Scanner/actions/workflows/build.yml/badge.svg)](https://github.com/pwssOrg/File-Integrity-Scanner/actions/workflows/build.yml)
[![SCA Scan - File Integrity Scanner](https://github.com/pwssOrg/File-Integrity-Scanner/actions/workflows/snyk-scan.yml/badge.svg)](https://github.com/pwssOrg/File-Integrity-Scanner/actions/workflows/snyk-scan.yml)

## Contact Information

For questions, support, or contributions:

- **Peter** — [@pwgit-create](https://github.com/pwgit-create)
- **Stefan** — [@lilstiffy](https://github.com/lilstiffy)

### Discussion Forum

Please visit our discussion forum for project-related documentation and discussions: [Project Discussion
Forum](https://github.com/orgs/pwssOrg/discussions/categories/file-integrity-scanner)

---

## Related Repositories

### [PWSS Release Repository](https://github.com/pwssOrg/PWSS-Release-File-Integrity-Scanner)

User-focused distribution of the Integrity Hash platform for Windows and Linux systems.

Designed for non-developers and system administrators who want a simplified installation and local file integrity monitoring experience without manually configuring backend services.
<p align="center">
  <a href="https://youtu.be/DcZYuQVOpCQ">
    <img src="https://img.youtube.com/vi/DcZYuQVOpCQ/maxresdefault.jpg" alt="Titta på videon" width="100%" max-width="600px">
  </a>
</p>
Features:

* Easy setup
* Local-only operation
* PostgreSQL integration
* Multi-algorithm hashing support
* Privacy-focused design
---

## System Architecture

The system is split into backend services, a GUI client, shared PWSS libraries, and an end-user distribution package. This modular architecture enables independent development of core security logic, user interface components, and deployment tooling for both technical and non-technical users. Each component can be developed and deployed independently while maintaining a shared security and hashing standard through the PWSS libraries.

### Components

- **Core Backend (FIM Engine)** – Handles hashing, integrity verification, and monitoring logic  
- **GUI Application** – User interface for managing scans and viewing results  
- **PWSS Libraries** – Shared components used across all PWSS projects  
- **PWSS Release Repository** – End-user distribution for Windows and Linux

### Architecture diagram
This repository represents the backend layer of the File Integrity Scanner system and implements the core FIM engine.
```
GUI → Local Backend → PostgreSQL
          ↓
   PWSS Libraries (dependency)
```
