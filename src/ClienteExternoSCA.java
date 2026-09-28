package sca;

import java.net.URI;
import java.net.URL;
import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

// java -cp bin sca.ClienteExternoSCA 192.168.0.10

public class ClienteExternoSCA {

    public static void main(String[] args) {

        String hostGateway = (args.length > 0) ? args[0] : "localhost";

        try {
            URL url = URI.create("http://" + hostGateway + ":9876/WSSCA?wsdl").toURL();

            // Nome do serviço = <NomeDaImpl>Service ; namespace = pacote invertido.
            // Pacote "sca" (um único segmento) vira "http://sca/".
            QName qname = new QName("http://sca/", "WSServerImplService");

            Service service = Service.create(url, qname);
            WSServer wsca = service.getPort(WSServer.class);

            System.out.println("Conectado ao WSSCA em " + hostGateway + ":9876 (sem tocar em RMI)\n");

            wsca.cadastrarAluno(new Aluno("Cliente Externo", "cliente@ws.com", "5551000"));
            wsca.cadastrarTurma(new Turma("WS01"));

            System.out.println("Solicitando matrícula via WSSCA...");
            wsca.matricularAluno("5551000", "WS01");

            List<Aluno> alunosDaTurma = wsca.listarAlunos("WS01");
            System.out.println("Alunos na turma WS01: " + alunosDaTurma.size());
            for (Aluno a : alunosDaTurma) {
                System.out.println(" - " + a);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
