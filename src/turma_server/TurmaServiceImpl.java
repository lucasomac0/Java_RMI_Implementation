package turma_server;

import comum.Aluno;
import comum.Turma;
import comum.TurmaService;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class TurmaServiceImpl extends UnicastRemoteObject implements TurmaService {

    // Simula o banco de dados de turmas em memória 
    private List<Turma> bdTurmas;

    public TurmaServiceImpl() throws RemoteException {
        super();
        this.bdTurmas = new ArrayList<>();
    }

    @Override
    public void cadastrarTurma(Turma turma) throws RemoteException {
        bdTurmas.add(turma);
        System.out.println("[TurmaService] Nova turma cadastrada: " + turma.getCodigo());
    }

    @Override
    public Turma consultarTurma(String codigo) throws RemoteException {
        for (Turma t : bdTurmas) {
            if (t.getCodigo().equals(codigo)) {
                return t;
            }
        }
        return null;
    }

    @Override
    public List<Turma> listarTurmas() throws RemoteException {
        return bdTurmas;
    }

    @Override
    public void removerTurma(String codigo) throws RemoteException {
        bdTurmas.removeIf(t -> t.getCodigo().equals(codigo));
        System.out.println("[TurmaService] Turma removida: " + codigo);
    }

    @Override
    public void adicionarAluno(String codigoTurma, String matricula) throws RemoteException {
        Turma turma = consultarTurma(codigoTurma);
        
        if (turma != null) {
            Aluno alunoRef = new Aluno("Nome buscado na Secretaria", "Email buscado", matricula);
            turma.adicionarAluno(alunoRef);
            System.out.println("[TurmaService] Aluno " + matricula + " adicionado à turma " + codigoTurma);
        } else {
            System.out.println("[TurmaService] Falha: Turma " + codigoTurma + " não encontrada.");
        }
    }

    @Override
    public void removerAluno(String codigoTurma, String matricula) throws RemoteException {
        Turma turma = consultarTurma(codigoTurma);
        
        if (turma != null) {
            boolean removido = turma.removerAlunoPorMatricula(matricula);
            if (removido) {
                System.out.println("[TurmaService] Aluno " + matricula + " removido da turma " + codigoTurma);
            }
        }
    }

    @Override
    public List<Aluno> listarAlunos(String codigoTurma) throws RemoteException {
        Turma turma = consultarTurma(codigoTurma);
        if (turma != null) {
            return turma.getAlunos();
        }
        return new ArrayList<>(); // Retorna vazio se a turma não existir
    }
}