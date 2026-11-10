package sca;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RunAluno {
    public static void main(String[] args) throws Exception {
        Rede.configurar();
        int porta = Portas.aluno();
        Registry registry = LocateRegistry.createRegistry(porta, null, Rede.SSF);
        registry.rebind("AlunoService", new AlunoServiceImpl());
        System.out.println("[RunAluno] AlunoService em " + Rede.HOST + ":" + porta);
    }
}