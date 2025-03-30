package controller;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class ThreadCavalo extends Thread {
	private int idCavaleiro;
	private int speed;

	private static Semaphore semaforoTocha = new Semaphore(1);
	private static Semaphore semaforoPedra = new Semaphore(1);
	private static Semaphore semaforoPorta = new Semaphore(1);

	private static Boolean temTocha = false;
	private static Boolean temPedra = false;
	private static Random rnd = new Random();
	private static int portaCerta = rnd.nextInt(4);

	public ThreadCavalo(int idCavaleiro) {
		this.idCavaleiro = idCavaleiro;
		this.speed = 200 + rnd.nextInt(401); // Velocidade inicial entre 20 e 60
	}

	@Override
	public void run() {
		Cavalgada();
		EscolhePorta();
	}

	private void Cavalgada() {
		int time = 50;
		int percurso = 0;

		while (percurso < 2000) {
			percurso += speed;
			if (percurso > 2000) {
				percurso = 2000;
			}

			System.out.println("O cavaleiro #" + idCavaleiro + " andou " + percurso + " metros no total");

			// Tenta pegar a tocha após 500 metros
			if (percurso >= 500) {
				PegaTocha();
			}

			// Tenta pegar a pedra após 1500 metros
			if (percurso >= 1500) {
				PegaPedra();
			}

			try {
				sleep(time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void PegaTocha() {
		if (semaforoTocha.tryAcquire()) {
			if (!temTocha) {
				temTocha = true;
				speed += 2; // Aumenta a velocidade após pegar a tocha
				System.out.println("O cavaleiro #" + idCavaleiro + " pegou a tocha! Nova velocidade: " + speed);
			}
			semaforoTocha.release();
		}
	}

	private void PegaPedra() {
		if (!temPedra && semaforoPedra.tryAcquire()) {
			temPedra = true; // Marca que o cavaleiro agora tem a pedra
			speed += 2; // Aumenta a velocidade após pegar a pedra
			System.out.println("O cavaleiro #" + idCavaleiro + " pegou a pedra! Nova velocidade: " + speed);
		}

		semaforoPedra.release();

	}

	private void EscolhePorta() {
		try {
			semaforoPorta.acquire();
			System.out.println("O cavaleiro #" + idCavaleiro + " chegou na porta");
			int portaEscolhida = rnd.nextInt(4);
			if (portaEscolhida == portaCerta) {
				System.out.println("O cavaleiro #" + idCavaleiro + " vai viver mais um dia...");
			} else {
				System.out.println("O cavaleiro #" + idCavaleiro + " foi pego pelo monstro...");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			semaforoPorta.release();
		}
	}
}