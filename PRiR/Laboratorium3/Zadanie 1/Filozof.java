package com.company;

import java.util.concurrent.Semaphore;

public class Filozof extends Thread {
    static int MAX = 5;
    static Semaphore[] widelec = new Semaphore[MAX];
    int nr;

    public Filozof(int nr) {
        this.nr = nr;
    }

    public static void ustawMax(int iloscFilozofow) {
        MAX = iloscFilozofow;
        widelec = new Semaphore[MAX];
    }

}
