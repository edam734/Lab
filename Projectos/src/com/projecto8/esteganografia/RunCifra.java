package com.projecto8.esteganografia;

import java.io.IOException;
import java.util.Scanner;

import edu.faculty.provided.LabPImage;

public class RunCifra {

	public static void main(String[] args) throws IOException {

		RunCifra run = new RunCifra();

		Scanner sc = new Scanner(System.in);
		String opcao = "";
		do {
			System.out.println("Opcção desejada?");
			opcao = sc.nextLine();

			if (opcao.equalsIgnoreCase("cifrar")) {
				String mensagem = readMensagem(sc);

				String cifra = readCifra(sc);

				System.out.println("Nome do ficheiro não cifrado:");
				String filename = sc.nextLine();

				run.cifrar(mensagem, cifra, filename);
				
			} else if (opcao.equalsIgnoreCase("decifrar")) {
				System.out.println("Nome do ficheiro cifrado:");
				String fileInput = sc.nextLine();

				String cifra = readCifra(sc);

				System.out.println("Nome do ficheiro para escrever a imagem decifrada:");
				String fileOutput = sc.nextLine();

				run.decifrar(fileInput, cifra, fileOutput);
				
			}

		} while (!opcao.equalsIgnoreCase("terminar"));
		sc.close();
	}

	private static String readMensagem(Scanner sc) {
		String msg = "";
		int maxMsgLength = 50;
		do {
			System.out.println("Mensagem a cifrar:");
			msg = sc.nextLine();
			if (msg.length() > maxMsgLength) {
				System.out.println(
						"Mensagem deverá ter até 50 caracteres, digite uma nova mensagem:");
			}
		} while (msg.length() > maxMsgLength);
		return msg;
	}

	private static String readCifra(Scanner sc) {
		String cifra = "";
		int minCifraLength = 3;
		int maxCifraLength = 7;
		boolean notNum = false;
		do {
			notNum = false;
			System.out.println("Chave de cifra:");
			cifra = sc.nextLine();
			try {
				Integer.valueOf(cifra);
			} catch (NumberFormatException e) {
				notNum = true;
			}
			if (cifra.length() < minCifraLength || cifra.length() > maxCifraLength || notNum) {
				System.out.println("Chave deverá ter entre 3 e 7 dígitos, digite uma nova chave:");
			}
		} while (cifra.length() < minCifraLength || cifra.length() > maxCifraLength || notNum);
		return cifra;
	}

	public static final String OUTPUT_FOLDER = "outputs\\projecto8\\";

	private Cifra c;

	public RunCifra() {
		super();
		c = new Cifra();
	}

	/**
	 * Pede a mensagem a cifrar, a chave de cifra e o nome do ficheiro onde será
	 * gravada a imagem não cifrada. Promove o desenho da imagem não cifrada, com os
	 * quadrados recursivos e grava-a. Cifra a mensagem na imagem, bem como a
	 * própria imagem, tal como descrito na descrição do problema. Grava a imagem
	 * cifrada, num ficheiro com o nome da imagem não cifrada mais o prefixo “Cif_”.
	 *
	 * @param msg
	 * @param cifra
	 * @param filename
	 * @throws IOException
	 */
	public void cifrar(String msg, String cifra, String filename) throws IOException {
		LabPImage lpi = c.geraFigura();
		filename = filename + ".png";
		this.c.escreveImagem(lpi, OUTPUT_FOLDER + filename);
		System.out.println("Ficheiro " + filename + " gravado com sucesso");
		lpi = this.c.cifraImagem(lpi, msg, cifra, OUTPUT_FOLDER + filename);
		this.c.escreveImagem(lpi, OUTPUT_FOLDER + "Cif_" + filename);
		System.out.println("Ficheiro Cif_" + filename + " gravado com sucesso");
	}

	/**
	 * Pede o nome do ficheiro cifrado, da chave de cifra e o nome do ficheiro onde
	 * a imagem decifrada será gravada. Procede à decifragem da mensagem e da
	 * imagem, mosta a mensagem e grava a imagem decifrada.
	 * 
	 * @param fileInput
	 * @param cifra
	 * @param fileOutput
	 * @throws IOException
	 */
	public void decifrar(String fileInput, String cifra, String fileOutput) throws IOException {
		LabPImage lpi = c.decifraImagem(cifra, OUTPUT_FOLDER + fileInput + ".png");
		c.escreveImagem(lpi, OUTPUT_FOLDER + fileOutput + ".png");
		String secret = c.decifraMensagem(lpi, cifra);
		System.out.println("Mensagem secreta: " + secret);
	}

}