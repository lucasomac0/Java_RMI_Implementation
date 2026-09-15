import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

// java -cp bin RunSecretaria 192.168.0.10 192.168.0.15

public class RunSecretaria {
    public static void main(String[] args) {
        // Pega os IPs dos argumentos ou usa localhost como padrão
        String ipAluno = (args.length > 0) ? args[0] : "localhost";
        String ipTurma = (args.length > 1) ? args[1] : "localhost";

        try {
            LocateRegistry.createRegistry(1101);
            Naming.rebind("rmi://localhost:1101/SecretariaService", new SecretariaServiceImpl(ipAluno, ipTurma));
            System.out.println("Servidor da SECRETARIA rodando na porta 1101...");
            System.out.println("-> Apontando para Aluno em: " + ipAluno);
            System.out.println("-> Apontando para Turma em: " + ipTurma);
        } catch (Exception e) { e.printStackTrace(); }
    }
}