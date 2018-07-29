package com.projecto5.calculadora;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.StringTokenizer;

import com.projecto5.calculadora.exceptions.ParenthesisBalanceErrorException;

public class CalculadoraSufixa {

	private static final String OPERADORES_MATEMATICOS = "+-*/";

	private Deque<String> pilha;
	private StringBuilder sufixa;

	public CalculadoraSufixa() {
		pilha = new ArrayDeque<String>();
		sufixa = new StringBuilder();
	}

	/**
	 * Verifica se o número de parêntesis abertos é igual ao número de parêntesis
	 * fechados
	 * 
	 * @param infixa
	 *            A expressão algébrica na notação infixa
	 * @return true se o número de parêntesis abertos é igual ao número de
	 *         parêntesis fechados
	 */
	public boolean verificaParentesis(String infixa) {
		return quantasOcorrencias(infixa, "(") == quantasOcorrencias(infixa, ")");
	}

	/**
	 * Devolve a expressão infixa na notação sufixa
	 * 
	 * @param infixa
	 *            A expressão algébrica na notação infixa
	 * @return a expressão infixa na notação sufixa
	 */
	public String paraSufixa(String infixa) throws ParenthesisBalanceErrorException {
		clean();

		if (!verificaParentesis(infixa)) {
			throw new ParenthesisBalanceErrorException("Número de parênteses não é coincidente");
		}
		StringTokenizer tokenizer = new StringTokenizer(infixa, " ");
		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			processaToken(token);
		}
		flush();

		return sufixa();
	}

	/*
	 * Limpa a sufixa
	 */
	private void clean() {
		sufixa.setLength(0);
	}

	/*
	 * Retira tudo da pilha e adiciona em sufixa
	 */
	private void flush() {
		while (!pilha.isEmpty()) {
			String token = pilha.pop();
			adicionaEmSufixa(token);
		}
	}

	/**
	 * Processa um token de uma expressão algébrica na notação sufixa.
	 * 
	 * @param token
	 *            um token
	 */
	public void processaToken(String token) {
		if (ehOperadorMatematico(token)) {
			String top = pilha.peek();
			while (top != null && ehOperadorMatematico(top) && comparaOperador(token, top) > 0) {
				pilha.pop();
				adicionaEmSufixa(top);
				top = pilha.peek();
			}
			pilha.push(token);
		} else {
			if (token.equals("(")) {
				pilha.push(token);
			} else if (token.equals(")")) {
				String top = pilha.peek();
				while (top != null && ehOperadorMatematico(top)) {
					pilha.pop();
					adicionaEmSufixa(top);
					top = pilha.peek();
				}
				if (top != null && top.equals("(")) {
					pilha.pop();
				}
			} else {
				adicionaEmSufixa(token);
			}
		}
	}

	/*
	 * Devolve a String sufixa
	 */
	private String sufixa() {
		return sufixa.toString();
	}

	/*
	 * Adiciona um token em sufixa
	 */
	private void adicionaEmSufixa(String token) {
		sufixa.append(token).append(" ");
	}

	/**
	 * Devolve o resultado da expressão aritmética processada
	 */
	public double resultado() {
		double acumulado = -1;

		StringTokenizer tokenizer = new StringTokenizer(sufixa(), " ");
		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			while (!ehOperadorMatematico(token)) {
				pilha.push(token);
				token = tokenizer.nextToken();
			}

			if (acumulado < 0) {
				acumulado = Double.parseDouble(pilha.pop());
			}
			double valor = Double.parseDouble(pilha.pop());
			acumulado = fazConta(valor, acumulado, token);
		}

		return acumulado;
	}

	public String imprimeResultado(String infixa) {
		String input = "";
		try {
			input = paraSufixa(infixa);
		} catch (ParenthesisBalanceErrorException e) {
			return e.getMessage();
		}

		try {
			return input + "? " + resultado();
		} catch (ArithmeticException e) {
			return input + "? " + e.getMessage();
		} catch (java.util.NoSuchElementException e) {
			return input + "? " + "Argumentos insuficientes";
		}
	}

	/*
	 * Faz uma operacao matematica entre dois operandos v1 e v2
	 */
	private double fazConta(double v1, double v2, String operador) {
		if (operador.equals("+")) {
			return v1 + v2;
		} else if (operador.equals("-")) {
			return v1 - v2;
		} else if (operador.equals("*")) {
			return v1 * v2;
		} else {
			if (Double.valueOf(v2).equals(0d)) {
				throw new ArithmeticException("Divisão por zero");
			}
			return v1 / v2;
		}
	}

	/**
	 * Ver org.apache.commons.lang.StringUtils#countMatches
	 * <p>
	 * Conta quantas vezes a substring aparece na String maior. Uma entrada de
	 * String nula ou vazia ("") retorna 0.
	 * 
	 * @param str
	 *            A String para verificar, pode ser null
	 * @param sub
	 *            A substring para contar, pode ser nula
	 * @return O numero de vezes a substring aparece na String maior
	 */
	private int quantasOcorrencias(String str, String sub) {

		if (ehVazia(str) || ehVazia(sub)) {
			return 0;
		}
		int count = 0;
		int idx = 0;
		while ((idx = str.indexOf(sub, idx)) != -1) {
			count++;
			idx += sub.length();
		}
		return count;
	}

	/*
	 * Verifica se uma String eh null ou vazia
	 */
	private boolean ehVazia(String str) {
		return str == null || str.length() == 0;
	}

	/*
	 * Verifica se um token eh um operador matematico
	 */
	private boolean ehOperadorMatematico(String token) {
		return OPERADORES_MATEMATICOS.indexOf(token) != -1;
	}

	/**
	 * Compara dois operandos especificados e retorna um inteiro que indica sua
	 * posição relativa na ordem de classificação.
	 * <p>
	 * Menor de zero: o primeiro operador tem uma prioridade menor do que o segundo
	 * operador
	 * <p>
	 * Zero: ambos os operadores têm a mesma prioridade
	 * <p>
	 * Maior que zero: o primeiro operador tem maior prioridade do que o segundo
	 * operador
	 * 
	 * @param o1
	 *            O primeiro operador
	 * @param o2
	 *            O segundo operador
	 * @return um inteiro
	 */
	private int comparaOperador(String o1, String o2) {
		if (adicionaIndice(o1, o2) == 1 || adicionaIndice(o1, o2) == 5) {
			return 0;
		} else {
			return deduzIndice(o1, o2);
		}
	}

	private int deduzIndice(String o1, String o2) {
		return OPERADORES_MATEMATICOS.indexOf(o1) - OPERADORES_MATEMATICOS.indexOf(o2);
	}

	private int adicionaIndice(String o1, String o2) {
		return OPERADORES_MATEMATICOS.indexOf(o1) + OPERADORES_MATEMATICOS.indexOf(o2);
	}

	public static void main(String[] args) {
		CalculadoraSufixa cs = new CalculadoraSufixa();
		try {
			System.out.println(cs.imprimeResultado("1 + 2"));
			System.out.println(cs.imprimeResultado("2 * ( 3 + 2.5 )"));
			System.out.println(cs.imprimeResultado("5 / ( 8 / 4 )"));
			System.out.println(cs.imprimeResultado("( 5 / ( 3 - 1 )"));
			System.out.println(cs.imprimeResultado("9 / ( 2 - 2 )"));
			System.out.println(cs.imprimeResultado("1 +"));
			System.out.println(cs.imprimeResultado("1 2"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
