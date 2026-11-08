# Variáveis
JAVAC = javac
JAVA = java
SRC_DIR = sca
BIN_DIR = bin
PKG = sca

# Encontra todos os arquivos .java soltos dentro da pasta src
SOURCES = $(wildcard $(SRC_DIR)/*.java)

# Regra principal: compilar tudo
all: compile

# Cria a pasta bin (se não existir) e compila os arquivos lá dentro
compile:
	@mkdir -p $(BIN_DIR)
	$(JAVAC) -d $(BIN_DIR) $(SOURCES)
	@echo "Compilação concluída com sucesso na pasta $(BIN_DIR)/"

# Regras para rodar cada parte do sistema (já chamam o compile automaticamente)
run-aluno: compile
	$(JAVA) -cp $(BIN_DIR) $(PKG).RunAluno

run-turma: compile
	$(JAVA) -cp $(BIN_DIR) $(PKG).RunTurma

run-secretaria: compile
	$(JAVA) -cp $(BIN_DIR) $(PKG).RunSecretaria

run-cliente: compile
	$(JAVA) -cp $(BIN_DIR) $(PKG).ClienteMain

# WSSCA: publica o endpoint SOAP (Exportador WS + Cliente RMI)
run-wsca: compile
	$(JAVA) -cp $(BIN_DIR) $(PKG).WSSCAServerPublisher

# Cliente externo: só fala com o WSSCA, nunca com RMI diretamente
run-cliente-externo: compile
	$(JAVA) -cp $(BIN_DIR) $(PKG).WSSCAClient

# Comando mágico para limpar a sujeira
clean:
	rm -rf $(BIN_DIR)
	@echo "Pasta $(BIN_DIR) e arquivos .class antigos removidos."
