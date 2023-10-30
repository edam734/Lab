package com.projecto7.pavilion;

import org.junit.jupiter.api.Test;

public class PavilionTest {

	@Test
	public void testPavillionValoresAltos() {
		int nPortas = 24;
		int compMax = 200;
		int compMin = 100;
		int taxaEntrada = 77;
		int totalEsperados = 90000;
		int mediaEvento = 900;

		Simulator.doSimulation(nPortas, compMax, compMin, taxaEntrada, totalEsperados, mediaEvento);
	}
	
	@Test
	public void testSimulatorLambda() {
		
	}
	
	@Test
	public void testDistribuicaoPoisson() {
		
	}

}
