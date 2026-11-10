package sca;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.server.RMISocketFactory;
import javax.xml.ws.Endpoint;

public class WSSCAServerPublisher {

	public static void main(String[] args) throws Exception {
		String padrao = Rede.destinoPadrao();
		String ipAluno = args.length > 0 ? args[0] : padrao;
		String ipTurma = args.length > 1 ? args[1] : ipAluno;
		String ipSecretaria = args.length > 2 ? args[2] : ipAluno;

		RMISocketFactory.setSocketFactory(new RMISocketFactory() {
			@Override
			public Socket createSocket(String host, int port) throws IOException {
				Socket s = new Socket();
				s.connect(new InetSocketAddress(host, port), 3000);
				s.setSoTimeout(10000);
				return s;
			}

			@Override
			public ServerSocket createServerSocket(int port) throws IOException {
				return new ServerSocket(port);
			}
		});

		String host = System.getProperty("ws.host", Rede.HOST);
		String porta = System.getProperty("ws.porta", "9876");

		Endpoint.publish("http://" + host + ":" + porta + "/WSSCA",
				new WSSCAServerImpl(ipAluno, ipTurma, ipSecretaria));

		System.out.println("[WSSCA] Publicado em http://" + host + ":" + porta + "/WSSCA (WSDL em ?wsdl)");
		System.out.println("[WSSCA] RMI: aluno=" + ipAluno + ", turma=" + ipTurma
				+ ", secretaria=" + ipSecretaria);
	}
}