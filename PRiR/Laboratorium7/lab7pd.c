#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <time.h>
#include "mpi.h"
#define REZERWA 500
#define PORT 1
#define START 2
#define PLYWANIE 3
#define KONIEC_PLYWANIA 4
#define STANIECIE_NA_OCEANIE 5
#define TANKUJ 5000

int paliwo = 5000;
int CUMUJ_W_PORCIE=1, NIE_CUMUJ_W_PORCIE=0;
int liczba_procesow;
int nr_procesu;
int ilosc_statkow;
int ilosc_stanowisk=4;
int ilosc_zajetych_stanowisk=0;
int tag=5;
int wyslij[2];
int odbierz[2];
MPI_Status mpi_status;

void Wyslij(int nr_statku, int stan) {
        wyslij[0]=nr_statku;
	wyslij[1]=stan;
	MPI_Send(&wyslij, 2, MPI_INT, 0, tag, MPI_COMM_WORLD);
	sleep(1);
}

void Port(int liczba_procesow) {
    int nr_statku, status;
    ilosc_statkow = liczba_procesow - 1;

    printf("Dysponujemy %d wolnymi stanowiskami dla statkow\n", ilosc_stanowisk);
    sleep(2);

    while(ilosc_stanowisk <= ilosc_statkow) {
        MPI_Recv(&odbierz,2,MPI_INT,MPI_ANY_SOURCE,tag,MPI_COMM_WORLD, &mpi_status);
        nr_statku = odbierz[0];
        status = odbierz[1];

        if(status == 1){
	    printf("Samolot %d stoi na stanowisku portowym\n", nr_statku);
        }

        if(status == 2){
	    printf("Statek %d wyplywa z portu ze stanowiska %d\n", nr_statku, ilosc_zajetych_stanowisk);
            ilosc_zajetych_stanowisk--;
        }

        if(status == 3){
            printf("Statek %d plywa po szerokich wodach\n", nr_statku);
        }

        if(status == 4){
            if(ilosc_zajetych_stanowisk < ilosc_stanowisk){
	        ilosc_zajetych_stanowisk++;
	        MPI_Send(&CUMUJ_W_PORCIE, 1, MPI_INT, nr_statku, tag, MPI_COMM_WORLD);
            } else {
	        MPI_Send(&NIE_CUMUJ_W_PORCIE, 1, MPI_INT, nr_statku, tag, MPI_COMM_WORLD);
            }
	}

        if(status==5){
            ilosc_statkow--;
            printf("Ilosc statkow %d\n", ilosc_statkow);
        }
    }

}
void Statek() {
    int stan,suma,i;
    stan = PLYWANIE;
    while(1){
	if(stan == 1){
	    if(rand()%2 == 0) {
	    	paliwo = TANKUJ;
	        stan = START;
	        printf("Prosze o pozwolenie na wyplyniecie, statek numer: %d\n",nr_procesu);
	        Wyslij(nr_procesu, stan);
	    }
	    else{
	        Wyslij(nr_procesu,stan);
	    }
	}
	else if(stan == 2){
	    printf("Wyplynalem, statek numer: %d\n",nr_procesu);
	    stan = PLYWANIE;
	    Wyslij(nr_procesu,stan);
	}
	else if(stan == 3){
	    paliwo -= rand()%500;

	    if(paliwo <= REZERWA){
	        stan = KONIEC_PLYWANIA;
	        printf("Prosze o pozwolenie na cumowanie w porcie\n");
	        Wyslij(nr_procesu,stan);
	    } else {
	        for(i=0; rand()%10000;i++);
	    }
	}
	else if(stan == 4) {
	    int temp;
	    MPI_Recv(&temp, 1, MPI_INT, 0, tag, MPI_COMM_WORLD, &mpi_status);
	    if(temp == CUMUJ_W_PORCIE){
	        stan = PORT;
                printf("Zacumowalem w porcie, samolot numer: %d\n", nr_procesu);
            }
	    else {
	        paliwo -= rand()%500;
	        if(paliwo > 0) {
	            Wyslij(nr_procesu,stan);
	        } else {
	            stan = STANIECIE_NA_OCEANIE;
	            printf("Stracilismy paliwo, dryfujemy na oceanie i plyna do nas rekiny :(\n");
	            Wyslij(nr_procesu,stan);
	            return;
	        }
	    }
        }
    }
}
int main(int argc, char *argv[]) {
    MPI_Init(&argc, &argv);
    MPI_Comm_rank(MPI_COMM_WORLD,&nr_procesu);
    MPI_Comm_size(MPI_COMM_WORLD,&liczba_procesow);
    srand(time(NULL));
    if(nr_procesu == 0)
        Port(liczba_procesow);
    else
        Statek();
    MPI_Finalize();
    return 0;
}
