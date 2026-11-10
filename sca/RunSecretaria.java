package sca;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RunSecretaria {
    public static void main(String[] args) throws Exception {
        Rede.configurar();
        String ipAluno = args.length > 0 ? args[0] : Rede.destinoPadrao();
        String ipTurma = args.length > 1 ? args[1] : ipAluno;
        int porta = Portas.secretaria();
        Registry registry = LocateRegistry.createRegistry(porta, null, Rede.SSF);
        registry.rebind("SecretariaService", new SecretariaServiceImpl(ipAluno, ipTurma));
        System.out.println("[RunSecretaria] SecretariaService em " + Rede.HOST + ":" + porta);
    }
}