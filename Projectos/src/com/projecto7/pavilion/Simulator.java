package com.projecto7.pavilion;

import java.util.Random;

/**
 * ArrayQueue:
 * https://cs.slu.edu/~goldwasser/dsaj/docs/index.html?net/datastructures/ArrayQueue.html
 * 
 * @author Eduardo
 *
 */
public class Simulator {

	public static void doSimulation(int nPortas, int compMax, int compMin, int taxaEntrada,
			int totalEsperados, int mediaEvento) {

		Pavilion pavillion = new Pavilion(nPortas, compMax, compMin, taxaEntrada);

		// abre as portas que deverão estar sempre abertas
		for (int p = 0; p < nPortas; p++) {
			if (eSempreAberta(p)) {
				pavillion.sempreAberta(p);
			}
		}
		int nEspectadores = 0;
		int lambda = 0;
		int diagnostico = 0;

		while (pavillion.instanteActual() <= 60) {
			lambda = calcLambda(totalEsperados, mediaEvento, pavillion.instanteActual());
			nEspectadores = poissonRandomizer(lambda);
			System.out.println("num espectadores: " + nEspectadores);

			diagnostico = pavillion.simulaProxInst(nEspectadores);
			if (diagnostico > 0) {
				pavillion.abrirUmaPorta();
			} else if (diagnostico < 0) {
				pavillion.fecharUmaPorta();
			}
			System.out.println("instante: " + pavillion.instanteActual());
			pavillion.imprimePortas();

			pavillion.avancaInstante();
		}
	}

	/**
	 * Gera valores aleatórios duma distribuição de Poisson.
	 * 
	 * wiki: "Na teoria da probabilidade e na estatística, a distribuição de Poisson
	 * é uma distribuição de probabilidade discreta que expressa a probabilidade de
	 * um determinado número de eventos ocorrer em um intervalo fixo de tempo ou
	 * espaço se esses eventos ocorrerem com uma taxa média constante conhecida e
	 * independentemente do tempo desde o último evento."
	 * 
	 * @param lambda o parâmetro da distribuição
	 * @return um valor aleatório segundo uma distribuição de Poisson
	 */
	private static int poissonRandomizer(int lambda) {

		Random randomizer = new Random();
		int k = 0;
		double sum = -1.0 / lambda * Math.log(randomizer.nextDouble());
		do {
			k++;
			sum = sum - 1.0 / lambda * Math.log(randomizer.nextDouble());
		} while (sum <= 1);

		return k;
	}

	/**
	 * Devolve o parâmetro lambda da distribuição.
	 * 
	 * wiki: "λ é um número real, igual ao número esperado de ocorrências que
	 * ocorrem num dado intervalo de tempo. Por exemplo, se o evento ocorre a uma
	 * média de 4 minutos, e estamos interessados no número de eventos que ocorrem
	 * num intervalo de 10 minutos, usaríamos como modelo a distribuição de Poisson
	 * com λ=10/4= 2.5."
	 * 
	 * @return o parâmetro lambda da distribuição
	 */
	private static int calcLambda(int totalExpected, int mediaEvento, int instant) {
		int beta = totalExpected / mediaEvento;
		if (instant > 0 && instant <= 30) {
			return beta * instant;
		} else if (instant > 30 && instant <= 60) {
			return beta * (60 - instant);
		} else {
			return 0;
		}
	}

	/**
	 * Verifica se é uma porta aberta.
	 * 
	 * @param porta a analizar
	 * @return verdadeiro se é uma porta sempre aberta
	 */
	private static boolean eSempreAberta(int porta) {
		return porta == 0 || porta == 5 || porta == 11 || porta == 17 || porta == 23;
	}

}
