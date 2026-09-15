# Sistema Acadêmico Distribuído (Mini-SIGAA) via Java RMI

**Autores:** Lucas Macedo e Matheus Sá <br>
**Disciplina:** Sistemas Distribuídos 

Este projeto implementa uma arquitetura distribuída para gerenciar operações acadêmicas (alunos, turmas e matrículas) utilizando **Java RMI (Remote Method Invocation)**. O sistema adota o padrão de Microsserviços com Orquestrador, garantindo forte acoplamento e comunicação síncrona, além de persistência de dados local.

## Arquitetura do Sistema

O projeto foi construído de forma modularizada em processos, porém com código-fonte achatado em um único pacote padrão para facilitar a interoperabilidade.

1. **Servidor de Alunos (Porta 1099):** Mantém o registro de estudantes. Salva e recupera dados localmente em `alunos.csv`.
2. **Servidor de Turmas (Porta 1100):** Mantém as disciplinas e matrículas associadas. Salva referências no arquivo `turmas.csv`.
3. **Servidor da Secretaria (Porta 1101):** Atua como o **Orquestrador**. Valida a existência do aluno remotamente antes de autorizar a inclusão na turma. Possui *lookup dinâmico* (conecta aos servidores base sob demanda).
4. **Cliente Terminal:** Interface de testes que dispara chamadas RMI para popular dados e efetuar matrículas.

## Estrutura de Diretórios

    projeto_sigaa_rmi/
    ├── Makefile
    ├── README.md
    └── src/
        ├── Aluno.java             # Entidade serializável
        ├── Turma.java             # Entidade serializável
        ├── AlunoService.java      # Interface remota
        ├── TurmaService.java      # Interface remota
        ├── SecretariaService.java # Interface remota
        ├── AlunoServiceImpl.java  
        ├── TurmaServiceImpl.java  
        ├── SecretariaServiceImpl.java
        ├── RunAluno.java          # Inicializador na porta 1099
        ├── RunTurma.java          # Inicializador na porta 1100
        ├── RunSecretaria.java     # Inicializador na porta 1101
        └── ClienteMain.java       # Aplicação cliente

---

## Como Compilar

O projeto utiliza um `Makefile` para automatizar a compilação, isolando os binários na pasta `bin/`. 

Na raiz do projeto, execute:
```bash
make clean    # Limpa artefatos antigos
make compile  # Compila todos os .java da pasta src/ para bin/
