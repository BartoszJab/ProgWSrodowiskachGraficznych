#include <stdio.h>
#include <mpi.h>
#include <math.h>

double func(double x) {
    return x * x;
}

double licz_pole_trapezu(double a, double b, int iloscTrapezow, int i) {
    double h = (b - a) / iloscTrapezow;
    double pole = fabs(func(a + (i - 1) * h)) + fabs(func(a + i * h));
    return pole/2 * h;
}

int main(int argc, char **argv) {
    // przedzial calkowania
    int a = 1, b = 6;
    
    int ilosc_trapezow, nr_procesu, i;
    int tag = 13;
    double pole_trapezu, suma = 0;
    MPI_Status status;
    
    MPI_Init(&argc, &argv);
    MPI_Comm_rank(MPI_COMM_WORLD, &nr_procesu);
    MPI_Comm_size(MPI_COMM_WORLD, &ilosc_trapezow);
    
    if (nr_procesu == ilosc_trapezow - 1) {
    	i = nr_procesu + 1;
        pole_trapezu = licz_pole_trapezu(a, b, ilosc_trapezow, i);
        suma += pole_trapezu;
        printf("\nNumer procesu = %d\n", nr_procesu);
        printf("Pole aktualnego trapezu %d wynosi %f\n", i, pole_trapezu);
        printf("Obecna suma wynosi %f\n", suma);
        
        MPI_Send(&pole_trapezu, 1, MPI_DOUBLE, nr_procesu - 1, tag, MPI_COMM_WORLD);
        MPI_Send(&suma, 1, MPI_DOUBLE, nr_procesu - 1, tag, MPI_COMM_WORLD);
    }
    
    if (nr_procesu > 0 && nr_procesu < ilosc_trapezow - 1) {
        MPI_Recv(&pole_trapezu, 1, MPI_DOUBLE, nr_procesu + 1, tag, MPI_COMM_WORLD, &status);
        MPI_Recv(&suma, 1, MPI_DOUBLE, nr_procesu + 1, tag, MPI_COMM_WORLD, &status);
        
        i = nr_procesu + 1;
        pole_trapezu = licz_pole_trapezu(a, b, ilosc_trapezow, i);
        suma += pole_trapezu;
        printf("\nNumer procesu = %d\n", nr_procesu);
        printf("Pole aktualnego trapezu %d wynosi %f\n", i, pole_trapezu);
        printf("Obecna suma wynosi %f\n", suma);
        
        MPI_Send(&pole_trapezu, 1, MPI_DOUBLE, nr_procesu -1, tag, MPI_COMM_WORLD);
        MPI_Send(&suma, 1, MPI_DOUBLE, nr_procesu -1, tag, MPI_COMM_WORLD);
    }
    
    if (nr_procesu == 0) {
        MPI_Recv(&pole_trapezu, 1, MPI_DOUBLE, nr_procesu + 1, tag, MPI_COMM_WORLD, &status);
        MPI_Recv(&suma, 1, MPI_DOUBLE, nr_procesu + 1, tag, MPI_COMM_WORLD, &status);
        
        i = nr_procesu + 1;
        pole_trapezu = licz_pole_trapezu(a, b, ilosc_trapezow, i);
        suma += pole_trapezu;
        printf("\nNumer procesu = %d\n", nr_procesu);
        printf("Pole aktualnego trapezu %d wynosi %f\n", i, pole_trapezu);
        printf("Suma wszystkich trapezow wynosi %f\n", suma);
    }
    MPI_Finalize();
    return 0;
    
    
}
