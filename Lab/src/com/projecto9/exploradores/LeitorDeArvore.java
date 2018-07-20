package com.projecto9.exploradores;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Scanner;

import net.datastructures.LinkedBinaryTree;
import net.datastructures.Position;

/**
 * Uma classe que lê dados de um ficheiro e os transforma como elementos de uma
 * LinkedBinaryTree
 * 
 * @author Eduardo Amorim
 *
 */
public class LeitorDeArvore {

    private static final char END_CHARACTER = '#';
    private static final char SPACE = ' ';

    LinkedBinaryTree<Character> lbt;
    Character[][] matrix = null;

    /**
     * Construtor que recebe o nome de um ficheiro e, com os dados que nele 
     * encontra, cria uma matriz auxiliar que será útil na descoberta dos pais 
     * de cada elemento e constrói uma LinkedBinaryTree com esses elementos.
     * 
     * @param filename - O nome do ficheiro a ler
     * @throws FileNotFoundException se o ficheiro não foi encontrado
     */
    public LeitorDeArvore(String filename) throws FileNotFoundException {
	lbt = new LinkedBinaryTree<>();
	File file = new File(filename);

	matrix = buildMatrix(file);

	Scanner sc = new Scanner(file);
	String line = sc.nextLine();
	Character ch = line.trim().charAt(0);

	int l = 0;
	int previousColumn = 0;
	while (ch != END_CHARACTER) {
	    int c = getCharColumn(line);
	    matrix[l][c] = Character.toUpperCase(ch);

	    if (c == 0) {
		lbt.addRoot(ch);
	    } else {
		Character parent = getParent(l, c);
		if (previousColumn < c) {
		    lbt.addLeft(getPosition(parent), ch);
		} else {
		    lbt.addRight(getPosition(parent), ch);
		}
	    }
	    l++;

	    line = sc.nextLine();
	    ch = line.trim().charAt(0);
	    previousColumn = c;
	}
	sc.close();
    }

    /**
     * Busca na matriz dos elementos, o pai do elemento na posição l, c
     * 
     * @param l - o número da linha do elemento na matriz
     * @param c - o número da coluna do elemento na matriz
     * @return O pai do elemento na posição l, c da matriz
     */
    private char getParent(int l, int c) {
	Character parent = null;
	while (parent == null && l >= 0) {
	    parent = matrix[l][c - 1];
	    l--;
	}
	return parent;
    }

    /**
     * Constrói uma matriz. Para saber a sua dimensão, tem primeiro de ler todos 
     * os valores no ficheiro file
     * @param file - O ficheiro a ler
     * @return A matriz contruída
     * 
     * @throws FileNotFoundException se o ficheiro não foi encontrado
     */
    private Character[][] buildMatrix(File file) throws FileNotFoundException {
	Scanner sc = new Scanner(file);

	String line = sc.nextLine();
	Character ch = line.trim().charAt(0);

	int wide = 0;
	int height = 0;
	while (ch != END_CHARACTER) {
	    int column = getCharColumn(line);
	    if (column > wide) {
		wide = column;
	    }
	    height++;

	    line = sc.nextLine();
	    ch = line.trim().charAt(0);
	}
	sc.close();

	return new Character[height][wide + 1];
    }

    /**
     * Obtém a posição do elemento ch na LinkedBinaryTree
     * @param ch - O elemento a ser procurado
     * @return A posição deste elemento ch
     */
    private Position<Character> getPosition(char ch) {
	Iterator<Position<Character>> iterator = lbt.preorder().iterator();

	Position<Character> element = null;
	boolean found = false;
	while (iterator.hasNext() && !found) {
	    found = (element = iterator.next()).getElement() == ch;
	}

	return element;
    }

    /**
     * Obtém a identação de um dado contido numa linha do ficheiro
     * @param str - uma string que corresponde a uma certa linha do ficheiro
     * @return A identação do dado contido numa linha
     */
    private int getCharColumn(String str) {
	int column = 0;
	while (str.charAt(column) == SPACE)
	    column++;

	return column;
    }

    /**
     * 
     * @return A linkedBinaryTree contruída
     */
    public LinkedBinaryTree<Character> getLinkedBinaryTree() {
	return lbt;
    }
}
