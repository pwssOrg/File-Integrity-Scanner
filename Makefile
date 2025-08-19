# Define the pom directory path
POM_DIR := File-Integrity-Scanner

# Default Target - nothing to do here, but it helps users
.PHONY: all
all:
	@echo "Please use 'make build' to build the project."

# Build Target
.PHONY: build
build:
	cd $(POM_DIR) && mvn clean install

# Test Target
.PHONY: test
test:
	cd $(POM_DIR) && mvn clean test

# Clean Target
.PHONY: clean
clean:
	cd $(POM_DIR) && mvn clean

# Start Target
.PHONY: start
start:
	cd $(POM_DIR) && mvn spring-boot:run

# Create Artifact
.PHONY: artifact
artifact: build
	cd $(POM_DIR) && mvn clean package

# Help target
.PHONY: help
help:
	@echo "Available targets:"
	@echo "  make build	  - Build the project using Maven"
	@echo "  make test	  - Test the project using Maven"
	@echo "  make clean	  - Clean the project using Maven"
	@echo "  make start	  - Start the project using Maven"
	@echo "  make artifact  - Create FIS artifact using Maven"
	@echo "  make help	  - Display this help message"