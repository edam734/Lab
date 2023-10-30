package com.projecto7.pavillion;

import net.datastructures.ArrayQueue;
import java.util.Random;

/**
 * ArrayQueue:
 * https://cs.slu.edu/~goldwasser/dsaj/docs/index.html?net/datastructures/ArrayQueue.html
 * 
 * @author Eduardo
 *
 */
public class Simulator {

	public static void main(String[] args) {

		int nPortas = 24;
		int compMax = 200;
		int compMin = 100;
		int taxaEntrada = 77;
//		int nPortas = 6;
//		int compMax = 20;
//		int compMin = 10;
//		int taxaEntrada = 2;

		Pavillion pavillion = new Pavillion(nPortas, compMax, compMin, taxaEntrada);

		// abre as portas que deverão estar sempre abertas
		for (int p = 0; p < nPortas; p++) {
			if (eSempreAberta(p)) {
				pavillion.sempreAberta(p);
			}
		}
		int totalEsperados = 90000;
//		int totalEsperados = 2000;
		int nEspectadores = 0;
		int lambda = 0;
		int diagnostico = 0;

		while (pavillion.instanteActual() <= 60) {
			lambda = calcLambda(totalEsperados, pavillion.instanteActual());
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
	 * @return o parâmetro lambda da distribuição
	 */
	private static int calcLambda(int totalExpected, int instant) {
		int beta = totalExpected / 900; /*20;*/
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
