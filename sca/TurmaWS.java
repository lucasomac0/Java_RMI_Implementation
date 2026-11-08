package sca;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlType;

// Representação da Turma no XML do contrato WSSCA (campo "nome").
// Não é usada no RMI, onde a Turma continua com "codigo".
@XmlType(name = "turma", namespace = "http://sca/")
public class TurmaWS {
    private String nome;
    private List<Aluno> alunos = new ArrayList<>();

    public TurmaWS() {
    }

    public TurmaWS(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Aluno> getAlunos() {
        return alunos;
    }

    public void setAlunos(List<Aluno> alunos) {
        this.alunos = alunos;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Turma: ").append(nome).append("\nLista de Alunos:\n");
        if (alunos == null || alunos.isEmpty()) {
            sb.append("- Nenhum aluno matriculado.");
        } else {
            for (Aluno aluno : alunos) {
                sb.append("- ").append(aluno).append("\n");
            }
        }
        return sb.toString();
    }
}