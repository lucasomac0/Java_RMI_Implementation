package sca;

import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

public class WSSCAClient {

	public static void main(String[] args) {

		String host = (args.length < 1) ? "localhost" : args[0];

		try {
			URL url = new URL("http://" + host + ":9876/WSSCA?wsdl");
			QName qname = new QName("http://sca/", "WSSCAServerImplService");

			Service service = Service.create(url, qname);
			WSSCAServer server = service.getPort(WSSCAServer.class);

			System.out.println("== Cadastrando alunos");
			server.cadastrarAluno(new Aluno("Joao", "joao@udesc.br", "123"));
			server.cadastrarAluno(new Aluno("Maria", "maria@udesc.br", "456"));
			for (Aluno a : server.listarAlunos()) {
				System.out.println(a);
			}

			System.out.println("\n== Cadastrando turmas");
			server.cadastrarTurma(new TurmaWS("SDI"));
			server.cadastrarTurma(new TurmaWS("BAN"));

			System.out.println("\n== Matriculando Joao em SDI e Maria em BAN");
			server.matricularAluno("123", "SDI");
			server.matricularAluno("456", "BAN");

			System.out.println("\n== Turmas com os alunos");
			for (TurmaWS t : server.listarTurmas()) {
				System.out.println(t);
			}

			System.out.println("\n== Apagando");
			server.cancelarMatricula("123", "SDI");
			server.cancelarMatricula("456", "BAN");
			server.removerTurma("SDI");
			server.removerTurma("BAN");
			server.removerAluno("123");
			server.removerAluno("456");
			System.out.println("Turmas: " + server.listarTurmas());
			System.out.println("Alunos: " + server.listarAlunos());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}