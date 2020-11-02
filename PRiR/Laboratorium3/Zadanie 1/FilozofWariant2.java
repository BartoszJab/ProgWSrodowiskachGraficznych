package com.company;

import java.util.concurrent.Semaphore;

/*
    Czterech filozofów w pierwszej kolejności sięga po widelec z lewej strony, a następnie po widelec z prawej
    natomiast jeden z nich czynności te wykonuje w odwrotnej kolejności
 */

public class FilozofWariant2 extends Filozof {
    public FilozofWariant2(int nr) {
        super(nr);
    }


    public void run() {
        while(true) {
            // myslenie
            System.out.println("Mysle " + nr);
            try {
                Thread.sleep((long) (5000 * Math.random()));
            } catch (InterruptedException e) {
                System.out.println("Error message: " + e.getMessage());
            }

            if (nr == 0) {
                widelec[(nr + 1) % MAX].acquireUninterruptibly();
                widelec[nr].acquireUninterruptibly();
            } else {
                widelec[nr].acquireUninterruptibly();
                widelec[(nr + 1) % MAX].acquireUninterruptibly();
            }

            // jedzenie
            System.out.println("Zaczyna jesc " + nr);
            try {
                Thread.sleep((long) (3000 * Math.random()));
            } catch (InterruptedException e) {
                System.out.println("Error message: " + e.getMessage());
            }
            System.out.println("Konczy jesc " + nr);

            widelec[nr].release();
            widelec[(nr + 1) % MAX].release();
        }
    }
}
