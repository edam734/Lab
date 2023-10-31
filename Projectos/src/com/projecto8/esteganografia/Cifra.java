package com.projecto8.esteganografia;

import java.io.IOException;
import java.util.Random;

import edu.faculty.provided.LabPImage;

/**
 * 
 * @author Eduardo
 *
 */
public class Cifra {

	public static void main(String[] args) throws IOException {
		Cifra c = new Cifra();
		LabPImage lpi = c.geraFigura();
		c.escreveImagem(lpi, "outputs\\projecto8\\testeImagem8.png");
	}

	private int generateRandomInt(Random rn, int max, int min) {
		int num = rn.nextInt(max - min + 1) + min;
		return num;
	}

	/**
	 * Desenha aleatoriamente quadrados recursivos, com tamanhos, posições e número
	 * de passos de recursão aleatórios, dentro dos limites definidos.
	 * 
	 */
	public LabPImage geraFigura() {
		// fill canvas
		LabPImage lpi = new LabPImage(500, 500);
		lpi.filledRectangle(0, 0, 500, 500, 128, 128, 128, 255);

		// draw fractals
		Random rn = new Random();
		int numSquares = generateRandomInt(rn, 7, 3);
		for (int s = 0; s < numSquares; s++) {
			int side = generateRandomInt(rn, 200, 5);
			int x = generateRandomInt(rn, 500, 0);
			int y = generateRandomInt(rn, 500, 0);
			int maxLevel = generateRandomInt(rn, 10, 2);

			drawSquares(lpi, x, y, side, 1, maxLevel);
		}

		return lpi;
	}

	/**
	 * Point (x, y) is the square central point.
	 * 
	 * It's going to draw the square in this order:
	 * <ol>
	 * <li>horizontal inf. line.</li>
	 * <li>vertical esq. line</li>
	 * <li>horizontal sup. line</li>
	 * <li>vertical dir. line</li>
	 * </ol>
	 * 
	 * The limits for each color are between 0 and 255.
	 *
	 * Transparency is 255 always (completely opaque)
	 * 
	 * @param lpi  LabPImage instance
	 * @param side square side in pixels
	 * 
	 */
	private boolean drawSquare(LabPImage lpi, int x, int y, int side) {
		if (x - side / 2 > 0 && x + side / 2 <= 500 && y - side / 2 > 0 && y + side / 2 <= 500) {
			Random rn = new Random();
			int red = generateRandomInt(rn, 255, 0);
			int green = generateRandomInt(rn, 255, 0);
			int blue = generateRandomInt(rn, 255, 0);

			// fill rectangle
			lpi.filledRectangle(x - side / 2, y - side / 2, side, side, red, green, blue, 255);

			// contour
			lpi.drawLine(x - side / 2, y - side / 2, x + side / 2, y - side / 2, 255, 0, 0, 255);
			lpi.drawLine(x - side / 2, y - side / 2, x - side / 2, y + side / 2, 255, 0, 0, 255);
			lpi.drawLine(x - side / 2, y + side / 2, x + side / 2, y + side / 2, 255, 0, 0, 255);
			lpi.drawLine(x + side / 2, y - side / 2, x + side / 2, y + side / 2, 255, 0, 0, 255);

			return true;
		} else {
			return false;
		}
	}

	/**
	 * Point (x, y) is the square central point.
	 * 
	 * It's going to start drawing the squares in this order:
	 * <ol>
	 * <li>left inferior point</li>
	 * <li>left superior point</li>
	 * <li>right superior point</li>
	 * <li>right inferior point</li>
	 * </ol>
	 * 
	 * @param lpi      LabPImage instance
	 * @param side     square side in pixels
	 * @param level    the current recursion level
	 * @param maxLevel the maximum recursion level
	 */
	private void drawSquares(LabPImage lpi, int x, int y, int side, int level, int maxLevel) {
		boolean wasDrawn = drawSquare(lpi, x, y, side);
		if (level < maxLevel && wasDrawn) {
			level++;
			drawSquares(lpi, x - side / 2, y - side / 2, side / 2, level, maxLevel);
			drawSquares(lpi, x - side / 2, y + side / 2, side / 2, level, maxLevel);
			drawSquares(lpi, x + side / 2, y + side / 2, side / 2, level, maxLevel);
			drawSquares(lpi, x + side / 2, y - side / 2, side / 2, level, maxLevel);
		}
	}

	/**
	 * Cifra uma mensagem de texto na imagem, bem como a própria imagem. Note que
	 * este método quando aplicado sobre a imagem cifrada, com os mesmos parâmetros
	 * que foram usados na cifra inicial, decifra a imagem.
	 * 
	 * @param mensagem
	 * @param cifra
	 * @param filename
	 */
	public void cifraImagem(String mensagem, String cifra, String filename) {

	}

	/**
	 * Percorre uma imagem e procura a mensagem escondida, de modo a que o receptor,
	 * apenas na posse da chave, possa ser capaz de procurar a mensagem secreta.
	 * 
	 * @param cifra
	 * @param filename
	 */
	public void decifraMensagem(String cifra, String filename) {

	}

	/**
	 * Writes an image to a file.
	 * 
	 * @param filename
	 * @throws IOException
	 */
	public void escreveImagem(LabPImage lpi, String filename) throws IOException {
		lpi.writeImageToPNG(filename);
	}

}
