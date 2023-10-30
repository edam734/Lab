package com.projecto7.pavilion;

import net.datastructures.ArrayQueue;

public class Pavilion {

	private Porta[] portas;
	private int instanteActual;
	private int compMax;
	private int compMin;
	private int taxaEntrada;

	/**
	 * Construtor que representa um pavilhão genérico.
	 * 
	 * @param nPortas     o número de portas que tem o pavilhão
	 * @param compMax     o comprimento máximo das filas de espera
	 * @param compMin     o comprimento mínimo das filas de espera
	 * @param taxaEntrada o número de espectadores que entram no pavilhão por uma
	 *                    porta de acesso em cada instante de tempo
	 */
	public Pavilion(int nPortas, int compMax, int compMin, int taxaEntrada) {
		this.compMax = compMax;
		this.compMin = compMin;
		this.taxaEntrada = taxaEntrada;
		this.instanteActual = 1;
		this.portas = new Porta[nPortas];
		for (int p = 0; p < portas.length; p++) {
			this.portas[p] = new Porta(p + 1);
		}
	}

	/**
	 * Dá início a uma nova simulação em que se mantém o número de portas do
	 * pavilhão.
	 * 
	 * @param compMax     o comprimento máximo das filas de espera
	 * @param compMin     o comprimento mínimo das filas de espera
	 * @param taxaEntrada o número de espectadores que entram no pavilhão por uma
	 *                    porta de acesso em cada instante de tempo
	 */
	public void reIniciaSimulacao(int compMax, int compMin, int taxaEntrada) {
		// TODO
	}

	/**
	 * Abre a porta p e permite impedir que esta porta seja fechada no decurso da
	 * simulação.
	 * 
	 * @param p a porta a ser aberta e impedida a ser fechada no decurso da
	 *          simulação
	 * @requires ePortaValida(p)
	 */
	public void sempreAberta(int p) {
		this.portas[p].setSempreAberta();
	}

	/**
	 * Fecha a porta p e permite impedir que esta porta seja aberta no decurso da
	 * simulação.
	 * 
	 * @param p a porta a ser fechada e impedida a ser aberta no decurso da
	 *          simulação
	 * @requires ePortaValida(p)
	 */
	public void sempreFechada(int p) {
		this.portas[p].setSempreFechada();
	}

	public void abrirUmaPorta() {
		int p = 0;
		boolean abriu = false;
		while (p < portas.length && !abriu) {
			if (portas[p].estaFechada()) {
				abriu = abrePorta(p);
			}
			p++;
		}
	}

	public void fecharUmaPorta() {
		int p = 0;
		boolean fechou = false;
		while (p < portas.length && !fechou) {
			if (portas[p].estaAberta()) {
				fechou = fechaPorta(p);
			}
			p++;
		}
	}

	/**
	 * Abre a porta p se puder e retorna um booleano a confirmar.
	 * 
	 * @requires p não está sempre fechada && ePortaValida(p)
	 * @param p a porta a ser aberta
	 */
	public boolean abrePorta(int p) {
		return portas[p].abrePorta();
	}

	/**
	 * Fecha a porta p se puder e retorna um booleano a confirmar.
	 * 
	 * @requires p não está sempre aberta && ePortaValida(p)
	 * @param p a porta a ser fechada
	 * @return verdadeiro se a porta foi fechada
	 */
	public boolean fechaPorta(int p) {
		return portas[p].fechaPorta();
	}

	/**
	 * Devolve quantos instantes após o instante actual a porta poderá ser fechada.
	 * return quantos instantes após o instante actual a porta poderá ser fechada
	 */
	public int instEmQueSeraFechada(int p) {
		return portas[p].tamanhoFila() / taxaEntrada;
	}

	/**
	 * Devolve o instante actual da simulação.
	 * 
	 * @return o instante actual da simulação
	 */
	public int instanteActual() {
		return this.instanteActual;
	}

	/**
	 * Avança um instante temporal.
	 */
	public void avancaInstante() {
		this.instanteActual++;
	}

	/**
	 * Devolve o tamanho da fila de espera da porta de acesso p no instante corrente
	 * da execução.
	 * 
	 * @param p a porta verificada
	 * @requires ePortaValida(p)
	 * @return o tamanho da fila de espera
	 */
	public int tamanhoFila(int p) {
		return this.portas[p].tamanhoFila();
	}

	/**
	 * Devolve o tempo médio de espera para entrar, obtido desde o início da
	 * simulação até ao instante corrente
	 * 
	 * @return o tempo médio de espera para entrar
	 */
	public double tempoMedioEspera() {
		return -1; // ???
	}

	/**
	 * Devolve o comprimento médio da fila de espera da porta p, obtido desde o
	 * início da simulação até ao instante corrente.
	 * 
	 * @requires acumulaCompFE(int p) em todos os instantes até ao instante corrente
	 *           inclusive
	 * @param p a porta verificada
	 * @return o comprimento médio da fila de espera da porta p
	 */
	public double compMedioFE(int p) {
		return (double) this.portas[p].getCumprimentoAcumulado() / instanteActual();
	}

	/**
	 * Devolve o comprimento acumulado da fila de espera da porta p, obtido desde o
	 * início da simulação até ao instante corrente.
	 * 
	 * @param p a porta verificada
	 * @return o comprimento acumulado da fila de espera da porta p
	 */
	public void acumulaCompFE(int p) {
		this.portas[p].acumulaComp();
	}

	/**
	 * Efectua a simulação no instante de tempo corrente e devolve a necessidade que
	 * há de abrir ou fechar portas.
	 * 
	 * @requires há portas abertas
	 * @param nEspectadores o número de espectadores esperado
	 * @return 1 para abrir portas, 0 para manter, -1 para fechar
	 */
	public int simulaProxInst(int nEspectadores) {
		int diagnostico = 0;
		int portasCheias = 0;
		boolean sobrelotou = false;
		
		// 1. Coloca cada um dos nEspectadores na cauda de uma fila de espera de uma
		// porta de acesso
		while (nEspectadores > 0 && !sobrelotou) {
			int p = 0;
			while (p < portas.length && nEspectadores > 0) {
				if (portas[p].estaAberta() && portas[p].tamanhoFila() < compMax) {
					portas[p].colocaNaFila();
					nEspectadores--;
				} else if (portas[p].tamanhoFila() >= compMax) {
					portasCheias++;
					if (portasCheias == portasAbertas()) {
						sobrelotou = true;
					}
				}
				p++;
			}
		}
		// uma porta precisa de ser aberta ?
		if (nEspectadores > compMin) {
			diagnostico += 1;
		}

		// 2. Retira da cabeça de cada fila de espera, os indivíduos que entram no
		// instante de tempo corrente
		for (int p = 0; p < portas.length; p++) {
			if (portas[p].estaAberta()) {
				int r = 0;
				while (r < taxaEntrada && portas[p].haFila()) {
					portas[p].removeDaFila();
					r++;
				}
				if (!portas[p].haFila()) {
					// pode fechar esta porta
					diagnostico -= 1;
				}
			}
		}
		return diagnostico;
	}

	private int portasAbertas() {
		int nPortasAbertas = 0;
		for (int p = 0; p < portas.length; p++) {
			if (portas[p].estaAberta()) {
				nPortasAbertas++;
			}
		}
		return nPortasAbertas;
	}

	public boolean ePortaValida(int p) {
		return p < this.portas.length;
	}

	public void imprimePortas() {
		for (int p = 0; p < portas.length; p++) {
			System.out.println(portas[p].imprimePorta());
		}
		System.out.println("-------------------------------------------");
		System.out.println();
	}

	public static class Porta {
		public enum Estado {
			ABERTA, FECHADA, SEMPRE_ABERTA, SEMPRE_FECHADA;
		}

		private int numPorta;
		private Estado estado;
		private double cumprimentoAcumulado;
		private ArrayQueue<Integer> fila;

		public Porta(int numPorta) {
			this.numPorta = numPorta;
			estado = Estado.FECHADA;
			fila = new ArrayQueue<>();

			this.cumprimentoAcumulado = fila.size(); // 0
		}

		public double acumulaComp() {
			return cumprimentoAcumulado += fila.size();
		}

		public double getCumprimentoAcumulado() {
			return cumprimentoAcumulado;
		}

		public int tamanhoFila() {
			return fila.size();
		}

		public void setSempreFechada() {
			if (estado != Estado.SEMPRE_ABERTA) {
				estado = Estado.SEMPRE_FECHADA;
			}
		}

		public void setSempreAberta() {
			if (estado != Estado.SEMPRE_FECHADA) {
				estado = Estado.SEMPRE_ABERTA;
			}
		}

		public boolean abrePorta() {
			if (estado == Estado.SEMPRE_FECHADA || estado == Estado.SEMPRE_ABERTA) {
				return false;
			} else {
				estado = Estado.ABERTA;
				return true;
			}
		}

		public boolean fechaPorta() {
			if (estado == Estado.SEMPRE_ABERTA || estado == Estado.SEMPRE_FECHADA) {
				return false;
			} else {
				estado = Estado.FECHADA;
				return true;
			}
		}

		public boolean estaAberta() {
			return estado == Estado.ABERTA || estado == Estado.SEMPRE_ABERTA;
		}

		public boolean estaFechada() {
			return estado == Estado.FECHADA || estado == Estado.SEMPRE_FECHADA;
		}

		public boolean haFila() {
			return !fila.isEmpty();
		}

		public void colocaNaFila() {
			fila.enqueue(1);
		}

		public boolean removeDaFila() {
			return fila.dequeue() != null;
		}

		public String imprimePorta() {
			String str = "Porta " + this.numPorta + ": ";

			if (this.estado == Estado.FECHADA) {
				str += "(x)";
			} else if (this.estado == Estado.SEMPRE_FECHADA) {
				str += "(X)";
			} else if (this.estado == Estado.SEMPRE_ABERTA) {
				str += "(O)";
			} else {
				str += "(o)";
			}
			return str += " " + this.fila.size();
		}
	}
}
