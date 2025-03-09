import java.awt.*;
import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

class FilterThread extends Thread{
    HashMap<Integer, ArrayList<Integer>> lastMatrix;
    HashMap<Integer, ArrayList<Integer>> filter;
    public FilterThread(HashMap<Integer, ArrayList<Integer>> lastMatrix, HashMap<Integer, ArrayList<Integer>> filter, String name) {
        super(name);
        this.lastMatrix = lastMatrix;
        this.filter = filter;
    }

    public void run(){
        for (Integer key : lastMatrix.keySet()) {
            if (filter.containsKey(key)) {
                for (int i = 0; i < 5; i++) {
                    lastMatrix.get(key).set(i, lastMatrix.get(key).get(i) + filter.get(key).get(i));
                    GUIThread.buttons[key][i].setText(GUIThread.buttons[key][i].getText()+Thread.currentThread().getName());
                }
            }
        }
    }
}

class GUIThread extends Thread{
    public static final int SIZE = 5;
    static JFrame frame;
    static JButton[][] buttons;
    public void run(){
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
        frame.setSize(600, 600);
        frame.setVisible(true);
    }
}

class ColorThread extends Thread{
    HashMap<Integer, ArrayList<Integer>> lastMatrix;
    public ColorThread(HashMap<Integer, ArrayList<Integer>> lastMatrix, String name){
        super(name);
        this.lastMatrix = lastMatrix;
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
                int value = lastMatrix.get(i).get(j);
                GUIThread.buttons[i][j].setBackground(getColorForValue(value));
                GUIThread.buttons[i][j].setText(GUIThread.buttons[i][j].getText()+Thread.currentThread().getName());
                try {
                    Thread.sleep(150);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}


class Operator extends GUIThread{
    HashMap<Integer, ArrayList<Integer>> target;
    static HashMap<Integer, ArrayList<Integer>> filter1;
    static HashMap<Integer, ArrayList<Integer>> filter2;
    static HashMap<Integer, ArrayList<Integer>> filter3;
    public static HashMap<Integer, ArrayList<Integer>> readMatrixFromFile(String filename){
        try{
            File file = new File(filename);
            Scanner scanner = new Scanner(file);
            ArrayList<Integer> list = new ArrayList<>();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
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
            return filters;
        }catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public void operate() throws InterruptedException, IOException {
        GUIThread gui = new GUIThread();
        gui.start();
        gui.join();
        target = readMatrixFromFile("C:\\Users\\Casper\\IdeaProjects\\SE375LABS\\target");
        filter1 = readMatrixFromFile("C:\\Users\\Casper\\IdeaProjects\\SE375LABS\\filter1");
        filter2 = readMatrixFromFile("C:\\Users\\Casper\\IdeaProjects\\SE375LABS\\filter2");
        filter3 = readMatrixFromFile("C:\\Users\\Casper\\IdeaProjects\\SE375LABS\\filter3");
        Thread t1 = new FilterThread(target,filter1,"T1");
        Thread t2 = new FilterThread(target,filter2,"T2");
        Thread t3 = new FilterThread(target,filter3,"T3");
        Thread t4 = new ColorThread(target,"T4");
        long startTime = System.nanoTime();
        t1.start();
        //t1.join();
        t2.start();
        //t2.join();
        t3.start();
        //t3.join();
        t1.join();
        t2.join();
        t3.join();
        t4.start();
        t4.join();

        long endTime = System.nanoTime();
        double executionTime =(double) (endTime - startTime) / 1000000000;

        GUIThread.frame.revalidate();
        GUIThread.frame.repaint();

        FileWriter writer = new FileWriter("outputMatrix.txt",true);
        for (Map.Entry<Integer, ArrayList<Integer>> entry : target.entrySet()) {
            ArrayList<Integer> value = entry.getValue();
            for (Integer num : value) {
                writer.write(num + " ");
            }
            writer.write(" ");
            writer.write("\n");

        }
        writer.write("Total Execution Time: " + executionTime + " s\n");
        writer.write("\n");
        writer.close();
    }
}

public class Lab1 {
    public static void main(String[] args) throws InterruptedException, IOException {
        Operator o1 = new Operator();
        for(int i=0 ; i<10;i++){
            o1.operate();
        }
    }
}

