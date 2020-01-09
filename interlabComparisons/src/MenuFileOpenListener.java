
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;

public class MenuFileOpenListener implements ActionListener {

    private final Main mainApp;
    public MenuFileOpenListener(Main app) { mainApp = app; }

    @Override
    public void actionPerformed(ActionEvent e) {

        JFileChooser fc = new JFileChooser();
        if (0 == fc.showOpenDialog(null)) {
            String path = fc.getSelectedFile().getAbsolutePath();
            FileParser fp = new FileParser(path);
            try {
                ArrayList<String[]> data = fp.parse();

                int sz = data.size();

                double XU[][] = new double[sz][2];

                String tmp[][] = new String[sz][];
                for (int i = 0; i < sz; ++i) {
                    String row[] = data.get(i);
                    tmp[i] = row;

                    XU[i][0] = Double.parseDouble(row[1]);
                    XU[i][1] = Double.parseDouble(row[2]);
                }
                mainApp.updateInitialData(tmp);
                mainApp.getCalculator().setInitialData(XU);

            } catch (FileNotFoundException ex) {
                Logger.getLogger(MenuFileOpenListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
