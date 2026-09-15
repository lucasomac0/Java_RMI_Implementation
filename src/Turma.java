import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class Turma implements Serializable{
    private String codigo;
    private List<Aluno> alunos; 

    public Turma(String codigo) {
        this.codigo = codigo;
        this.alunos = new ArrayList<>(); 
    }

    public void adicionarAluno(Aluno aluno) {
        this.alunos.add(aluno);
    }

    public boolean removerAlunoPorMatricula(String matricula) {
        return alunos.removeIf(aluno -> aluno.getMatricula().equals(matricula));
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public List<Aluno> getAlunos() {
        return alunos;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Turma: ").append(codigo).append("\nLista de Alunos:\n");
        if (alunos.isEmpty()) {
            sb.append("- Nenhum aluno matriculado.");
        } else {
            for (Aluno aluno : alunos) {
                sb.append("- ").append(aluno).append("\n");
            }
        }
        return sb.toString();
    }
}

