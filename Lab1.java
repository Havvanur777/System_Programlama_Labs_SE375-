import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

class Coloring extends Thread{
    HashMap<String, ArrayList<Integer>> lastMatrix;
    JButton[][] buttons;
    public Coloring(HashMap<String, ArrayList<Integer>> lastMatrix ,JButton[][] buttons){
        this.lastMatrix = lastMatrix;
        this.buttons = buttons;
    }
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
    public void run(){
        for (int i = 0; i < lastMatrix.size(); i++) {
            for (int j = 0; j < lastMatrix.size(); j++) {

                int value = lastMatrix.get(String.valueOf(i)).get(j);
                buttons[i][j].setBackground(getColorForValue(value));
                buttons[i][j].setText(buttons[i][j].getText()+"T"); // This is the part where threads should write their own signature as text. You should change this "PLACEHOLDER" value.
                try {
                    Thread.sleep(150);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}

class Filters implements Runnable{
    private HashMap<String, ArrayList<Integer>> lastMatrix;
    private HashMap<String, ArrayList<Integer>> filterMatrix;
    public Filters(HashMap<String, ArrayList<Integer>> lastMatrix, HashMap<String, ArrayList<Integer>> filterMatrix){
        this.lastMatrix = lastMatrix;
        this.filterMatrix = filterMatrix;
    }
    public void run(){
        for (String key : lastMatrix.keySet()) {
            if (filterMatrix.containsKey(key)) {
                for (int i = 0; i < 5; i++) {
                    lastMatrix.get(key).set(i, lastMatrix.get(key).get(i) + filterMatrix.get(key).get(i));
                }
            }
        }
    }
}

public class Lab1 {
    public static final int SIZE = 5;
    private static JFrame frame;
    private static JButton[][] buttons;


    public static void main(String[] args) throws InterruptedException {
        initializeGridLayout();
        HashMap<String, ArrayList<Integer>> target = readMatrixFromFile("C:\\Users\\Casper\\OneDrive\\Masaüstü\\System_Programlama_Labs_SE375-\\target");
        HashMap<String, ArrayList<Integer>> filter1 = readMatrixFromFile("C:\\Users\\Casper\\OneDrive\\Masaüstü\\System_Programlama_Labs_SE375-\\filter1");
        HashMap<String, ArrayList<Integer>> filter2 = readMatrixFromFile("C:\\Users\\Casper\\OneDrive\\Masaüstü\\System_Programlama_Labs_SE375-\\filter2");
        HashMap<String, ArrayList<Integer>> filter3 = readMatrixFromFile("C:\\Users\\Casper\\OneDrive\\Masaüstü\\System_Programlama_Labs_SE375-\\filter3");
        HashMap<String, ArrayList<Integer>> matrix = new HashMap<>(target);
        Thread t1 = new Thread(new Filters(matrix ,filter1));
        Thread t2 = new Thread(new Filters(matrix ,filter2));
        Thread t3 = new Thread(new Filters(matrix,filter3));
        Thread t4 = new Coloring(matrix,buttons);
        t1.start();
        t2.start();
        t3.start();
        t1.join();
        t2.join();
        t3.join();
        t4.start();
        frame.revalidate();
        frame.repaint();

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
