package sca;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.rmi.server.RMIServerSocketFactory;

public class Rede {
    private static final String TODAS = "0.0.0.0";

    // IP em que ESTE processo escuta. Sem -Dsca.host: todas as interfaces (como antes).
    public static final String HOST = System.getProperty("sca.host", TODAS);

    // Endereço padrão para falar com os outros processos da mesma pilha.
    public static String destinoPadrao() {
        return TODAS.equals(HOST) ? "localhost" : HOST;
    }

    // Chamar no início do main, antes de exportar qualquer objeto RMI:
    // os stubs passam a anunciar HOST em vez do IP que a máquina "acha" que tem.
    public static void configurar() {
        if (!TODAS.equals(HOST) && System.getProperty("java.rmi.server.hostname") == null) {
            System.setProperty("java.rmi.server.hostname", HOST);
        }
    }

    public static final RMIServerSocketFactory SSF = new RMIServerSocketFactory() {
        @Override
        public ServerSocket createServerSocket(int porta) throws IOException {
            InetAddress addr = TODAS.equals(HOST) ? null : InetAddress.getByName(HOST);
            return new ServerSocket(porta, 50, addr);
        }
    };
}