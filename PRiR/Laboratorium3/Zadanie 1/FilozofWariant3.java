package com.company;

/*
    Rzut monety w rozwiązaniu problemu ucztujących filozofów
 */

import java.util.Random;
import java.util.concurrent.Semaphore;

public class FilozofWariant3 extends Filozof {
    private Random losuj;

    public FilozofWariant3(int nr) {
        super(nr);
        losuj = new Random(nr);
    }

    public void run() {
        while (true) {
            // myslenie
            System.out.println("Mysle " + nr);
            try {
                Thread.sleep((long) (5000 * Math.random()));
            } catch (InterruptedException e) {
                System.out.println("Error message: " + e.getMessage());
            }

            int strona = losuj.nextInt(2);
            boolean podnioslDwaWidelce = false;
            do {
                if (strona == 0) {
                    widelec[nr].acquireUninterruptibly();

                    if (!(widelec[(nr + 1) % MAX].tryAcquire())) {
                        widelec[nr].release();
                    } else {
                        podnioslDwaWidelce = true;
                    }
                } else {
                    widelec[(nr + 1) % MAX].acquireUninterruptibly();

                    if (!(widelec[nr].tryAcquire())) {
                        widelec[(nr + 1) % MAX].release();
                    } else {
                        podnioslDwaWidelce = true;
                    }
                }
            } while(!podnioslDwaWidelce);

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
