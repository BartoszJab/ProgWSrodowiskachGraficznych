#include <stdio.h>
#include <mpi.h>
#include <math.h>

double PI(int N) {
    double suma = 0;
    double ulamek;
    for (int n = 1; n <= N; n++) {
        ulamek = pow(-1, n - 1) / (2 * n - 1);
        suma += ulamek;
    }
    
    return 4 * suma;
}

int main(int argc, char **argv) {
    int N, nr_procesu;
    int tag = 10;
    double pi;

    MPI_Status status;
    
    MPI_Init(&argc, &argv);
    MPI_Comm_rank(MPI_COMM_WORLD, &nr_procesu);
    // liczba uruchomionych procesow
    MPI_Comm_size(MPI_COMM_WORLD, &N);
    
    
    // inicializujemy obliczenia w procesie o indexie 0
    if (nr_procesu == 0) {
        pi = PI(nr_procesu + 1);
        printf("\nNumer procesu = %d\n", nr_procesu);
        printf("Aktualna wartosc PI: %f\n", pi);
        // wyslij zmienna do nastepnego procesu
        MPI_Send(&pi, 1, MPI_DOUBLE, nr_procesu + 1, tag, MPI_COMM_WORLD);
    }
    
    if (nr_procesu > 0 && nr_procesu < N - 1) {
        // odbierz zmienna
        MPI_Recv(&pi, 1, MPI_DOUBLE, nr_procesu - 1, tag, MPI_COMM_WORLD, &status);
        
        printf("\nNumer procesu = %d\n", nr_procesu);
        printf("Poprzednia wartosc PI: %f\n", pi);
        pi = PI(nr_procesu + 1);
        printf("Aktualna wartosc PI: %f\n", pi);
        
        MPI_Send(&pi, 1, MPI_DOUBLE, nr_procesu + 1, tag, MPI_COMM_WORLD);
    }
    
    if (nr_procesu == N - 1) {
        MPI_Recv(&pi, 1, MPI_DOUBLE, nr_procesu - 1, tag, MPI_COMM_WORLD, &status);
        printf("\nNumer procesu = %d\n", nr_procesu);
        printf("Poprzednia wartosc PI: %f\n", pi);
        pi = PI(nr_procesu + 1);
        printf("Aktualna wartosc PI: %f\n", pi);
    }
    MPI_Finalize();
    return 0;
    
    
}
