package com.projecto8.esteganografia;

public class RunCifra {
	
	public static void main(String[] args) {
		byte x = (byte)128 ^ (byte)2;
		System.out.println(x);
		System.out.println((byte)-126^ (byte)2);
	}
	
	/**
	 * Pede a mensagem a cifrar, a chave de cifra e o nome do ficheiro onde ser�
	 * gravada a imagem n�o cifrada. Promove o desenho da imagem n�o cifrada, com os
	 * quadrados recursivos e grava-a. Cifra a mensagem na imagem, bem como a
	 * pr�pria imagem, tal como descrito na descri��o do problema. Grava a imagem
	 * cifrada, num ficheiro com o nome da imagem n�o cifrada mais o prefixo �Cif_�.
	 */
	public void cifrar() {

	}

	/**
	 * Pede o nome do ficheiro cifrado, da chave de cifra e o nome do ficheiro onde
	 * a imagem decifrada ser� gravada. Procede � decifragem da mensagem e da
	 * imagem, mosta a mensagem e grava a imagem decifrada.
	 */
	public void decifrar() {

	}

	/**
	 * Termina a interac��o.
	 */
	public void terminar() {

	}
}
