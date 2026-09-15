import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class AlunoServiceImpl extends UnicastRemoteObject implements AlunoService {
    
    private List<Aluno> bdAlunos; 
    private final String ARQUIVO_CSV = "alunos.csv";

    public AlunoServiceImpl() throws RemoteException {
        super();
        this.bdAlunos = new ArrayList<>();
        carregarDoCSV();
    }

    private void carregarDoCSV() {
        File arquivo = new File(ARQUIVO_CSV);
        if (!arquivo.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(";");
                if (dados.length == 3) {
                    bdAlunos.add(new Aluno(dados[0], dados[1], dados[2]));
                }
            }
            System.out.println("[AlunoService] " + bdAlunos.size() + " alunos carregados do CSV.");
        } catch (Exception e) {
            System.err.println("Erro ao ler o CSV: " + e.getMessage());
        }
    }

    private void salvarNoCSV(Aluno aluno) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARQUIVO_CSV, true))) {
            pw.println(aluno.getNome() + ";" + aluno.getEmail() + ";" + aluno.getMatricula());
        } catch (Exception e) {
            System.err.println("Erro ao salvar no CSV: " + e.getMessage());
        }
    }

    @Override
    public void cadastrarAluno(Aluno aluno) throws RemoteException {
        bdAlunos.add(aluno);
        salvarNoCSV(aluno);
        System.out.println("[AlunoService] Novo aluno salvo: " + aluno.getNome());
    }

    @Override
    public Aluno consultarAluno(String matricula) throws RemoteException {
        for (Aluno a : bdAlunos) {
            if (a.getMatricula().equals(matricula)) return a;
        }
        return null;
    }

    @Override
    public List<Aluno> listarAlunos() throws RemoteException {
        return bdAlunos;
    }

    @Override
    public void removerAluno(String matricula) throws RemoteException {
        bdAlunos.removeIf(a -> a.getMatricula().equals(matricula));
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARQUIVO_CSV, false))) {
            for (Aluno a : bdAlunos) {
                pw.println(a.getNome() + ";" + a.getEmail() + ";" + a.getMatricula());
            }
        } catch (Exception e) {}
    }
}