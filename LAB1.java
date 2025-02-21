import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

public class LAB1 {
    public static final int SIZE = 5;
    private static JFrame frame;
    private static JButton[][] buttons;

    public static void main(String[] args) {
        initializeGridLayout();
        updateGridLayout();
    }
    public static ArrayList<Integer> readMatrixFromFile(String filename){
        try{
            File file = new File(filename);
            Scanner scanner = new Scanner(file);
            ArrayList<Integer> list = new ArrayList<>();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                //System.out.println(line);
                if (!line.isEmpty()) {
                    String[] parts = line.split("\\s+");
                    for (String part : parts) {
                        list.add(Integer.parseInt(part));
                    }
                }
            }
            return list;
        }catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<Integer> applyFilter(){
        ArrayList<Integer> list = readMatrixFromFile("C:\\Users\\Casper\\OneDrive\\Masaüstü\\System_Programlama_Labs_SE375-\\filter1");
        ArrayList<Integer> list2 = readMatrixFromFile("C:\\Users\\Casper\\OneDrive\\Masaüstü\\System_Programlama_Labs_SE375-\\filter2");
        ArrayList<Integer> list3 = readMatrixFromFile("C:\\Users\\Casper\\OneDrive\\Masaüstü\\System_Programlama_Labs_SE375-\\filter3");
        HashMap<String, ArrayList<Integer>> filters = new HashMap<>();
        filters.put("filter1",list);
        filters.put("filter2",list2);
        filters.put("filter3",list3);
        //System.out.println(filters);
        ArrayList<Integer> matrixList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            int toplam = 0;
            for (ArrayList<Integer> matrix : filters.values()) {
                toplam += matrix.get(i); // Aynı indeksteki elemanları topluyor
            }
            matrixList.add(toplam); // Toplamı ekle
        }
        //System.out.println(matrixList);
        return matrixList;
    }

    /* This method updates the frame after each coloring step.

     Hint: After updating a value of an element, you should call this method. */
    public static void updateGridLayout() {
        Random rand = new Random();
        ArrayList<Integer> list = applyFilter();
        //System.out.println("a" + list);
        int count = 0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                int value =  list.get(count);
                count++;
                buttons[i][j].setBackground(getColorForValue(value));
            }
        }
        frame.revalidate();
        frame.repaint();
    }

    // This method assigns values to colors.
    private static Color getColorForValue(int value) {
        return switch (value) {
            case 1 -> Color.RED;
            case 2 -> Color.BLUE;
            case 3 -> Color.GREEN;
            case 4 -> Color.ORANGE;
            case 5 -> Color.MAGENTA;
            case 6 -> Color.CYAN;
            case 7 -> Color.PINK;
            case 8 -> Color.YELLOW;
            default -> Color.WHITE;
        };
    }

    // This is the method where our frame is created.
    public static void initializeGridLayout() {
        frame = new JFrame("Sequential");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(SIZE, SIZE));

        buttons = new JButton[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                JButton button = new JButton();
                button.setEnabled(false);
                buttons[i][j] = button;
                frame.add(button);
            }
        }
        //	updateGridLayout();
        frame.setSize(600, 600);
        frame.setVisible(true);
    }
}

