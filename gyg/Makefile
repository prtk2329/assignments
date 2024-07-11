# Variables
DOCKER_COMPOSE := docker compose

.PHONY: up down clean
DEFAULT_TARGET: up

.DEFAULT_GOAL := help

help: ## List and show descriptions for make targets
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}'

up: ## Run targets
	@echo "Starting the application using Docker Compose..."
	$(DOCKER_COMPOSE) up --build -d

down: ## Stop targets
	@echo "Stopping the application using Docker Compose..."
	$(DOCKER_COMPOSE) down

clean: down ## Clean target
	@echo "Removing Docker containers and volumes..."
	$(DOCKER_COMPOSE) down --volumes --remove-orphans
