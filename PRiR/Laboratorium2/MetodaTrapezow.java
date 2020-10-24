package Laboratorium2;

public class MetodaTrapezow extends Thread{
    double xp, xk, dx, calka;
    int n;

    MetodaTrapezow(double xp, double xk, int n){
        this.xp = xp;
        this.xk = xk;
        this.n = n;
    }

    public double getCalka() {
        return calka;
    }

    @Override
    public void run() {
        dx = (xk - xp) / (double)n;

        calka = 0;
        for (int i=1; i<n; i++) {
            calka += func(xp + i * dx);
        }
        calka += (func(xp) + func(xk)) / 2;
        calka *= dx;
    }

    public double func(double x) {
        return x * x;
    }
}
