package sca;

import javax.xml.ws.Endpoint;

// java -cp bin sca.WSServerPublisher 192.168.0.10 192.168.0.11 192.168.0.12

public class WSServerPublisher {

    public static void main(String[] args) {
        String ipAluno = (args.length > 0) ? args[0] : "localhost";
        String ipTurma = (args.length > 1) ? args[1] : "localhost";
        String ipSecretaria = (args.length > 2) ? args[2] : "localhost";

        System.out.println("Beginning to publish WSSCA now");
        System.out.println("-> Apontando para Aluno em: " + ipAluno + ":1099");
        System.out.println("-> Apontando para Turma em: " + ipTurma + ":1100");
        System.out.println("-> Apontando para Secretaria em: " + ipSecretaria + ":1101");

        WSServerImpl gateway = new WSServerImpl(ipAluno, ipTurma, ipSecretaria);
        Endpoint.publish("http://0.0.0.0:9876/WSSCA", gateway);

        System.out.println("Done publishing. WSDL disponível em http://localhost:9876/WSSCA?wsdl");
    }
}
