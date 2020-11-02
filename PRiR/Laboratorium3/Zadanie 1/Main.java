package com.company;

import java.awt.*;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class Main {

    public static void main(String[] args) {

        int wybranyNumerWariantu = pobierzNumberWariantu();
        int iloscFilozofow = pobierzIloscFilozofow();
        Filozof.ustawMax(iloscFilozofow);

        for (int i = 0; i < Filozof.MAX; i++) {
            Filozof.widelec[i] = new Semaphore(1);
        }

        for (int i = 0; i < Filozof.MAX; i++) {
            if (wybranyNumerWariantu == 1) {
                new FilozofWariant1(i).start();
            } else if (wybranyNumerWariantu == 2) {
                new FilozofWariant2(i).start();
            } else if (wybranyNumerWariantu == 3) {
                new FilozofWariant3(i).start();
            }
        }


    }

    private static int pobierzNumberWariantu() {
        System.out.println("Wybierz wariant do uruchomienia (podaj cyfre odpowiadajaca wariantowi)");
        System.out.println("Warianty:\n 1. Ograniczenie do 4 liczby filozofów trzymających jednocześnie widelce\n 2. Czterech filozofów w pierwszej kolejności sięga po widelec z lewej strony, a następnie po widelec z prawej\n" +
                "    natomiast jeden z nich czynności te wykonuje w odwrotnej kolejności\n 3. Rzut monety w rozwiązaniu problemu ucztujących Filozofów");

        Scanner wariantUzytkownika = new Scanner(System.in);
        int wybranyWariant = wariantUzytkownika.nextInt();

        while (wybranyWariant != 1 && wybranyWariant != 2 && wybranyWariant != 3) {
            System.out.println("Wybierz wariant 1, 2 lub 3!!");
            wybranyWariant = wariantUzytkownika.nextInt();
        }

        return wybranyWariant;
    }

    private static int pobierzIloscFilozofow() {
        System.out.println("Wybierz ile filozofow chcesz wybrac (od 2 do 100)");

        Scanner wyborIlosciFilozofow = new Scanner(System.in);
        int iloscFilozofow = wyborIlosciFilozofow.nextInt();

        while (iloscFilozofow < 2 || iloscFilozofow > 100) {
            System.out.println("Ilosc filozofow musi znajdywac sie w przedziale 2-100");
            iloscFilozofow = wyborIlosciFilozofow.nextInt();
        }

        return iloscFilozofow;
    }
}
