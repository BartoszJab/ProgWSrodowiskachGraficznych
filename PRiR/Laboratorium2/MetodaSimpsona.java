package Laboratorium2;

public class MetodaSimpsona extends Thread{
    double xp, xk, dx, calka, s, x;
    int n;

    MetodaSimpsona(double xp, double xk, int n){
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
        s = 0;
        for (int i=1; i<n; i++) {
            x = xp + i*dx;
            s += func(x - dx / 2);
            calka += func(x);
        }
        s += func(xk - dx / 2);
        calka = (dx/6) * (func(xp) + func(xk) + 2*calka + 4*s);
    }

    public double func(double x) {
        return x * x;
    }
}
