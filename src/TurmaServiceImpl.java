import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class TurmaServiceImpl extends UnicastRemoteObject implements TurmaService {

    private List<Turma> bdTurmas;
    private final String ARQUIVO_CSV = "turmas.csv";

    public TurmaServiceImpl() throws RemoteException {
        super();
        this.bdTurmas = new ArrayList<>();
        carregarDoCSV();
    }

    private void carregarDoCSV() {
        File arquivo = new File(ARQUIVO_CSV);
        if (!arquivo.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(";");
                if (dados.length >= 1) {
                    Turma turma = new Turma(dados[0]);
                    if (dados.length == 2 && !dados[1].isEmpty()) {
                        String[] matriculas = dados[1].split(",");
                        for (String mat : matriculas) {
                            turma.adicionarAluno(new Aluno("N/A", "N/A", mat));
                        }
                    }
                    bdTurmas.add(turma);
                }
            }
            System.out.println("[TurmaService] " + bdTurmas.size() + " turmas carregadas do CSV.");
        } catch (Exception e) {}
    }

    private void atualizarCSV() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARQUIVO_CSV, false))) {
            for (Turma t : bdTurmas) {
                StringBuilder linha = new StringBuilder(t.getCodigo());
                linha.append(";");
                List<Aluno> alunos = t.getAlunos();
                for (int i = 0; i < alunos.size(); i++) {
                    linha.append(alunos.get(i).getMatricula());
                    if (i < alunos.size() - 1) linha.append(",");
                }
                pw.println(linha.toString());
            }
        } catch (Exception e) {}
    }

    @Override
    public void cadastrarTurma(Turma turma) throws RemoteException {
        bdTurmas.add(turma);
        atualizarCSV();
        System.out.println("[TurmaService] Nova turma salva: " + turma.getCodigo());
    }

    @Override
    public Turma consultarTurma(String codigo) throws RemoteException {
        for (Turma t : bdTurmas) {
            if (t.getCodigo().equals(codigo)) return t;
        }
        return null;
    }

    @Override
    public List<Turma> listarTurmas() throws RemoteException { return bdTurmas; }

    @Override
    public void removerTurma(String codigo) throws RemoteException {
        bdTurmas.removeIf(t -> t.getCodigo().equals(codigo));
        atualizarCSV();
    }

    @Override
    public void adicionarAluno(String codigoTurma, String matricula) throws RemoteException {
        Turma turma = consultarTurma(codigoTurma);
        if (turma != null) {
            turma.adicionarAluno(new Aluno("N/A", "N/A", matricula));
            atualizarCSV();
            System.out.println("[TurmaService] Aluno " + matricula + " adicionado na turma " + codigoTurma);
        }
    }

    @Override
    public void removerAluno(String codigoTurma, String matricula) throws RemoteException {
        Turma turma = consultarTurma(codigoTurma);
        if (turma != null) {
            if (turma.removerAlunoPorMatricula(matricula)) atualizarCSV();
        }
    }

    @Override
    public List<Aluno> listarAlunos(String codigoTurma) throws RemoteException {
        Turma turma = consultarTurma(codigoTurma);
        return (turma != null) ? turma.getAlunos() : new ArrayList<>();
    }
}