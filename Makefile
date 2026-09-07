JAVAC = javac
JAVA = java
SRC_DIR = src
BIN_DIR = bin

# Encontra todos os arquivos .java dentro da pasta src e subpastas
SOURCES = $(shell find $(SRC_DIR) -name "*.java")

all: compile

# Cria a pasta bin e compila os arquivos mantendo a estrutura de pacotes
compile:
	@mkdir -p $(BIN_DIR)
	$(JAVAC) -d $(BIN_DIR) $(SOURCES)
	@echo "Compilação concluída com sucesso na pasta $(BIN_DIR)/"

# Regras para rodar cada parte do sistema distribuído
run-aluno: compile
	$(JAVA) -cp $(BIN_DIR) aluno_server.RunAluno

run-turma: compile
	$(JAVA) -cp $(BIN_DIR) turma_server.RunTurma

run-secretaria: compile
	$(JAVA) -cp $(BIN_DIR) secretaria_server.RunSecretaria

run-cliente: compile
	$(JAVA) -cp $(BIN_DIR) cliente.ClienteMain

# Limpar os compilados
clean:
	rm -rf $(BIN_DIR)
	@echo "Pasta $(BIN_DIR) removida."