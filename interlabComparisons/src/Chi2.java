
public class Chi2 {

    // 1 to 40
    private static double CRIT[] = {
           3.841 // 1
        ,  5.991 // 2
        ,  7.815 // 3
        ,  9.448 // 4
        , 11.070 // 5
        , 12.592 // 6
        , 14.067 // 7
        , 15.507 // 8
        , 16.919 // 9
        , 18.307 // 10
        , 19.675 // 11
        , 21.026 // 12
        , 22.362 // 13
        , 23.685 // 14
        , 24.996 // 15
        , 26.296 // 16
        , 27.587 // 17
        , 28.869 // 18
        , 30.144 // 19
        , 31.410 // 20
        , 32.671 // 21
        , 33.924 // 22
        , 35.172 // 23
        , 36.415 // 24
        , 37.652 // 25
        , 38.885 // 26
        , 40.113 // 27
        , 41.337 // 28
        , 42.557 // 29
        , 43.773 // 30
        , 44.985 // 31
        , 46.194 // 32
        , 47.400 // 33
        , 48.602 // 34
        , 49.802 // 35
        , 50.998 // 36
        , 52.192 // 37
        , 53.384 // 38
        , 54.572 // 39
        , 55.758 // 40
    };

    public static double getChi2(double x[], double u[], double v) {

        int n = x.length;
        if (u.length != n) {
            // TODO
        }

        if (n < 2) { // at least two results
            // TODO
        }

        // TODO: n > 41 ?

        double s = 0.;
        for (int i = 0; i < n; ++i) {
            s += ((x[i] - v) * (x[i] - v)) / (u[i] * u[i]);
        }

        return s;
    }

    public static boolean test(double x[], double u[], double v) {

        double s = getChi2(x, u, v);
        return (s <= CRIT[x.length - 1]);
    }
}
