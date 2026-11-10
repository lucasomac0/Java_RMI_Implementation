package sca;

public class Portas {
    private static final int BASE = Integer.getInteger("sca.porta", 1099);

    public static int aluno()      { return BASE; }
    public static int turma()      { return BASE + 1; }
    public static int secretaria() { return BASE + 2; }
}