package com.projecto9.exploradores;

import java.util.Scanner;

public class RunRede {

    public static void main(String[] args) {
	Rede r = new Rede("tree3.txt");
	System.out.println(r.fazRedeParentesis());

	Scanner sc = new Scanner(System.in);
	System.out.print("Diminuir raio de exploracao para: ");
	int raio = sc.nextInt();
	r.diminuirRaioPara(raio);
	System.out.println(r.fazRedeParentesis());
	System.out.println("Exploradores de reserva: " + r.devolveGrupoReserva());
	r.preencherRede();
	System.out.println(r.fazRedeParentesis());
	System.out.println("Exploradores de reserva: " + r.devolveGrupoReserva());
	if (r.temReserva()) {
	    do {
		System.out.print("Preencher partindo de: ");
		char ch = sc.next().charAt(0);
		if (r.preencherRedePartindoDe(Character.toUpperCase(ch))) {
		    System.out.println(r.fazRedeParentesis());
		    break;
		} else {
		    System.out.println("O nó " + ch + " é inapropriado");
		}
	    } while (true);
	}
	sc.close();
    }
}
