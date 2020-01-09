
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuCalcRunListener implements ActionListener {

    private final Main mainApp;
    private final Calculator calc;
    public MenuCalcRunListener(Main app) {
        mainApp = app;
        calc = mainApp.getCalculator();
    }

    private String format(double x) {
        return String.format("%." + calc.getNDigs() + "f", x);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // TODO: update data from the app table!!
        calc.setInitialData(mainApp.getInitialDataFromTable());

        calc.calculateRef();

        // TODO: nDigs setup
        // String.format("%.2f", x)

        if (calc.isCalculated()) {

            mainApp.removeTableExtraColumns();

            boolean chi2 = calc.getChi2();

            if (chi2) {

                mainApp.addTableColumn("Xref", new Object[]{format(calc.getXRef())});
                mainApp.addTableColumn("Uref", new Object[]{format(calc.getURef())});
                mainApp.addTableColumn("Chi2", new Object[]{"+"});

                int n = calc.getN();
                Object tmp[] = new Object[n];
                for (int i = 0; i < n; ++i) { tmp[i] = format(calc.getD()[i]); }
                mainApp.addTableColumn("D", tmp);
                for (int i = 0; i < n; ++i) { tmp[i] = format(calc.getUD()[i]); }
                mainApp.addTableColumn("UD", tmp);
                for (int i = 0; i < n; ++i) { tmp[i] = (calc.getEn()[i] ? "+" : "-"); }
                mainApp.addTableColumn("En", tmp);

            } else {

                mainApp.addTableColumn("Chi2", new Object[]{"-"});

                switch (calc.getAlgorithmForInconsistent()) {
                    case NONE:
                        break;
                    case REM:
                        mainApp.addTableColumn("LREM", new Object[]{format(calc.getLambdaREM())});
                        int n = calc.getN();
                        Object tmp[] = new Object[n];
                        for (int i = 0; i < n; ++i) { tmp[i] = format(calc.getUREM()[i]); }
                        mainApp.addTableColumn("UREM", tmp);
                        break;
                    case LCS:
                        break;
                    case MCS:
                        break;
                    default:
                        // should not reach here
                        break;
                }
            }
        }
    }
}
