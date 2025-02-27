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
        HashMap<String, ArrayList<Integer>> target = readMatrixFromFile("C:\\Users\\Casper\\IdeaProjects\\SE375LAB1\\src\\target");
        HashMap<String, ArrayList<Integer>> filter1 = readMatrixFromFile("C:\\Users\\Casper\\IdeaProjects\\SE375LAB1\\src\\filter1");
        HashMap<String, ArrayList<Integer>> filter2 = readMatrixFromFile("C:\\Users\\Casper\\IdeaProjects\\SE375LAB1\\src\\filter2");
        HashMap<String, ArrayList<Integer>> filter3 = readMatrixFromFile("C:\\Users\\Casper\\IdeaProjects\\SE375LAB1\\src\\filter3");
        HashMap<String, ArrayList<Integer>> matrix = new HashMap<>(target);
        matrix = applyFilter(matrix, filter1);
        matrix = applyFilter(matrix, filter2);
        matrix = applyFilter(matrix, filter3);
        updateGridLayout(matrix);
    }
    public static HashMap<String, ArrayList<Integer>> readMatrixFromFile(String filename){
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
            HashMap<String, ArrayList<Integer>> filters = new HashMap<>();
            int counter = 0;
            for(int i=0;i<5;i++){
                ArrayList <Integer> temp = new ArrayList<>();
                for(int j=0;j<5;j++){
                    temp.add(list.get(counter));
                    counter++;
                }
                filters.put(String.valueOf(i), temp);
            }
            //System.out.println(filters);
            return filters;
        }catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public static HashMap<String, ArrayList<Integer>> applyFilter(HashMap<String, ArrayList<Integer>> lastMatrix, HashMap<String, ArrayList<Integer>> filterMatrix){
        for (String key : lastMatrix.keySet()) {
            if (filterMatrix.containsKey(key)) {
                for (int i = 0; i < 5; i++) {
                    lastMatrix.get(key).set(i, lastMatrix.get(key).get(i) + filterMatrix.get(key).get(i));
                }
            }
        }
        return lastMatrix;
    }

    /* This method updates the frame after each coloring step.

     Hint: After updating a value of an element, you should call this method. */
    public static void updateGridLayout(HashMap<String, ArrayList<Integer>> matrix) {
        int count = 0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                int value = matrix.get(String.valueOf(i)).get(j);
                buttons[i][j].setBackground(getColorForValue(value));
                count++;
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
