package com.company;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class ParallelJulliet extends Thread {
    final static int N = 4096;
    final static int CUTOFF = 175;
    final static int iloscWatkow = 4;
    static int[][] set = new int[N][N];

    public static void main(String[] args) {
        // Calculate set
        long startTime = System.currentTimeMillis();

        ParallelJulliet thread0 = new ParallelJulliet(0);
        ParallelJulliet thread1 = new ParallelJulliet(1);
        ParallelJulliet thread2 = new ParallelJulliet(2);
        ParallelJulliet thread3 = new ParallelJulliet(3);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Wybierz stala 'c' poprzez wpisanie cyfry. Kilka przykladowych ponizej");
        System.out.println("1. -0.123 + 0.745i\n2. c = -0.75\n3. c = -0.390541 - 0.586788i\n4. c = i\n5. c = 0.355 + 0.355i\n6. c = 0.34 - 0.05i\n7. c = -0.4 - 0.59i");

        try {
            int wybor = scanner.nextInt();

            switch (wybor) {
                case 1:
                    ParallelJulliet.ustawStala(-0.123, 0.745);
                    break;
                case 2:
                    ParallelJulliet.ustawStala(-0.75, 0);
                    break;
                case 3:
                    ParallelJulliet.ustawStala(-0.390541, -0.586788);
                    break;
                case 4:
                    ParallelJulliet.ustawStala(0, 1);
                    break;
                case 5:
                    ParallelJulliet.ustawStala(0.355, 0.355);
                    break;
                case 6:
                    ParallelJulliet.ustawStala(0.34, -0.05);
                    break;
                case 7:
                    ParallelJulliet.ustawStala(-0.4, -0.59);
                    break;
                default:
                    break;
            }
        } catch (InputMismatchException e) {
            System.out.println("Error: " + e);
        }


        thread0.start();
        thread1.start();
        thread2.start();
        thread3.start();
        try {
            thread0.join();
            thread1.join();
            thread2.join();
            thread3.join();
        } catch (InterruptedException e) {
            System.out.println("Error message: " + e.getMessage());
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Obliczenia zakończone w czasie " + (endTime - startTime) + " millisekund");

        // wyświetlanie rusunku
        BufferedImage img = new BufferedImage(N, N, BufferedImage.TYPE_INT_ARGB);

        Random random = new Random();
        // Rysowanie pixeli
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                int k = set[i][j];
                float level;

                if (k < CUTOFF) {
                    level = (float) k / CUTOFF;
                } else {
                    level = 0;
                }

                Color c = new Color(level, level, 0);
                img.setRGB(i, j, c.getRGB());
            }
        }
        // zapis do pliku
        try {
            ImageIO.write(img, "PNG", new File("Mandelbrot.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int me;

    public ParallelJulliet(int me) {
        this.me = me;
    }

    private static double cr = -0.75;
    private static double ci = 0;

    public static void ustawStala(double newcr, double newci) {
        cr = newcr;
        ci = newci;
    }

    public void run() {
        int begin = 0, end = 0;
        if (me == 0) {
            begin = 0;
            end = (N / iloscWatkow);
        }
        else if (me == 1) {
            begin = (N / iloscWatkow);
            end = (N / iloscWatkow) * 2;
        }
        else if (me == 2) {
            begin = (N / iloscWatkow) * 2;
            end = (N / iloscWatkow) * 3;
        }
        else if (me == 3) {
            begin = (N / iloscWatkow) * 3;
            end = N;
        }
        // iteracja skladajaca obrazek w calosc
        for (int i = begin; i < end; i++) {
            //
            for (int j = 0; j < N; j++) {
                double zr = (4.0 * i - 2 * N) / N;
                double zi = (4.0 * j - 2 * N) / N;

                int k = 0;
                while (k < CUTOFF && zr * zr + zi * zi < 4.0) {
                    double newr = cr + zr * zr - zi * zi; // x + a * a -  b * b
                    double newi = ci + 2 * zr * zi; // y + 2 * a * b
                    zr = newr;
                    zi = newi;
                    k++;
                }
                set[i][j] = k;
            }

        }

    }
}

