package cliente;

import comum.SecretariaService;
import java.rmi.Naming;

public class ClienteMain {
    public static void main(String[] args) {
        try {
            // Conecta na porta 1101 da Secretaria
            SecretariaService secretaria = (SecretariaService) Naming.lookup("rmi://localhost:1101/SecretariaService");

            System.out.println("Conectado ao SIGAA Distribudo!");
            
            // Exemplo hardcoded por enquanto

            secretaria.matricularAluno("2023001", "COMP01");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}