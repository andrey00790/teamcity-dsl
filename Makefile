GRADLE_EXEC=$(shell which gradle || echo ${GRADLE_HOME}'/bin/gradle')

.DEFAULT_GOAL := test

test: ## This test.
	$(GRADLE_EXEC) clean test