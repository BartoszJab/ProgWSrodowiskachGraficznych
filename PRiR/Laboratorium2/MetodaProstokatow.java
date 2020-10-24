package Laboratorium2;

public class MetodaProstokatow extends Thread{
    double xp, xk, dx, calka;
    int n;

    MetodaProstokatow(double xp, double xk, int n){
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
        for (int i=1; i<=n; i++) {
            calka += func(xp + i * dx);
        }
        calka *= dx;
    }

    public double func(double x) {
        return x * x;
    }
}
