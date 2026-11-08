package sca;

import java.rmi.Naming;
import java.util.ArrayList;
import java.util.List;
import javax.jws.WebService;

@WebService(endpointInterface = "sca.WSSCAServer",
		serviceName = "WSSCAServerImplService",
		portName = "WSSCAServerImplPort")
public class WSSCAServerImpl implements WSSCAServer {

	private interface Chamada<T> {
		T executar() throws Exception;
	}

	private interface Acao {
		void executar() throws Exception;
	}

	private final String ipAluno;
	private final String ipTurma;
	private final String ipSecretaria;

	public WSSCAServerImpl(String ipAluno, String ipTurma, String ipSecretaria) {
		this.ipAluno = ipAluno;
		this.ipTurma = ipTurma;
		this.ipSecretaria = ipSecretaria;
	}

	// ---------- acesso ao RMI (lookup a cada chamada) ----------

	private AlunoService alunos() throws Exception {
		return (AlunoService) Naming.lookup("rmi://" + ipAluno + ":1099/AlunoService");
	}

	private TurmaService turmas() throws Exception {
		return (TurmaService) Naming.lookup("rmi://" + ipTurma + ":1100/TurmaService");
	}

	private SecretariaService secretaria() throws Exception {
		return (SecretariaService) Naming.lookup("rmi://" + ipSecretaria + ":1101/SecretariaService");
	}

	private <T> T obter(Chamada<T> c) {
		try {
			return c.executar();
		} catch (Exception e) {
			throw new RuntimeException("Falha ao acessar o serviço RMI: " + e, e);
		}
	}

	private void rodar(Acao a) {
		try {
			a.executar();
		} catch (Exception e) {
			throw new RuntimeException("Falha ao acessar o serviço RMI: " + e, e);
		}
	}

	// ---------- conversão Turma (RMI, codigo) <-> TurmaWS (XML, nome) ----------

	private static TurmaWS paraWS(Turma t) {
		if (t == null) return null;
		TurmaWS w = new TurmaWS(t.getCodigo());
		w.setAlunos(new ArrayList<>(t.getAlunos()));
		return w;
	}

	private static Turma paraRMI(TurmaWS w) {
		Turma t = new Turma(w.getNome());
		if (w.getAlunos() != null) {
			for (Aluno a : w.getAlunos()) {
				t.adicionarAluno(a);
			}
		}
		return t;
	}

	// ---------- Aluno ----------

	@Override
	public void cadastrarAluno(Aluno aluno) {
		rodar(() -> alunos().cadastrarAluno(aluno));
	}

	@Override
	public Aluno consultarAluno(String matricula) {
		return obter(() -> alunos().consultarAluno(matricula));
	}

	@Override
	public List<Aluno> listarAlunos() {
		return obter(() -> new ArrayList<>(alunos().listarAlunos()));
	}

	@Override
	public void removerAluno(String matricula) {
		rodar(() -> alunos().removerAluno(matricula));
	}

	// ---------- Turma ----------

	@Override
	public void cadastrarTurma(TurmaWS turma) {
		rodar(() -> turmas().cadastrarTurma(paraRMI(turma)));
	}

	@Override
	public TurmaWS consultarTurma(String codigo) {
		return obter(() -> paraWS(turmas().consultarTurma(codigo)));
	}

	@Override
	public List<TurmaWS> listarTurmas() {
		return obter(() -> {
			List<TurmaWS> resultado = new ArrayList<>();
			for (Turma t : turmas().listarTurmas()) {
				resultado.add(paraWS(t));
			}
			return resultado;
		});
	}

	@Override
	public void removerTurma(String codigo) {
		rodar(() -> turmas().removerTurma(codigo));
	}

	@Override
	public void adicionarAluno(String codigoTurma, String matricula) {
		rodar(() -> turmas().adicionarAluno(codigoTurma, matricula));
	}

	@Override
	public void removerAluno(String codigoTurma, String matricula) {
		rodar(() -> turmas().removerAluno(codigoTurma, matricula));
	}

	@Override
	public List<Aluno> listarAlunos(String codigoTurma) {
		return obter(() -> new ArrayList<>(turmas().listarAlunos(codigoTurma)));
	}

	// ---------- Secretaria ----------

	@Override
	public void matricularAluno(String matricula, String codigoTurma) {
		rodar(() -> secretaria().matricularAluno(matricula, codigoTurma));
	}

	@Override
	public void cancelarMatricula(String matricula, String codigoTurma) {
		rodar(() -> secretaria().cancelarMatricula(matricula, codigoTurma));
	}
}