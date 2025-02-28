import com.sun.tools.javac.Main;

import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

class Coloring extends Thread{
    HashMap<Integer, ArrayList<Integer>> lastMatrix;
    JButton[][] buttons;
    public Coloring(HashMap<Integer, ArrayList<Integer>> lastMatrix ,JButton[][] buttons){
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
    @Override
    public void run(){
        for (int i = 0; i < lastMatrix.size(); i++) {
            for (int j = 0; j < lastMatrix.size(); j++) {

                int value = lastMatrix.get(i).get(j);
                buttons[i][j].setBackground(getColorForValue(value));
                buttons[i][j].setText(buttons[i][j].getText()+Thread.currentThread().getName());
                try {
                    Thread.sleep(150);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}

class Filters extends Thread{
    HashMap<Integer, ArrayList<Integer>> lastMatrix;
    HashMap<Integer, ArrayList<Integer>> filter;
    JButton[][] buttons;
    public Filters(HashMap<Integer, ArrayList<Integer>> lastMatrix, HashMap<Integer, ArrayList<Integer>> filter , JButton[][] buttons) {
        this.lastMatrix = lastMatrix;
        this.filter = filter;
        this.buttons = buttons;
    }

@Override
    public void run(){
        for (Integer key : lastMatrix.keySet()) {
            if (filter.containsKey(key)) {
                for (int i = 0; i < 5; i++) {
                    lastMatrix.get(key).set(i, lastMatrix.get(key).get(i) + filter.get(key).get(i));
                    buttons[key][i].setText(buttons[key][i].getText()+Thread.currentThread().getName());
                }
            }
        }
    }
}

public class Lab1 {
    public static final int SIZE = 5;
    private static JFrame frame;
    private static JButton[][] buttons;
    static HashMap<Integer, ArrayList<Integer>> target;
    static HashMap<Integer, ArrayList<Integer>> filter1;
    static HashMap<Integer, ArrayList<Integer>> filter2;
    static HashMap<Integer, ArrayList<Integer>> filter3;

    public static void main(String[] args) throws InterruptedException {
        initializeGridLayout();
        target = readMatrixFromFile("C:\\Users\\Casper\\OneDrive\\Masaüstü\\System_Programlama_Labs_SE375-\\target");
        filter1 = readMatrixFromFile("C:\\Users\\Casper\\OneDrive\\Masaüstü\\System_Programlama_Labs_SE375-\\filter1");
        filter2 = readMatrixFromFile("C:\\Users\\Casper\\OneDrive\\Masaüstü\\System_Programlama_Labs_SE375-\\filter2");
        filter3 = readMatrixFromFile("C:\\Users\\Casper\\OneDrive\\Masaüstü\\System_Programlama_Labs_SE375-\\filter3");
        Thread t1 = new  Filters(target,filter1,buttons);
        Thread t2 = new Filters(target,filter2,buttons);
        Thread t3 = new Filters(target,filter3,buttons);
        Thread t4 = new Coloring(target,buttons);
        t1.setName("T1");
        t2.setName("T2");
        t3.setName("T3");
        t4.setName("T4");
        t1.start();
        t2.start();
        t2.join();
        t3.start();
        t1.join();
        t2.join();
        t3.join();
        t4.start();
        frame.revalidate();
        frame.repaint();

    }
    public static HashMap<Integer, ArrayList<Integer>> readMatrixFromFile(String filename){
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
            HashMap<Integer, ArrayList<Integer>> filters = new HashMap<>();
            int counter = 0;
            for(int i=0;i<5;i++){
                ArrayList <Integer> temp = new ArrayList<>();
                for(int j=0;j<5;j++){
                    temp.add(list.get(counter));
                    counter++;
                }
                filters.put(i, temp);
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
