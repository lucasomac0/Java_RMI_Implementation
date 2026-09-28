package sca;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.List;
import javax.jws.WebService;

/**
 * Implementação do endpoint WSSCA.
 *
 * É ao mesmo tempo:
 *  - Cliente RMI do "trabalho anterior": os métodos privados no topo
 *    fazem Naming.lookup nos três serviços (Aluno, Turma, Secretaria)
 *    a cada chamada, sem guardar a referência entre chamadas.
 *  - Exportador WS: cada método público apenas repassa a chamada para
 *    o serviço RMI correspondente e devolve o resultado (ou deixa a
 *    RemoteException propagar) — não há lógica de negócio aqui, só
 *    encaminhamento. É por isso que "o acesso fica idêntico às
 *    interfaces existentes": o WSServer não decide nada, só traduz
 *    o transporte de SOAP para RMI e vice-versa.
 */
@WebService(endpointInterface = "sca.WSServer")
public class WSServerImpl implements WSServer {

    private final String ipAluno;
    private final String ipTurma;
    private final String ipSecretaria;

    public WSServerImpl(String ipAluno, String ipTurma, String ipSecretaria) {
        this.ipAluno = ipAluno;
        this.ipTurma = ipTurma;
        this.ipSecretaria = ipSecretaria;
    }

    // Construtor de conveniência: assume os três serviços em localhost
    public WSServerImpl() {
        this("localhost", "localhost", "localhost");
    }

    // ============================================================
    // CLIENTE RMI — lookups sob demanda nos três servidores
    // ============================================================

    private AlunoService getAlunoService() throws RemoteException {
        try {
            return (AlunoService) Naming.lookup("rmi://" + ipAluno + ":1099/AlunoService");
        } catch (Exception e) {
            throw new RemoteException("Falha ao localizar AlunoService em " + ipAluno + ":1099", e);
        }
    }

    private TurmaService getTurmaService() throws RemoteException {
        try {
            return (TurmaService) Naming.lookup("rmi://" + ipTurma + ":1100/TurmaService");
        } catch (Exception e) {
            throw new RemoteException("Falha ao localizar TurmaService em " + ipTurma + ":1100", e);
        }
    }

    private SecretariaService getSecretariaService() throws RemoteException {
        try {
            return (SecretariaService) Naming.lookup("rmi://" + ipSecretaria + ":1101/SecretariaService");
        } catch (Exception e) {
            throw new RemoteException("Falha ao localizar SecretariaService em " + ipSecretaria + ":1101", e);
        }
    }

    // ============================================================
    // EXPORTADOR WS — repassa 1:1 para o serviço RMI correspondente
    // ============================================================

    @Override
    public void cadastrarAluno(Aluno aluno) throws RemoteException {
        getAlunoService().cadastrarAluno(aluno);
    }

    @Override
    public Aluno consultarAluno(String matricula) throws RemoteException {
        return getAlunoService().consultarAluno(matricula);
    }

    @Override
    public List<Aluno> listarAlunos() throws RemoteException {
        return getAlunoService().listarAlunos();
    }

    @Override
    public void removerAluno(String matricula) throws RemoteException {
        getAlunoService().removerAluno(matricula);
    }

    @Override
    public void cadastrarTurma(Turma turma) throws RemoteException {
        getTurmaService().cadastrarTurma(turma);
    }

    @Override
    public Turma consultarTurma(String codigo) throws RemoteException {
        return getTurmaService().consultarTurma(codigo);
    }

    @Override
    public List<Turma> listarTurmas() throws RemoteException {
        return getTurmaService().listarTurmas();
    }

    @Override
    public void removerTurma(String codigo) throws RemoteException {
        getTurmaService().removerTurma(codigo);
    }

    @Override
    public void adicionarAluno(String codigoTurma, String matricula) throws RemoteException {
        getTurmaService().adicionarAluno(codigoTurma, matricula);
    }

    @Override
    public void removerAluno(String codigoTurma, String matricula) throws RemoteException {
        getTurmaService().removerAluno(codigoTurma, matricula);
    }

    @Override
    public List<Aluno> listarAlunos(String codigoTurma) throws RemoteException {
        return getTurmaService().listarAlunos(codigoTurma);
    }

    @Override
    public void matricularAluno(String matricula, String codigoTurma) throws RemoteException {
        getSecretariaService().matricularAluno(matricula, codigoTurma);
    }

    @Override
    public void cancelarMatricula(String matricula, String codigoTurma) throws RemoteException {
        getSecretariaService().cancelarMatricula(matricula, codigoTurma);
    }
}
