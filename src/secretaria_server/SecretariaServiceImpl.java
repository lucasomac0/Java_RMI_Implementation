package secretaria_server;

import comum.Aluno;
import comum.AlunoService;
import comum.SecretariaService;
import comum.TurmaService;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class SecretariaServiceImpl extends UnicastRemoteObject implements SecretariaService {

    private AlunoService alunoService;
    private TurmaService turmaService;

    public SecretariaServiceImpl() throws RemoteException {
        super();
        try {
            // A Secretaria procura os outros servidores na rede
            // Portas 1099 e 1100.
            alunoService = (AlunoService) Naming.lookup("rmi://localhost:1099/AlunoService");
            turmaService = (TurmaService) Naming.lookup("rmi://localhost:1100/TurmaService");
        } catch (Exception e) {
            System.err.println("Erro ao conectar Secretaria com Aluno/Turma: " + e.getMessage());
        }
    }

    @Override
    public void matricularAluno(String matricula, String codigoTurma) throws RemoteException {
        // 1. Verifica no Servidor de Alunos se ele existe
        Aluno aluno = alunoService.consultarAluno(matricula);
        
        if (aluno == null) {
            System.out.println("[Secretaria] Matrícula falhou: Aluno não encontrado.");
            return;
        }

        // 2. Se existe, pede ao Servidor de Turmas para adicioná-lo
        try {
            turmaService.adicionarAluno(codigoTurma, matricula);
            System.out.println("[Secretaria] Aluno " + matricula + " matriculado na turma " + codigoTurma);
        } catch (Exception e) {
            System.out.println("[Secretaria] Erro ao matricular na turma.");
        }
    }

    @Override
    public void cancelarMatricula(String matricula, String codigoTurma) throws RemoteException {
        turmaService.removerAluno(codigoTurma, matricula);
        System.out.println("[Secretaria] Matrícula cancelada.");
    }
}