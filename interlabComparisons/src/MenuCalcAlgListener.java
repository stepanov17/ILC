
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class MenuCalcAlgListener implements ActionListener {

    private final Main mainApp;

    public MenuCalcAlgListener(Main app) { mainApp = app; }

    private final static String NONE = "None";
    private final static String REM  = "Random Effect Model";
    private final static String LCS  = "Largest Consistent Subset";
    private final static String MCS  = "Metrological Compatibility Set";

    @Override
    public void actionPerformed(ActionEvent e) {
        String choices[] = {NONE, REM, LCS, MCS};
        String choice = (String) JOptionPane.showInputDialog(
                null,
                "",
                "Evaluation algorithm for inconsistent data",
                JOptionPane.QUESTION_MESSAGE,
                null, // default icon
                choices,
                choices[0]);

        Calculator calc = mainApp.getCalculator();

        if (choice == null) {
            return;
        }

        switch (choice) {
            case REM:
                calc.setAlgorithmForInconsistent(Calculator.AlgorithmForInconsistent.REM);
                break;
            case LCS:
                calc.setAlgorithmForInconsistent(Calculator.AlgorithmForInconsistent.LCS);
                break;
            case MCS:
                calc.setAlgorithmForInconsistent(Calculator.AlgorithmForInconsistent.MCS);
                break;
            default:
                // ?
                break;
        }
    }
}
