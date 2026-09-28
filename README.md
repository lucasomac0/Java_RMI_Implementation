# Sistema Acadêmico Distribuído — RMI + Gateway WS (WSSCA)

**Disciplina:** Sistemas Distribuídos

Este projeto tem duas camadas. A primeira (etapa anterior) é um sistema
acadêmico com três serviços **RMI** independentes — Aluno, Turma e
Secretaria. A segunda, adicionada nesta etapa, é o **WSSCA**: um gateway
**WS (SOAP)** que expõe esse mesmo sistema para clientes externos que não
falam RMI.

## Visão geral da arquitetura

```
Cliente externo (SOAP/HTTP)
        |
        v
   WSServerPublisher  --publica-->  WSServer / WSServerImpl   ["/WSSCA"]
        |                                  |
        |                          (Cliente RMI interno:
        |                           faz Naming.lookup sob demanda)
        v                                  v
        +--------------> AlunoService (RMI :1099)
        +--------------> TurmaService (RMI :1100)
        +--------------> SecretariaService (RMI :1101)
```

O `WSServerImpl` tem duas responsabilidades na mesma classe:

1. **Cliente RMI** — três métodos privados (`getAlunoService`,
   `getTurmaService`, `getSecretariaService`) que fazem `Naming.lookup`
   nos três servidores a cada chamada (não guardam a referência entre
   chamadas, então se um servidor reiniciar o gateway continua
   funcionando na próxima requisição).
2. **Exportador WS** — a implementação do contrato `WSServer`. Cada
   método simplesmente repassa a chamada para o serviço RMI
   correspondente e devolve o resultado. Não há lógica de negócio aqui
   — só tradução de transporte (SOAP ⇄ RMI).

O cliente externo (`ClienteExternoSCA`) só importa `WSServer`, `Aluno` e
`Turma`. Ele não sabe que RMI existe por trás.

## Estrutura dos arquivos (pacote `sca`)

| Arquivo | O que faz |
|---|---|
| `Aluno.java` | Entidade serializável (nome, email, matrícula) |
| `Turma.java` | Entidade serializável (código + lista de `Aluno`) |
| `AlunoService.java` / `TurmaService.java` / `SecretariaService.java` | Interfaces remotas RMI — **contrato não alterado**, só receberam o pacote `sca` |
| `AlunoServiceImpl.java` / `TurmaServiceImpl.java` / `SecretariaServiceImpl.java` | Implementações RMI com persistência em CSV local |
| `RunAluno.java` / `RunTurma.java` / `RunSecretaria.java` | Sobem os registries RMI nas portas 1099 / 1100 / 1101 |
| `ClienteMain.java` | Cliente RMI direto (trabalho anterior, sem passar pelo WSSCA) |
| `WSServer.java` | Interface do gateway (o *SEI* — Service Endpoint Interface) |
| `WSServerImpl.java` | Implementação do gateway: cliente RMI + exportador WS |
| `WSServerPublisher.java` | Publica o endpoint em `http://<host>:9876/WSSCA` |
| `ClienteExternoSCA.java` | Cliente externo — só fala com o WSSCA, nunca com RMI |
| `Makefile` | Automatiza compilação e execução de cada peça |

## Decisões técnicas (e os problemas que elas resolveram)

**Por que tudo está no pacote `sca`, inclusive as interfaces RMI?**
O JAX-WS proíbe que a classe do serviço WS fique no pacote padrão (sem
`package` declarado). E uma classe de pacote nomeado não consegue
importar uma classe do pacote padrão em Java — não tem como contornar
isso sem reflection. Por isso todo o projeto foi movido para `sca`;
o *contrato* das interfaces RMI (nomes, parâmetros, retornos) não mudou
em nada, só o pacote.

**Por que `WSServer` espelha as 3 interfaces RMI método a método?**
Requisito do trabalho: "acesso idêntico às interfaces existentes". Cada
método de `WSServer` tem o mesmo nome, parâmetros e retorno do método
RMI correspondente (ex.: `cadastrarAluno(Aluno aluno)`, não campos
soltos). `WSServerImpl` só repassa a chamada, sem decidir nada.

**Por que não usei `@SOAPBinding(style = Style.RPC)` (como no template
original)?** O estilo RPC, na implementação de JAX-WS que vem embutida
no JDK, não lida bem com `List<T>` como parâmetro/retorno — a
publicação falhava com `java.util.List is an interface, and JAXB can't
handle interfaces`. O estilo padrão (DOCUMENT/WRAPPED, que é o que fica
quando você **não** anota `@SOAPBinding`) resolve isso.

**Por que `@RequestWrapper`/`@ResponseWrapper` em alguns métodos?**
Três interfaces diferentes têm métodos com o mesmo nome, mas assinaturas
diferentes: `removerAluno(String)` × `removerAluno(String, String)`, e
`listarAlunos()` × `listarAlunos(String)`. Isso é sobrecarga válida em
Java, mas o JAX-WS gera uma classe interna de empacotamento (JAXB) a
partir do **nome do método**, não do `operationName` — então as duas
versões de `removerAluno` colidiam na mesma classe gerada e a
publicação falhava (`do not have a property of the name arg1`). A
correção foi nomear explicitamente a classe gerada para cada sobrecarga
com `@RequestWrapper`/`@ResponseWrapper`, uma por método.

## Pré-requisito: JDK 8 (e por que precisa dela)

O `javax.jws` (anotações como `@WebService`) e o `javax.xml.ws`
(`Endpoint`, `Service`) faziam parte do Java EE, que vinha embutido no
JDK até a versão 10. A partir do **Java 11**, esses módulos foram
removidos do JDK — por isso, numa JDK 17/21 (comum hoje em dia), o
projeto não compila (`package javax.jws does not exist`). A JDK 8 ainda
traz essas classes internamente (implementação Metro/`com.sun.xml.internal.ws`),
então é o caminho mais rápido: nenhuma dependência externa para baixar,
só usar essa versão do compilador e da JVM.

**Instalar (uma vez):**
```bash
sudo apt update && sudo apt install -y openjdk-8-jdk-headless
```
Se o pacote não existir no seu Ubuntu, baixe direto (sem precisar de `sudo`):
```bash
wget https://github.com/adoptium/temurin8-binaries/releases/latest/download/OpenJDK8U-jdk_x64_linux_hotspot.tar.gz
mkdir -p ~/jdk8 && tar -xzf OpenJDK8U-jdk_x64_linux_hotspot.tar.gz -C ~/jdk8 --strip-components=1
```

**Apontar o terminal para ela (repetir em CADA terminal novo que for
compilar/rodar o projeto):**
```bash
export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64   # ou: export JAVA_HOME=~/jdk8
export PATH=$JAVA_HOME/bin:$PATH
java -version   # tem que mostrar 1.8.0...
```
Dica: se não quiser repetir isso toda vez, adicione essas duas linhas de
`export` no final do seu `~/.bashrc`.

## Como compilar e rodar

Com a JDK 8 ativa no terminal (passo acima), em **5 terminais separados**,
todos dentro da pasta do projeto:

```bash
# Terminal 1
make run-aluno

# Terminal 2
make run-turma

# Terminal 3
make run-secretaria

# Terminal 4 — sobe o gateway WSSCA (precisa dos 3 anteriores no ar)
make run-wsca

# Terminal 5 — cliente externo, só fala com o WSSCA
make run-cliente-externo
```

Outros alvos disponíveis: `make run-cliente` (cliente RMI direto, sem
passar pelo gateway) e `make clean` (apaga a pasta `bin/`).

## Como confirmar que está tudo funcionando

1. Depois do `make run-wsca`, abra `http://localhost:9876/WSSCA?wsdl`
   no navegador — deve aparecer o XML do WSDL (contrato do serviço).
2. Rode `make run-cliente-externo`. A saída esperada é algo como:
   ```
   Conectado ao WSSCA em localhost:9876 (sem tocar em RMI)
   Solicitando matrícula via WSSCA...
   Alunos na turma WS01: 1
    - Aluno: Cliente Externo | Email: cliente@ws.com | Matrícula: 5551000
   ```
3. Nos terminais 1, 2 e 3 (Aluno, Turma, Secretaria) devem aparecer logs
   das chamadas RMI recebidas — prova de que o WSSCA está de fato
   repassando as chamadas, e não simulando os dados.

## Limitações conhecidas

- As anotações JAX-WS usadas (`javax.jws.*`, `javax.xml.ws.*`) só
  existem embutidas até o Java 10. Rodando em Java 11+ seria necessário
  adicionar as bibliotecas do Jakarta XML Web Services / JAX-WS RI
  manualmente ao classpath.
- O gateway publica em `0.0.0.0:9876`, aceitando conexões de qualquer
  interface de rede da máquina — ajuste o endereço em
  `WSServerPublisher.java` se quiser restringir isso.