
import java.util.ArrayList;
import java.util.TreeMap;

// TODO: singleton?

public class Calculator {

    private double X[];
    private double U[];

    private int n;
    public int getN() { return n; }

    private int nDigs = 6;
    public void setNDigs(int nDigs) { this.nDigs = nDigs; }
    public int  getNDigs() { return nDigs; }

    private boolean calculated = false;
    public boolean isCalculated() { return calculated; }

    private boolean chi2passed = false;
    public boolean getChi2() { return chi2passed; }

    private double xRef = 0.;
    private double uRef = 0.;
    public double getXRef() { return xRef; }
    public double getURef() { return uRef; }

    private double D[];
    private double UD[];
    private boolean En[];
    public double[]  getD () { return D;  }
    public double[]  getUD() { return UD; }
    public boolean[] getEn() { return En; }

    // === REM ===

    private double U_REM[];
    public double[] getUREM() { return U_REM; }

    private double lambda = 0.;
    public double getLambdaREM() { return lambda; }

    // === inconsistent data processing algorithm ===

    public enum AlgorithmForInconsistent {
        NONE, // none
        REM,  // random effect model
        LCS,  // largest consistent subset
        MCS   // metrological compatibility set
    };
    private AlgorithmForInconsistent alg = AlgorithmForInconsistent.NONE; // default
    public void setAlgorithmForInconsistent(AlgorithmForInconsistent alg) { this.alg = alg; }
    public AlgorithmForInconsistent getAlgorithmForInconsistent() { return alg; }


    private void checkUArePositive(double U[]) {

        // TODO: forbid incorrect modification in the table??

        for (int i = 0; i < U.length; ++i) {
            if (U[i] <= 0.) {
                // TODO
            }
        }
    }

    public void setInitialData(double XU[][]) {

        n = XU.length;
        X = new double[n];
        U = new double[n];
        for (int i = 0; i < n; ++i) {
            if (XU[i].length < 2) {
                // TODO: error
            }
            X[i] = XU[i][0];
            U[i] = XU[i][1];
        }

        checkUArePositive(U);
    }

    // expected: x and u are checked (equal x and u lengths, positive u)
    private double[] getRef(double x[], double u[]) {

        double res[] = new double[2];

        double s = 0.;
        for (double uv: u) { s += 1. / (uv * uv); }
        double uRef2 = 1. / s;
        res[1] = Math.sqrt(uRef2);

        s = 0.;
        for (int i = 0; i < x.length; ++i) {
            s += x[i] * uRef2 / (u[i] * u[i]);
        }
        res[0] = s;

        return res;
    }


    // weighted mean
    public void calculateRef() {

        calculated = false;

        if (X.length < 1 || U.length < 1) {
            // TODO: warning msg
            return;
        }

        calculated = true;

        double ref[] = getRef(X, U);
        xRef = ref[0];
        uRef = ref[1];

        chi2passed = Chi2.test(X, U, xRef);

        if (chi2passed) {
            checkEn(X, xRef, U, uRef);
        } else {
            switch (alg) {
                case NONE:
                    break;
                case REM:
                    ref = REM();
                    xRef = ref[0];
                    uRef = ref[1];
                    checkEn(X, xRef, U_REM, uRef);
                    break;
                case LCS:
                    break;
                case MCS:
                    break;
                default:
                    break;
            }
        }

    }

    private void checkEn(double x[], double xref, double u[], double uref) {

        D = new double[n];
        UD = new double[n];
        En = new boolean[n];
        for (int i = 0; i < n; ++i) {
            D[i] = x[i] - xref;
            UD[i] = Math.sqrt(u[i] * u[i] - uref * uref);
            En[i] = !(0.5 * Math.abs(D[i]) / UD[i] > 1.);
        }
    }


    private double[] REM() {

        double W[] = new double[n];
        double w1 = 0., w2 = 0., xw = 0.;
        for (int i = 0; i < n; ++i) {
            double w = 1. / (U[i] * U[i]);
            W[i] = w;
            xw += (w * X[i]);
            w1 +=  w;
            w2 += (w * w);
        }
        xw /= w1;

        lambda = 1. - n;
        for (int i = 0; i < n; ++i) {
            double dx = X[i] - xw;
            lambda += (W[i] * dx * dx);
        }
        if (lambda < 0.) { lambda = 0.; }
        lambda /= (w1 - w2 / w1);

        U_REM = new double[n];
        for (int i = 0; i < n; ++i) {
            U_REM[i] = Math.sqrt(U[i] * U[i] + lambda);
        }

        return getRef(X, U_REM);
    }

    class LCSResult {
        public boolean found = false;
        public double value = Double.NEGATIVE_INFINITY;
        public int indices[] = new int[]{};
    }


    private LCSResult LCS(double x[], double u[], int ind[]) {

        LCSResult res = new LCSResult();

        int l = x.length;
        if (l < 1) { return res; }

        double xr = getRef(x, u)[0];
        if (Chi2.test(x, u, xr)) {
            res.found = true;
            res.value = Chi2.getChi2(x, u, xr);
            res.indices = ind;
            return res;
        }

        //map length of subres indices length -> res array list
        TreeMap<Integer, ArrayList<LCSResult>> resMap = new TreeMap<>();

        // initialise
        for (int i = 1; i < l; ++i) { resMap.put(i, new ArrayList<>()); }

        for (int i = 0; i < l; ++i) {
            double subx[] = new double[l - 1];
            double subu[] = new double[l - 1];
            int  subind[] = new int   [l - 1];
            int ii = 0;
            for (int j = 0; j < l; ++j) {
                if (j != i) {
                    subx[ii] = x[j];
                    subu[ii] = u[j];
                    subind[ii] = ind[j];
                    ++ii;
                }
            }
            LCSResult r = LCS(subx, subu, subind);
            if (r.found) {
                int nConsistent = r.indices.length;
                resMap.get(nConsistent).add(r);
            }
        }

        for (int i = l - 1; i > 0; --i) {
            ArrayList<LCSResult> results = resMap.get(i);
            int nRes = results.size();
            if (nRes > 0) {
                int i0 = 0;
                double s0 = results.get(i0).value;
                for (int j = 1; j < nRes; ++j) {
                    double v = results.get(j).value;
                    if (v < s0) {
                        s0 = v;
                        i0 = j;
                    }
                }
                return results.get(i0);
            }
        }

        return res;
    }
}
