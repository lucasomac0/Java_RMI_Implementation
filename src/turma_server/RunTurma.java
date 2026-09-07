package turma_server;


import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class RunTurma {
    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1100);
            Naming.rebind("rmi://localhost:1100/TurmaService", new TurmaServiceImpl());
            System.out.println("Servidor de TURMAS rodando na porta 1100...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}