package sca;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.server.RMISocketFactory;
import javax.xml.ws.Endpoint;

public class WSSCAServerPublisher {

	public static void main(String[] args) throws Exception {
		String ipAluno = args.length > 0 ? args[0] : "localhost";
		String ipTurma = args.length > 1 ? args[1] : ipAluno;
		String ipSecretaria = args.length > 2 ? args[2] : ipAluno;

		// Timeouts nas conexões RMI do gateway: serviço fora do ar ou travado
		// vira erro em poucos segundos em vez de bloquear o cliente para sempre.
		RMISocketFactory.setSocketFactory(new RMISocketFactory() {
			@Override
			public Socket createSocket(String host, int port) throws IOException {
				Socket s = new Socket();
				s.connect(new InetSocketAddress(host, port), 3000); // conexão: 3 s
				s.setSoTimeout(10000);                              // resposta: 10 s
				return s;
			}

			@Override
			public ServerSocket createServerSocket(int port) throws IOException {
				return new ServerSocket(port);
			}
		});

		Endpoint.publish("http://0.0.0.0:9876/WSSCA",
				new WSSCAServerImpl(ipAluno, ipTurma, ipSecretaria));

		System.out.println("[WSSCA] Publicado em http://0.0.0.0:9876/WSSCA (WSDL em ?wsdl)");
		System.out.println("[WSSCA] RMI: aluno=" + ipAluno + ":1099, turma=" + ipTurma
				+ ":1100, secretaria=" + ipSecretaria + ":1101");
	}
}