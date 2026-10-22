package kozhukhova.spline;
/**
 *     lower[i] * y[i-1] + main[i] * y[i] + upper[i] * y[i+1] = rhs[i]
 * lower = cᵢ
 * main = aᵢ
 * upper = bᵢ
 * rhs = dᵢ
 */
public final class TridiagonalSolver {
    private TridiagonalSolver() {
    }

    public static double[] solve(double[] lower, double[] main, double[] upper, double[] rhs) {
        int n = main.length;
        if (lower.length != n || upper.length != n || rhs.length != n) {
            throw new IllegalArgumentException("Все массивы должны иметь одинаковую длину");
        }
        if (n == 0) {
            return new double[0];
        }

        double[] v = new double[n];
        double[] u = new double[n];

        double prevV = 0.0; // предыдущий V, те Vi-1
        double prevU = 0.0;
        for (int i = 0; i < n; i++) {
            double c = (i == 0) ? 0.0 : lower[i];
            double b = (i == n - 1) ? 0.0 : upper[i];

            double denominator = c * prevV + main[i]; // знаменатель из 30 формулы
            if (denominator == 0.0) {
                throw new ArithmeticException("Нулевой знаменатель в методе прогонки, строка " + i);
            }

            v[i] = -b / denominator;
            u[i] = (rhs[i] - c * prevU) / denominator;

            prevV = v[i];
            prevU = u[i];
        }

        double[] y = new double[n];
        y[n - 1] = u[n - 1];
        for (int i = n - 2; i >= 0; i--) {
            y[i] = v[i] * y[i + 1] + u[i];
        }
        return y;
    }
}
