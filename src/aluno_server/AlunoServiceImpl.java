package aluno_server;

import comum.Aluno;
import comum.AlunoService;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class AlunoServiceImpl extends UnicastRemoteObject implements AlunoService {
    
    private List<Aluno> bdAlunos; 

    public AlunoServiceImpl() throws RemoteException {
        super();
        this.bdAlunos = new ArrayList<>();
    }

    @Override
    public void cadastrarAluno(Aluno aluno) throws RemoteException {
        bdAlunos.add(aluno);
        System.out.println("[AlunoService] Novo aluno cadastrado: " + aluno.getNome());
    }

    @Override
    public Aluno consultarAluno(String matricula) throws RemoteException {
        for (Aluno a : bdAlunos) {
            if (a.getMatricula().equals(matricula)) return a;
        }
        return null;
    }

    @Override
    public List<Aluno> listarAlunos() throws RemoteException {
        return bdAlunos;
    }

    @Override
    public void removerAluno(String matricula) throws RemoteException {
        bdAlunos.removeIf(a -> a.getMatricula().equals(matricula));
    }
}