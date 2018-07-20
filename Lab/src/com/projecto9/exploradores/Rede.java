package com.projecto9.exploradores;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import net.datastructures.LinkedBinaryTree;
import net.datastructures.Position;

/**
 * Esta classe representa a organização dos exploradores (nós) numa estrutura em 
 * árvore representativa da rede, em que os ramos representam as ligações rádio.
 * @author Eduardo Amorim
 *
 */
public class Rede {

    private TreeWrapper<Character> tree;
    private Queue<Character> reserva;

    /**
     * Um construtor que lê um ficheiro e devolve a informação numa
     * LinkedBinaryTree
     * 
     * @param filename - O nome do ficheiro com a informação
     */
    public Rede(String filename) {
	reserva = new LinkedList<>();
	LeitorDeArvore ftr;
	try {
	    ftr = new LeitorDeArvore(filename);
	    tree = new TreeWrapper<>(ftr.getLinkedBinaryTree());
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	}
    }

    /**
     * Diminui para n unidades o raio da rede de exploração (ou seja n será o
     * novo raio de exploração), pela transição para o grupo de exploradores de
     * reserva, dos exploradores com raio de exploração superior a n.
     * 
     * @param n - O raio
     */
    public void diminuirRaioPara(int n) {
	if (n == 0 || n >= tree.height(tree.getRoot())) {
	    return;
	}

	List<Position<Character>> lastNodes = parentsWithDepthN(tree.getRoot(), n);
	Iterator<Position<Character>> it = lastNodes.iterator();

	while (it.hasNext()) {
	    Position<Character> p = it.next();
	    putInReserva(p); // coloca os nós em excesso na reserva
	}
    }

    /**
     * Devolve uma lista contendo todos os nós na árvore na profundidade n que
     * têm filhos
     * 
     * @param p - Uma posição válida na árvore
     * @param n - A profundidade escolhida na árvore
     * @return Uma lista preenchida
     */
    private List<Position<Character>> parentsWithDepthN(Position<Character> p, int n) {
	List<Position<Character>> list = new ArrayList<>();
	return parentsWithDepthN(p, n, list);
    }

    /**
     * Devolve uma lista contendo todos os nós na árvore na profundidade n que
     * têm filhos
     * 
     * @param p - Uma posição válida na árvore
     * @param n - A profundidade escolhida na árvore
     * @param list - A lista contendo todos os nós na árvore na profundidade n
     *            que têm filhos
     * @return A lista list preenchida
     */
    private List<Position<Character>> parentsWithDepthN(Position<Character> p, int n, List<Position<Character>> list) {
	if (p == null) {
	    return list;
	}

	if (n == tree.depth(p)) {
	    if (tree.hasChildren(p)) {
		list.add(p);
	    }
	}
	parentsWithDepthN(tree.leftChildOf(p), n, list);
	parentsWithDepthN(tree.rightChildOf(p), n, list);

	return list;
    }

    /**
     * Coloca todos os nós da árvore de raíz p em reserva, excepto o nó de
     * partida
     * 
     * @param p - a maior posição da árvore num determinado instante
     */
    private void putInReserva(Position<Character> p) {
	putInReserva(p, p);
    }

    /**
     * Coloca todos os nós da árvore de raíz p em reserva, excepto o nó de
     * partida (original)
     * 
     * @param original - a raíz desta árvore a colocar em reserva
     * @param p - a maior posição da árvore num determinado instante
     */
    private void putInReserva(Position<Character> original, Position<Character> p) {
	if (p == null) {
	    return;
	}

	putInReserva(original, tree.leftChildOf(p));
	putInReserva(original, tree.rightChildOf(p));

	if (!p.equals(original)) {
	    reserva.add(p.getElement());
	    tree.remove(p);
	}
    }

    /**
     * Completa a árvore de modo a ficar com todas as folhas possíveis, numa
     * árvore binária com a altura da rede de exploração actual. Se for
     * necessário acrescentar nós, estes devem resultar da re-activação do grupo
     * de exploradores de reserva. Se não existirem exploradores de reserva em
     * número suficiente o carácter a usar é o '*'.
     */
    public void preencherRede() {
	while (!tree.isBalanced()) {
	    refillTRee(tree.getRoot(), reserva);
	}
    }

    /**
     * Preenche as folhas da árvore até à profundidade de p
     * 
     * @param p - a posição raiz da árvore a preencher
     * @param fifo - uma fila com os exploradores da reserva que vão servir para
     *            preencher a árvore
     */
    private void refillTRee(Position<Character> p, Queue<Character> fifo) {
	if (p == null) {
	    return;
	}
	if (tree.depth(p) < tree.height(tree.getRoot())) {
	    if (!tree.hasChildren(p)) {
		tree.addLeft(p, (fifo.isEmpty() ? '*' : fifo.remove()));
		tree.addRight(p, (fifo.isEmpty() ? '*' : fifo.remove()));
	    } else {
		refillTRee(tree.leftChildOf(p), fifo);
		refillTRee(tree.rightChildOf(p), fifo);
	    }
	}
    }

    /**
     * Preenche a rede, da esquerda para a direita, partindo do nó c para a
     * periferia da rede, acrescentando todos os nós os existentes no grupo de
     * exploradores de reserva. Deve retornar false se o nó c for inexistente,
     * inapropriado (i.e. sem ligações rádio por atribuir) ou inactivo.
     * 
     * @param c
     * @return
     */
    public boolean preencherRedePartindoDe(char c) {
	Position<Character> p = tree.findNode(c);
	if (ehInapropriado(p)) {
	    return false;
	}
	preencherRedePartindoDe(p);
	return true;
    }
    
    /**
     * Verifica se o nó na posição p é inapropriado
     * @param p
     * @return true se é null ou se já tem filhos
     */
    private boolean ehInapropriado(Position<Character> p) {
	return p == null || tree.hasChildren(p);
    }

    /**
     * Preenche a rede, da esquerda para a direita, partindo do nó c para a
     * periferia da rede, acrescentando todos os nós os existentes no grupo de
     * exploradores de reserva.
     * 
     * @param p - A posição a iniciar o preenchimento da árvore
     */
    private void preencherRedePartindoDe(Position<Character> p) {
	if (p == null) {
	    return;
	}
	if (reserva.isEmpty()) {
	    return;
	}
	tree.addLeft(p, reserva.remove());
	tree.addRight(p, reserva.remove());

	preencherRedePartindoDe(tree.leftChildOf(p));
	preencherRedePartindoDe(tree.rightChildOf(p));
    }

    /**
     * Devolve uma cadeia de caracteres que é uma representação textual da rede
     * de exploração, tal que um nó aparece entre parêntesis, seguido das
     * sub-árvores esquerda e direita, também entre parêntesis.
     * 
     * @return
     */
    public String fazRedeParentesis() {
	StringBuilder result = new StringBuilder();

	Position<Character> root = tree.getRoot();
	if (root != null) {
	    result.append(root.getElement());
	    result.append("(");
	    result.append(imprimeNode(root));
	    result.append(")");
	}
	return "(" + result.toString() + ")";
    }

    /**
     * Imprime todos os nós abaixo da Position p
     * 
     * @param p - uma posição válida na árvore
     * @return Uma cadeia de strings representando o conteúdo da árvore
     */
    private String imprimeNode(Position<Character> p) {
	StringBuilder sb = new StringBuilder();

	if (p == null) {
	    sb.append("");
	}

	Position<Character> leftChild = tree.leftChildOf(p);
	Position<Character> rightChild = tree.rightChildOf(p);

	if (leftChild == null) {
	    sb.append("");
	} else {
	    sb.append(leftChild.getElement());
	    sb.append("(");
	    sb.append(imprimeNode(leftChild));
	    sb.append(")");
	}
	if (rightChild == null) {
	    sb.append("");
	} else {
	    sb.append(rightChild.getElement());
	    sb.append("(");
	    sb.append(imprimeNode(rightChild));
	    sb.append(")");
	}
	return sb.toString();
    }

    /**
     * Devolve uma cadeia de caracteres com uma representação textual da rede,
     * com um nó por linha, idêntica à representação no ficheiro de entrada e à
     * fornecida pelo método toString da LinkedBinaryTree.
     * 
     * @return
     */
    public String devolveRede() {
	return null;
    }

    /**
     * Devolve uma cadeia de caracteres com uma representação textual da rede
     * dos exploradores inactivos (em reserva), com todos os nós na mesma linha
     * e separados por espaços.
     * 
     * @return
     */
    public String devolveGrupoReserva() {
	StringBuilder sb = new StringBuilder();
	Iterator<Character> it = reserva.iterator();
	while (it.hasNext()) {
	    sb.append(it.next()).append(" ");
	}
	return sb.toString();
    }

    /**
     * Verifica se ainda existem exploradores de reserva
     * 
     * @return
     */
    public boolean temReserva() {
	return reserva.size() > 0;
    }

    /**
     * Fornece um iterador sobre a rede de exploração, de modo a poder percorrer
     * os caracteres da rede de exploração.
     * 
     * @return
     */
    public Iterator<Character> iterador() {
	return tree.iterator();
    }

    /**
     * Devolve a linkedBinaryTree
     * @return
     */
    public LinkedBinaryTree<Character> getLinkedBinaryTree() {
	return tree.getLinkedBinaryTree();
    }

    /**
     * Wraps a LinkedBinaryTree offering some more methods
     * @author Eduardo Amorim
     *
     * @param <E>
     */
    private class TreeWrapper<E> {

	private LinkedBinaryTree<E> linkedBinaryTree;

	private TreeWrapper(LinkedBinaryTree<E> linkedBinaryTree) {
	    this.linkedBinaryTree = linkedBinaryTree;
	}

	private Position<E> getRoot() {
	    return linkedBinaryTree.root();
	}

	private int height(Position<E> p) {
	    return linkedBinaryTree.height(p);
	}

	private int depth(Position<E> p) {
	    return linkedBinaryTree.depth(p);
	}

	private Position<E> leftChildOf(Position<E> p) {
	    return linkedBinaryTree.left(p);
	}

	private Position<E> rightChildOf(Position<E> p) {
	    return linkedBinaryTree.right(p);
	}

	private boolean hasChildren(Position<E> p) {
	    return linkedBinaryTree.isInternal(p);
	}

	private Position<E> addLeft(Position<E> p, E e) {
	    return linkedBinaryTree.addLeft(p, e);
	}

	private Position<E> addRight(Position<E> p, E e) {
	    return linkedBinaryTree.addRight(p, e);
	}

	private E remove(Position<E> p) {
	    return linkedBinaryTree.remove(p);
	}

	private LinkedBinaryTree<E> getLinkedBinaryTree() {
	    return linkedBinaryTree;
	}

	private Iterator<E> iterator() {
	    return linkedBinaryTree.iterator();
	}

	/**
	 * Esta árvore está balanceada?
	 * 
	 */
	private boolean isBalanced() {
	    return isBalancedTo(height(getRoot()));
	}

	/**
	 * Verifica se a árvore está balanceada apenas até a uma determinada
	 * profundidade
	 * 
	 * @param depth - A profundidade a que se quer verificar o balanceamento
	 * @return true se a árvore está balanceada até à profundidade heigth
	 */
	private boolean isBalancedTo(int depth) {
	    if (depth == 0) {
		return true;
	    }
	    return sizeTo(getRoot(), depth) == perfectNumberOfElements(depth);
	}

	/**
	 * O número de elementos da árvore até à profundidade n 
	 * @param p - a posição raíz da árvore
	 * @param n - a profundidade até onde se quer contar os elementos
	 * @return
	 */
	private int sizeTo(Position<E> p, int n) {
	    if (p == null) {
		return 0;
	    }
	    if (depth(p) == n) {
		return 1;
	    }
	    return 1 + sizeTo(leftChildOf(p), n) + sizeTo(rightChildOf(p), n);
	}

	/**
	 * Devolve o número de elementos de uma árvore balanceada com altura
	 * height 
	 * @param height - a altura da árvore
	 * @return
	 */
	private int perfectNumberOfElements(int height) {
	    if (height == 0) {
		return 1;
	    }
	    return (int) (Math.pow(2, height) + perfectNumberOfElements(--height));
	}

	/**
	 * Devolve a posição do nó da árvore com um elemento igual a element 
	 * @param p - a posição na árvore para começar a procurar
	 * @param element - o elemento a procurar
	 * @return null se não encontrou o nó com o elemento element
	 */
	private Position<E> findNode(E element) {
	    Iterator<Position<E>> it = linkedBinaryTree.preorder().iterator();

	    Position<E> p = null;
	    boolean found = false;
	    while (it.hasNext() && !found) {
		found = ((p = it.next()).getElement() == element);
	    }
	    return p;
	}
    }
}
