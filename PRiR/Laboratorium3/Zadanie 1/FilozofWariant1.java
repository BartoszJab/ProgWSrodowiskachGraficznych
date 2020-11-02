package com.company;

import java.util.concurrent.Semaphore;

public class FilozofWariant1 extends Filozof {
    public FilozofWariant1(int nr) {
        super(nr);
    }



    public void run() {
        while(true) {
            // myslenie
            System.out.println("Mysle " + nr);
            try {
                Thread.sleep((long) (7000 * Math.random()));
            } catch (InterruptedException e) {
                System.out.println("Error message: " + e.getMessage());
            }
            widelec[nr].acquireUninterruptibly(); // przechwycenie lewego widelca
            widelec[(nr + 1) % MAX].acquireUninterruptibly(); // przechwycenie prawego widelca

            // jedzenie
            System.out.println("Zaczyna jesc " + nr);
            try {
                Thread.sleep((long) (5000 * Math.random()));
            } catch (InterruptedException e) {
                System.out.println("Error message: " + e.getMessage());
            }
            System.out.println("Konczy jesc " + nr);

            widelec[nr].release(); // zwolnienie lewego widelca
            widelec[(nr + 1) % MAX].release(); // zwolnienie prawego widelca
        }
    }
}
