package view;

import controller.ThreadCavalo;

public class Cavalaria {

	public static void main(String[] args) {
		ThreadCavalo[] tc = new ThreadCavalo[5];
		for (int i = 1; i < 5; i++) {
			tc[i] = new ThreadCavalo(i);
			tc[i].start();
		}

	}

}
