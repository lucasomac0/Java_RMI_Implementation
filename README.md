# Sistema Acadêmico Distribuído (Mini-SIGAA) via Java RMI

Este projeto implementa uma arquitetura distribuída fortemente acoplada para gerenciar operações acadêmicas (alunos, turmas e matrículas) utilizando **Java RMI (Remote Method Invocation)**. O sistema foi projetado para a disciplina de Sistemas Distribuídos, separando responsabilidades lógicas em servidores independentes que se comunicam de forma síncrona pela rede.

## Arquitetura do Sistema

O sistema adota o padrão de **Microsserviços com Orquestrador**, composto por quatro nós principais:

1. **Servidor de Alunos (Porta 1099):** Mantém o registro e controle dos estudantes. Expõe métodos para cadastrar, consultar, listar e remover alunos.
2. **Servidor de Turmas (Porta 1100):** Mantém o registro das disciplinas/turmas e a lista de matrículas de cada uma.
3. **Servidor da Secretaria (Porta 1101):** Atua como o **Orquestrador (BFF)**. Não possui persistência própria. Ao receber uma requisição de matrícula do cliente, a Secretaria consome o `AlunoService` para validar a existência do aluno e, em seguida, consome o `TurmaService` para efetivar a inserção. 
4. **Cliente Terminal:** Interface de linha de comando que consome estritamente o serviço da Secretaria para disparar as operações do usuário final.

---

## Estrutura de Diretórios

    projeto_sigaa_rmi/
    ├── Makefile
    ├── src/
    │   ├── comum/                     # Interfaces RMI e Modelos (Serializable)
    │   │   ├── Aluno.java             
    │   │   ├── Turma.java             
    │   │   ├── AlunoService.java      
    │   │   ├── TurmaService.java      
    │   │   └── SecretariaService.java 
    │   ├── aluno_server/              # Lógica e Boot do Servidor 1
    │   │   ├── AlunoServiceImpl.java
    │   │   └── RunAluno.java
    │   ├── turma_server/              # Lógica e Boot do Servidor 2
    │   │   ├── TurmaServiceImpl.java
    │   │   └── RunTurma.java
    │   ├── secretaria_server/         # Lógica e Boot do Servidor 3 (Orquestrador)
    │   │   ├── SecretariaServiceImpl.java
    │   │   └── RunSecretaria.java
    │   └── cliente/                   # Interface do Usuário Final
    │       └── ClienteMain.java

---

## Como Compilar e Executar

O projeto utiliza um `Makefile` para automatizar a compilação e a inicialização dos nós distribuídos, mantendo a estrutura de pacotes intacta na pasta `bin/`.

### 1. Compilação Limpa
Abra o terminal na raiz do projeto e limpe/recompile os binários:

    make clean
    make compile


### 2. Inicialização dos Nós (Servidores)
Como o sistema é distribuído, cada serviço representa um processo independente. Abra **quatro abas distintas no terminal** e inicie os processos na seguinte ordem:

**Aba 1 (Serviço de Alunos):**

    make run-aluno


**Aba 2 (Serviço de Turmas):**

    make run-turma


**Aba 3 (Serviço de Secretaria):**

    make run-secretaria


### 3. Execução do Cliente
Com a infraestrutura no ar, inicie a interface de usuário na quarta aba:

    make run-cliente


---

## Detalhes de Implementação (RMI)
* **Serialização:** Entidades de domínio (`Aluno` e `Turma`) implementam `java.io.Serializable` para permitir a serialização em bytes e tráfego através de sockets RMI subjacentes.
* **UnicastRemoteObject:** Todas as classes `ServiceImpl` herdam de `UnicastRemoteObject`, instruindo a JVM a exportar as instâncias para receber chamadas remotas na porta de registro configurada via `LocateRegistry`.
* **Desacoplamento de Interface:** O Cliente interage apenas com a pasta `comum/` (contratos). Ele não possui conhecimento da estrutura de dados ou implementação (`.csv` ou memória) contida nas classes `ServiceImpl`.

---

<b>Alunos:</b> Lucas Oliveira Macedo e Matheus Azevedo de Sá