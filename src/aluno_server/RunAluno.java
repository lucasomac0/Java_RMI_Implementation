package aluno_server;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class RunAluno {
    public static void main(String[] args) {
        try {
            // Cria o servidor na porta 1099
            LocateRegistry.createRegistry(1099);
            Naming.rebind("rmi://localhost:1099/AlunoService", new AlunoServiceImpl());
            System.out.println("Servidor de ALUNOS rodando na porta 1099...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}