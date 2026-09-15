import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class SecretariaServiceImpl extends UnicastRemoteObject implements SecretariaService {

    private String ipAluno;
    private String ipTurma;

    public SecretariaServiceImpl(String ipAluno, String ipTurma) throws RemoteException {
        super();
        this.ipAluno = ipAluno;
        this.ipTurma = ipTurma;
    }

    private AlunoService getAlunoService() throws Exception {
        return (AlunoService) Naming.lookup("rmi://" + ipAluno + ":1099/AlunoService");
    }

    private TurmaService getTurmaService() throws Exception {
        return (TurmaService) Naming.lookup("rmi://" + ipTurma + ":1100/TurmaService");
    }

    @Override
    public void matricularAluno(String matricula, String codigoTurma) throws RemoteException {
        try {
            Aluno aluno = getAlunoService().consultarAluno(matricula);
            if (aluno == null) {
                System.out.println("[Secretaria] Falha: Aluno " + matricula + " não encontrado.");
                return;
            }
            getTurmaService().adicionarAluno(codigoTurma, matricula);
            System.out.println("[Secretaria] Sucesso: Aluno " + matricula + " na turma " + codigoTurma);
        } catch (Exception e) {
            System.err.println("[Secretaria] Erro de rede: " + e.getMessage());
        }
    }

    @Override
    public void cancelarMatricula(String matricula, String codigoTurma) throws RemoteException {
        try {
            getTurmaService().removerAluno(codigoTurma, matricula);
            System.out.println("[Secretaria] Matrícula cancelada.");
        } catch (Exception e) {
            System.err.println("[Secretaria] Erro de rede: " + e.getMessage());
        }
    }
}