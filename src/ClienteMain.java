import java.rmi.Naming;

// java -cp bin ClienteMain 192.168.0.20

public class ClienteMain {
    public static void main(String[] args) {
        String ipBase = (args.length > 0) ? args[0] : "localhost";

        try {
            AlunoService alunoService = (AlunoService) Naming.lookup("rmi://" + ipBase + ":1099/AlunoService");
            TurmaService turmaService = (TurmaService) Naming.lookup("rmi://" + ipBase + ":1100/TurmaService");
            SecretariaService secretaria = (SecretariaService) Naming.lookup("rmi://" + ipBase + ":1101/SecretariaService");
            
            System.out.println("Conectado aos servidores!\n");

            alunoService.cadastrarAluno(new Aluno("Teste", "teste@email.com", "2023001"));
            turmaService.cadastrarTurma(new Turma("COMP01"));

            alunoService.cadastrarAluno(new Aluno("AAA", "teste@email.com", "1234567"));

            System.out.println("\nSolicitando Matrícula...");
            secretaria.matricularAluno("2023001", "COMP01");

            System.out.println("\nSolicitando Matrícula...");
            secretaria.matricularAluno("1234567", "COMP01");

            System.out.println("\nQuantidade de alunos na COMP01: " + turmaService.listarAlunos("COMP01").size());
        } catch (Exception e) { e.printStackTrace(); }
    }
}