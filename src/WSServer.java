package sca;

import java.rmi.RemoteException;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * Endpoint: WSSCA
 *
 * Contrato do gateway. Cada método aqui espelha, propositalmente,
 * um método de AlunoService, TurmaService ou SecretariaService — mesmo
 * nome, mesmos parâmetros, mesmo retorno, mesmo "throws RemoteException".
 * A ideia é que consumir o sistema via WSSCA seja equivalente a consumir
 * via RMI direto; só muda o transporte (SOAP/HTTP em vez de RMI).
 *
 * Como três interfaces diferentes têm métodos com o mesmo nome mas
 * assinaturas diferentes (ex.: removerAluno(String) vs.
 * removerAluno(String, String)), uso @WebMethod(operationName=...) só
 * para dar um nome único a cada operação no WSDL — o nome do método em
 * Java continua idêntico ao original.
 */
@WebService
public interface WSServer {

    // ---- Espelha AlunoService ----

    @WebMethod
    void cadastrarAluno(Aluno aluno) throws RemoteException;

    @WebMethod
    Aluno consultarAluno(String matricula) throws RemoteException;

    @WebMethod(operationName = "listarTodosAlunos")
    @RequestWrapper(localName = "listarTodosAlunos", className = "sca.jaxws.ListarTodosAlunos")
    @ResponseWrapper(localName = "listarTodosAlunosResponse", className = "sca.jaxws.ListarTodosAlunosResponse")
    List<Aluno> listarAlunos() throws RemoteException;

    @WebMethod(operationName = "removerAlunoPorMatricula")
    @RequestWrapper(localName = "removerAlunoPorMatricula", className = "sca.jaxws.RemoverAlunoPorMatricula")
    @ResponseWrapper(localName = "removerAlunoPorMatriculaResponse", className = "sca.jaxws.RemoverAlunoPorMatriculaResponse")
    void removerAluno(String matricula) throws RemoteException;

    // ---- Espelha TurmaService ----

    @WebMethod
    void cadastrarTurma(Turma turma) throws RemoteException;

    @WebMethod
    Turma consultarTurma(String codigo) throws RemoteException;

    @WebMethod
    List<Turma> listarTurmas() throws RemoteException;

    @WebMethod
    void removerTurma(String codigo) throws RemoteException;

    @WebMethod
    void adicionarAluno(String codigoTurma, String matricula) throws RemoteException;

    @WebMethod(operationName = "removerAlunoDaTurma")
    @RequestWrapper(localName = "removerAlunoDaTurma", className = "sca.jaxws.RemoverAlunoDaTurma")
    @ResponseWrapper(localName = "removerAlunoDaTurmaResponse", className = "sca.jaxws.RemoverAlunoDaTurmaResponse")
    void removerAluno(String codigoTurma, String matricula) throws RemoteException;

    @WebMethod(operationName = "listarAlunosDaTurma")
    @RequestWrapper(localName = "listarAlunosDaTurma", className = "sca.jaxws.ListarAlunosDaTurma")
    @ResponseWrapper(localName = "listarAlunosDaTurmaResponse", className = "sca.jaxws.ListarAlunosDaTurmaResponse")
    List<Aluno> listarAlunos(String codigoTurma) throws RemoteException;

    // ---- Espelha SecretariaService ----

    @WebMethod
    void matricularAluno(String matricula, String codigoTurma) throws RemoteException;

    @WebMethod
    void cancelarMatricula(String matricula, String codigoTurma) throws RemoteException;
}