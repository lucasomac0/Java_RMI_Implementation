package sca;

import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

@WebService
public interface WSSCAServer {

	@WebMethod
	void cadastrarAluno(@WebParam(name = "aluno") Aluno aluno);

	@WebMethod
	Aluno consultarAluno(@WebParam(name = "matricula") String matricula);

	@WebMethod
	List<Aluno> listarAlunos();

	@WebMethod
	void removerAluno(@WebParam(name = "matricula") String matricula);

	@WebMethod
	void cadastrarTurma(@WebParam(name = "turma") TurmaWS turma);

	@WebMethod
	TurmaWS consultarTurma(@WebParam(name = "codigo") String codigo);

	@WebMethod
	List<TurmaWS> listarTurmas();

	@WebMethod
	void removerTurma(@WebParam(name = "codigo") String codigo);

	@WebMethod
	void adicionarAluno(@WebParam(name = "codigoTurma") String codigoTurma,
			@WebParam(name = "matricula") String matricula);

	@WebMethod(operationName = "removerAlunoTurma")
	@RequestWrapper(className = "sca.jaxws.RemoverAlunoTurma")
	@ResponseWrapper(className = "sca.jaxws.RemoverAlunoTurmaResponse")
	void removerAluno(@WebParam(name = "codigoTurma") String codigoTurma,
			@WebParam(name = "matricula") String matricula);

	@WebMethod(operationName = "listarAlunosTurma")
	@RequestWrapper(className = "sca.jaxws.ListarAlunosTurma")
	@ResponseWrapper(className = "sca.jaxws.ListarAlunosTurmaResponse")
	List<Aluno> listarAlunos(@WebParam(name = "codigoTurma") String codigoTurma);

	@WebMethod
	void matricularAluno(@WebParam(name = "matricula") String matricula,
			@WebParam(name = "codigoTurma") String codigoTurma);

	@WebMethod
	void cancelarMatricula(@WebParam(name = "matricula") String matricula,
			@WebParam(name = "codigoTurma") String codigoTurma);
}