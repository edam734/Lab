package com.projecto8.esteganografia;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import edu.faculty.provided.LabPImage;

public class CifraTest {

	public static final String OUTPUT_FOLDER = "outputs\\projecto8\\";

	@Test
	public void ciferDeciferMessageSuccess() throws IOException {
		Cifra c = new Cifra();
		String filename = "figura.png";
		String cifra = "1234";
		String mensagem = "10Kg de batatas e uma caixa de morangos";

		LabPImage lpi = c.geraFigura();
		c.escreveImagem(lpi, OUTPUT_FOLDER + filename);

		lpi = c.cifraImagem(lpi, mensagem, cifra, OUTPUT_FOLDER + filename);
		c.escreveImagem(lpi, OUTPUT_FOLDER + "Cif_" + filename);

		LabPImage lpi2 = c.decifraImagem(cifra, OUTPUT_FOLDER + "Cif_" + filename);
		c.escreveImagem(lpi2, OUTPUT_FOLDER + "Decif_" + filename);

		String secret = c.decifraMensagem(lpi2, cifra);

		assertEquals(mensagem, secret);
	}
}
