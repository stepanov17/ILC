
import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;


public class Main {

    private JFrame      frame;
    private JTable      dataTable;
    private JScrollPane scrollPane;

    private final static int WIDTH  = 900;
    private final static int HEIGHT = 500;

    private final Calculator calculator;

    public Calculator getCalculator() { return calculator; }

    private void createUI() {

        frame = new JFrame("Interlab Comparisons 0.1");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // menu
        JMenuBar menuBar = new JMenuBar();

        JMenu menuFile = new JMenu("File");
        menuFile.setMnemonic(KeyEvent.VK_F);
        menuBar.add(menuFile);
        JMenuItem menuFileOpen = new JMenuItem("Open", KeyEvent.VK_O);
        menuFileOpen.addActionListener(new MenuFileOpenListener(/*calculator*/this));
        menuFile.add(menuFileOpen);

        JMenuItem menuFileSave = new JMenuItem("Save", KeyEvent.VK_S);
        //menuFileSave.addActionListener(new MenuFileSaveListener(this));
        menuFile.add(menuFileSave);

        menuFile.addSeparator();
        JMenuItem menuFileExit = new JMenuItem("Exit", KeyEvent.VK_X);
        menuFileExit.addActionListener(new MenuFileExitListener());
        menuFile.add(menuFileExit);

        frame.setJMenuBar(menuBar);

        JMenu menuCalc = new JMenu("Calculate");
        menuCalc.setMnemonic(KeyEvent.VK_C);
        menuBar.add(menuCalc);

        JMenuItem menuCalcRun = new JMenuItem("Run", KeyEvent.VK_R);
        menuCalcRun.addActionListener(new MenuCalcRunListener(this));
        menuCalc.add(menuCalcRun);

        JMenuItem menuCalcAlg = new JMenuItem("Settings", KeyEvent.VK_S);
        menuCalcAlg.addActionListener(new MenuCalcAlgListener(this));
        menuCalc.add(menuCalcAlg);


        // data table
        dataTable = new JTable();

        //https://stackoverflow.com/questions/17627431/auto-resizing-the-jtable-column-widths ?
        dataTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        frame.setLayout(new BorderLayout());
        scrollPane = new JScrollPane(dataTable);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(dataTable.getTableHeader(), BorderLayout.NORTH);

        frame.setSize(WIDTH, HEIGHT);
        frame.setVisible(true);
    }

    public Main() throws InterruptedException, InvocationTargetException {

        calculator = new Calculator();
        SwingUtilities.invokeAndWait(() -> { createUI(); });
    }

    private void updateTable(String data[][], String header[]) {
        TableModel tableModel = new DefaultTableModel(data, header);
        dataTable.setModel(tableModel);
    }

    public void updateInitialData(String data[][]) {
        String header[] = {"ID", "X", "U"};
        updateTable(data, header);
    }

    public void addTableColumn(String name, Object data[]) {
        ((DefaultTableModel) dataTable.getModel()).addColumn(name, data);
    }

    public void removeTableExtraColumns() {
        ((DefaultTableModel) dataTable.getModel()).setColumnCount(3);
    }

    public double[][] getInitialDataFromTable() {

        // TODO
        //double data[][] = {};

        TableModel model = dataTable.getModel();
        int rows = model.getRowCount();
        int cols = model.getColumnCount();
        if (cols < 3) {
            // TODO
        }

        double data[][] = new double[rows][2];
        for (int i = 0; i < rows; ++i) {
            data[i][0] = Double.parseDouble(model.getValueAt(i, 1).toString());
            data[i][1] = Double.parseDouble(model.getValueAt(i, 2).toString());
        }

        return data;
    }

    public static void main(String[] args)
            throws InterruptedException, InvocationTargetException {
        new Main();
    }
}
