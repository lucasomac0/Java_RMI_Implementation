package sca;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RunTurma {
    public static void main(String[] args) throws Exception {
        Rede.configurar();
        int porta = Portas.turma();
        Registry registry = LocateRegistry.createRegistry(porta, null, Rede.SSF);
        registry.rebind("TurmaService", new TurmaServiceImpl());
        System.out.println("[RunTurma] TurmaService em " + Rede.HOST + ":" + porta);
    }
}