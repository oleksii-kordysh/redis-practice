# Makefile for running k6 load tests

BASE_URL ?= http://localhost:8080
CACHE_MODE ?= MANUAL

.PHONY: help
help:
	@echo "Commands for running k6 tests:"
	@echo "  make post      - Product creation test"
	@echo "  make get       - Get product by ID test"
	@echo "  make put       - Product update test"
	@echo "  make delete    - Product deletion test"
	@echo "  make all       - Run all tests"
	@echo ""
	@echo "Settings:"
	@echo "  BASE_URL       - Server URL (default: http://localhost:8080)"
	@echo "  CACHE_MODE     - Cache mode (default: MANUAL)"
	@echo ""
	@echo "Examples:"
	@echo "  make get CACHE_MODE=NONE_CACHE"
	@echo "  make post BASE_URL=http://prod-server:8080"

# Product creation test
.PHONY: post
post:
	@echo "Starting product creation test..."
	@BASE_URL=$(BASE_URL) CACHE_MODE=$(CACHE_MODE) k6 run test-post.js

# Get product by ID test
.PHONY: get
get:
	@echo "Starting get product by ID test..."
	@BASE_URL=$(BASE_URL) CACHE_MODE=$(CACHE_MODE) k6 run test-get.js

.PHONY: get-none-cache
get-none-cache:
	@echo "Starting get product by ID test (mode=NONE_CACHE)..."
	@BASE_URL=$(BASE_URL) CACHE_MODE=NONE_CACHE k6 run test-get.js

.PHONY: get-manual-cache
get-manual-cache:
	@echo "Starting get product by ID test (mode=MANUAL)..."
	@BASE_URL=$(BASE_URL) CACHE_MODE=MANUAL k6 run test-get.js

.PHONY: get-spring-cache
get-spring-cache:
	@echo "Starting get product by ID test (mode=SPRING_CACHE)..."
	@BASE_URL=$(BASE_URL) CACHE_MODE=SPRING_CACHE k6 run test-get.js

# Product update test
.PHONY: put
put:
	@echo "Starting product update test..."
	@BASE_URL=$(BASE_URL) CACHE_MODE=$(CACHE_MODE) k6 run test-put.js

# Product deletion test
.PHONY: delete
delete:
	@echo "Starting product deletion test..."
	@BASE_URL=$(BASE_URL) CACHE_MODE=$(CACHE_MODE) k6 run test-delete.js

# Run all tests sequentially
.PHONY: all
all: post get put delete
	@echo "All tests completed!"