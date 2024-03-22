package com.projecto8.esteganografia;

import java.io.IOException;
import java.util.Random;

import edu.faculty.provided.LabPImage;

/**
 * O objectivo deste trabalho é desenvolver uma pequena ferramenta
 * (SMS-LabPImage) capaz de gerar gráficos fractais e que possibilite ainda a
 * cifragem/decifragem de uma mensagem de teste embutida numa imagem. Para tal
 * recorre-se às funcionalidades da classe LabPImage e a técnicas de
 * esteganografia para ocultar a existência de uma mensagem dentro de outra. Um
 * exemplo básico da técnica de esteganografia poderá ser a alteração de um byte
 * de uma das componentes de cor de alguns pixels de uma imagem colorida, de
 * forma a que cada um destes passe a corresponder a um byte da mensagem secreta
 * que queremos esconder na imagem. Desta forma, o receptor será capaz de
 * enconrtar a mensagem secreta embutida na imagem, apenas se estiver na posse
 * da chave numérica usada na cifra.
 * 
 * A aplicação a desenvolver, deverá começar por agregar a mensagem secreta com
 * a chave numérica, devendo a chave numérica ser colocada no início e no final
 * da mensagem. Seguidamente a frase resultante deverá ser escrita em pixels
 * consecutivos de uma coluna da imagem definida aleatoriamente. A posição da
 * coluna onde a frase começa a ser escrita também deverá ser definida
 * aleatoriamente, devendo-se ter o cuidado de verificar se a frase cabe na
 * coluna. Deste modo, será possível detectar o início e fim da mensagem. Os
 * bytes da frase resultante deverão ser escritos na componente vermelha dos
 * pixels. A mensagem secreta não deverá ter mais de 50 caracteres, enquanto que
 * a chave numérica deverá ter entre 3 e 7 algarismos.
 * 
 * De forma a baralhar ainda mais o potencial “espião”, a imagem é ainda sujeita
 * a uma cifra das várias componentes de cor dos pixels. Esta cifra é efectuada
 * recorrendo à chave numérica anterior que, desta vez, assume o papel de
 * semente para um gerador de números aleatórios permitindo gerar cores
 * aleatórias para os pixels. Assim, apenas conhecendo a chave, será possível
 * decifrar a imagem e recuperar a mensagem secreta.
 * 
 * Para que a imagem possa ser decifrada é necessário não perder a informação de
 * cor da imagem inicial, para isso a cifra é implementada fazendo o “ou
 * exclusivo” bit a bit (XOR – operador ^) das cores iniciais com o resultado do
 * gerador de números aleatórios, excluindo desta operação apenas a componente
 * vermelha dos pixels onde é escrito cada caracter da mensagem. Quando a cifra
 * é feita desta forma, se aplicarmos o mesmo método de cifra à imagem cifrada,
 * com os mesmos parâmetros usados para a cifra inicial, obtemos a imagem
 * inicial (não cifrada), apenas alterada nos pixels onde a mensagem foi
 * escrita.
 * 
 * No nosso caso, para criar a imagem inicial, será necessário desenhar vários
 * fractais. Um fractal é uma imagem que contém elementos repetidos com escalas
 * diferentes. O floco de neve de Koch e o conjunto de Mandelbrot são exemplos
 * famosos de fractais. A nossa aplicação deverá desenhar quadrados recursivos
 * como os representados na Figura 1. Nestes quadrados, em cada passo da
 * recursão desenham-se outros quadrados centrados em cada um dos vértices do
 * quadrado anterior e em cada passo o tamanho dos lados reduz-se a metade. A
 * recursão termina quando se atinge o número de passos pretendido ou quando a
 * dimensão do lado do quadrado for menor que 5 pixels. As linhas dos quadrados
 * são vermelhas e os quadrados preenchidos com cores geradas aleatoriamente. O
 * número de quadrados recursivos a desenhar deverá poder variar entre 3 e 7,
 * enquanto que número de passos de recursão deverá poder variar entre 2 e 10. O
 * lado máximo dos quadrados não poderá exceder 200 pixels. Além disto, não
 * deverá ser desenhado nenhum quadrado que não caiba nos limites da imagem.
 * 
 * 
 * @author Eduardo
 *
 */
public class Cifra {

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
     * @param lpi      contém a imagem gerada
     * @param mensagem
     * @param cifra
     * @param filename
     */
    public LabPImage cifraImagem(LabPImage lpi, String message, String cifra, String filename) {
        String msg = cifra + message + cifra;
        byte[] msgBytes = msg.getBytes();

        Random rn = new Random();
        int column = generateRandomInt(rn, 499, 0);

        int line = -1;
        boolean retry = false;
        do {
            line = generateRandomInt(rn, 499, 0);
            if (line + msgBytes.length > 500) {
                retry = true;
            }
        } while (retry);

        escreveMensagem(lpi, msgBytes, column, line);

        // cifra agora a imagem toda
        aplicaCifra(lpi, cifra);
        return lpi;
    }

    /*
     * Cifra toda a imagem, aplicando o XOR em todas as componentes das cores dos
     * pixeis.
     */
    private void aplicaCifra(LabPImage lpi, String cifra) {
        int red = -1;
        int green = -1;
        int blue = -1;
        Random generator = new Random(Integer.valueOf(cifra));
        for (int l = 0; l < lpi.getHeight(); l++) {
            for (int c = 0; c < lpi.getWidth(); c++) {
                red = lpi.getPixelRed(c, l);
                green = lpi.getPixelGreen(c, l);
                blue = lpi.getPixelBlue(c, l);
                red ^= generateRandomInt(generator, 255, 0);
                green ^= generateRandomInt(generator, 255, 0);
                blue ^= generateRandomInt(generator, 255, 0);
                lpi.setPixelRGB(c, l, red, green, blue);
            }
        }
    }

    /*
     * Escreve a mensagem na imagem que está presente no objecto lpi.
     */
    private void escreveMensagem(LabPImage lpi, byte[] msgBytes, int column, int line) {
        int green = -1;
        int blue = -1;
        for (int i = 0; i < msgBytes.length; i++) {
            green = lpi.getPixelGreen(column, line);
            blue = lpi.getPixelBlue(column, line);
            lpi.setPixelRGB(column, line, msgBytes[i], green, blue);
            line++;
        }
    }

    /**
     * Apply scanning of the cipher through the entire image. The inverse of XOR is
     * XOR itself. XOR is used in cryptography for two main reasons: it is
     * reversible e.g if A XOR with B results in C then A XOR with C will give B.
     * 
     * @param cifra
     * @param filename
     * @return
     * @throws IOException
     */
    public LabPImage decifraImagem(String cifra, String filename) throws IOException {
        LabPImage lpi = new LabPImage(filename);
        aplicaCifra(lpi, cifra);
        return lpi;
    }

    /**
     * Percorre uma imagem e procura a mensagem escondida, de modo a que o receptor,
     * apenas na posse da chave, possa ser capaz de procurar a mensagem secreta.
     * 
     * @param lpi
     * @param cifra
     */
    public String decifraMensagem(LabPImage lpi, String cifra) {
        StringBuilder sb = new StringBuilder();

        byte[] cifraBytes = cifra.getBytes();
        boolean finish = false;
        int matchCifra = 0;
        boolean readingMessage = false;
        int red = -1;
        int c = 0;
        while (c < lpi.getWidth() && !finish) {
            int l = 0;
            while (l < lpi.getHeight() && !finish) {
                red = lpi.getPixelRed(c, l);
                if (readingMessage) {
                    sb.append((char) red + "");
                    if (red == cifraBytes[matchCifra] && matchCifra < 4) {
                        matchCifra++;
                        if (matchCifra == 4) { // final da cifra final
                            finish = true;
                        }
                    }
                } else {
                    if (red == cifraBytes[matchCifra] && matchCifra < 4) {
                        matchCifra++;
                        if (matchCifra == 4) { // final da cifra inicial
                            readingMessage = true;
                            matchCifra = 0;
                        }
                    } else {
                        matchCifra = 0;
                    }
                }
                l++;
            }
            c++;
        }

        return mensagemPura(sb.toString(), cifra);

    }

    /*
     * Remove a cifra do final da mensagem.
     */
    private String mensagemPura(String msg, String cifra) {
        return msg.length() > 0 ? msg.substring(0, msg.length() - cifra.length()) : "";
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
