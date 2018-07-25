package com.projecto3.escalonador;

/**
 * Classe que representa um escalonador de tarefas de modo a que possam ser
 * executadas dentro de um intervalo temporal pré-determinado
 * 
 * @author Eduardo Amorim
 *
 */
public class Escalonador {

	private int[][] tabela;
	private int instante;
	private int[] carregador;
	private char[] simbolos;

	/**
	 * Objectos desta classe funcionam da seguinte forma:
	 * <p>
	 * O objecto do tipo escalonador recebe uma tabela com as tarefas e o seu tempo
	 * de procesamento. Cria dois arrays auxiliares: {@code carregador} e
	 * {@code simbolos} que vão, respectivamente, guardar o tempo das tarefas que
	 * falta ser processado e o estado simbólico das tarefas num certo instante.
	 * 
	 * @param tabela
	 *            A tabela com a informação das tarefas
	 */
	public Escalonador(int[][] tabela) {
		this.tabela = tabela;
		instante = 0;
		carregador = constroiCarregador(tabela);
		simbolos = constroiSimbolos(tabela);
	}

	/**
	 * Verifica se esta lista de tarefas podem ser escalonáveis.
	 * 
	 * @return true se esta lista de tarefas podem ser escalonáveis
	 */
	public boolean garanteEscalonabilidade() {
		int n = tabela.length;
		int p = 0;
		int c = 0;
		for (int linha = 0; linha < n; linha++) {
			p += tabela[linha][0];
			c += tabela[linha][1];
		}

		boolean rateMonotonic = c / p <= (n * (Math.pow(2, 1 / n) - 1));
		return rateMonotonic;
	}

	/**
	 * Devolve uma string contendo a simbologia correspondente ao estado da tarefa
	 * neste instante.
	 */
	public String linhaSeguinte() {
		for (int linha = 0; linha < tabela.length; linha++) {
			if (comeca(linha)) {
				tentaActivar(linha);
			} else {
				tentaProcessar(linha);
			}
		}
		instante++;

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < simbolos.length; i++) {
			sb.append(simbolos[i]).append(" ");
		}

		return sb.toString();
	}

	/**
	 * Verifica se esta tarefa {@code linha} está à espera de ser processada
	 * 
	 * @param linha
	 *            A linha da tabela correspondente à tarefa
	 * @return true se a tarefa está à espera de ser processada
	 */
	private boolean estahEmEspera(int linha) {
		return simbolos[linha] == '*' || simbolos[linha] == '-';
	}

	/**
	 * Verifica se esta tarefa {@code linha} está a ser processada
	 * 
	 * @param linha
	 *            A linha da tabela correspondente à tarefa
	 * @return true se a tarefa está a ser processada
	 */
	private boolean estahActiva(int linha) {
		return simbolos[linha] == '+';
	}

	/**
	 * Activa a tarefa {@code linha} ou coloca-a à espera do processador.
	 * 
	 * @param linha
	 *            A linha da tabela correspondente à tarefa
	 */
	private void tentaActivar(int linha) {
		repoeValor(linha);

		if (podeExecutar(linha)) {
			carregador[linha]--;
			simbolos[linha] = '+';
		} else {
			simbolos[linha] = '*';
		}
	}

	/**
	 * Repõe o valor da segunda coluna que está na linha {@code linha} da tabela, no
	 * array carregador
	 * 
	 * @param linha
	 *            A linha da tabela correspondente à tarefa
	 */
	private void repoeValor(int linha) {
		carregador[linha] = tabela[linha][1];
	}

	/**
	 * Processa a tarefa {@code linha}, termina-a, coloca-a à espera ou coloca-a em
	 * preempção.
	 * 
	 * @param linha
	 *            A linha da tabela correspondente à tarefa
	 */
	private void tentaProcessar(int linha) {
		if (podeExecutar(linha)) {
			if (carregador[linha] == 0) {
				simbolos[linha] = '.';
			} else {
				carregador[linha]--;
				simbolos[linha] = '+';
			}
		} else {
			if (estahEmEspera(linha)) {
				// continua em espera se estava à espera
				simbolos[linha] = '-';
			} else if (estahActiva(linha)) {
				// termina ou coloca em preempção se estava activa
				if (carregador[linha] == 0) {
					simbolos[linha] = '.';
				} else {
					simbolos[linha] = 'o';
				}
			}
		}
	}

	/**
	 * Verifica se este é o instante da tarefa {@code linha} ser executada
	 * 
	 * @param linha
	 *            A linha da tabela correspondente à tarefa
	 * @return se este é o instante da tarefa ser executada
	 */
	private boolean comeca(int linha) {
		return instante == 0 || instante % tabela[linha][0] == 0;
	}

	/**
	 * Verifica se uma tarefa {@code linha} pode ser executada, isto é, se nenhuma
	 * tarefa de maior prioridade estah a ser executada.
	 * 
	 * @param linha
	 *            A linha da tabela correspondente à tarefa
	 * @return true se esta tarefa pode ser executada
	 */
	private boolean podeExecutar(int linha) {
		if (linha == 0) {
			return true;
		}
		for (int i = linha - 1; i >= 0; i--) {
			if (simbolos[i] != '.') {
				return false;
			}
		}
		return true;
	}

	/**
	 * Verifica se ainda há mais linhas a processar
	 */
	public boolean temLinhaSeguinte() {
		return false;
	}

	/**
	 * Constrói um array com os valores da segunda coluna da tabela
	 * 
	 * @param tabela
	 * @return
	 * @requires tabela != null
	 */
	private int[] constroiCarregador(int[][] tabela) {
		int[] c = new int[tabela.length];
		for (int i = 0; i < c.length; i++) {
			c[i] = tabela[i][1];
		}
		return c;
	}

	/**
	 * Constroi um array de símbolos, iniciando tudo a '*'
	 * 
	 * @param tabela
	 * @return
	 * @requires tabela != null
	 */
	private char[] constroiSimbolos(int[][] tabela) {
		char[] s = new char[tabela.length];
		for (int i = 0; i < s.length; i++) {
			s[i] = '*';
		}
		return s;
	}

	public static void main(String[] args) {
		int[][] tabela1 = new int[3][2];
		tabela1[0][0] = 4;
		tabela1[0][1] = 1;
		tabela1[1][0] = 6;
		tabela1[1][1] = 1;
		tabela1[2][0] = 12;
		tabela1[2][1] = 4;

		int[][] tabela2 = new int[5][2];
		tabela2[0][0] = 4;
		tabela2[0][1] = 1;
		tabela2[1][0] = 6;
		tabela2[1][1] = 1;
		tabela2[2][0] = 10;
		tabela2[2][1] = 1;
		tabela2[3][0] = 12;
		tabela2[3][1] = 1;
		tabela2[4][0] = 20;
		tabela2[4][1] = 2;

		imprimeMatriz(tabela2);

		Escalonador escalonador = new Escalonador(tabela2);

		System.out.println("Garante escalonabilidade ? " + escalonador.garanteEscalonabilidade());

		for (int i = 0; i < 60; i++) {
			System.out.println(escalonador.linhaSeguinte());
		}
	}

	private static void imprimeMatriz(int[][] m) {
		for (int l = 0; l < m.length; l++) {
			for (int c = 0; c < m[0].length; c++) {
				System.out.print(m[l][c]);
			}
			System.out.println();
		}
	}
}
