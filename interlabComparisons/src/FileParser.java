
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileParser {

    private final String path;

    public FileParser(String path) { this.path = path; }

    public ArrayList<String[]> parse() throws FileNotFoundException {

        ArrayList<String[]> data = new ArrayList<>();

        Scanner scanner = new Scanner(new File(path));
        while (scanner.hasNextLine()) {
            String s = scanner.nextLine().trim();
            if (!(s.isEmpty() || s.startsWith("#"))) {

                String d[] = s.split("\\s+");
                if (d.length == 3) {

                    // TODO: check if: correct doubles and U values are positive

                    data.add(d);
                } else {
                    //...
                }
            }
        }

        return data;
    }
}
