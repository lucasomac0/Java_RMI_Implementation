package secretaria_server; 
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class RunSecretaria {
    public static void main(String[] args) {
        try {
            // Cria o servidor na porta 1101
            LocateRegistry.createRegistry(1101);
            
            Naming.rebind("rmi://localhost:1101/SecretariaService", new SecretariaServiceImpl());
            
            System.out.println("Servidor da SECRETARIA rodando na porta 1101...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}