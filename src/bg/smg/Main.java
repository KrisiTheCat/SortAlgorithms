package bg.smg;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;


public class Main extends JFrame {

    private static final String[] SORTING_ALGORITHMS = {"Bubble sort", "Selection sort", "Insertion sort", "Merge sort", "Quick sort", "Heap sort"};

    private JComboBox<String> sortingComboBox;
    private JLabel[] labels;
    private JPanel centerPanel;

    int[] array = {50, 60, 70, 80, 90, 100, 110};
    final static int ARRAY_SIZE = 7;
    public Main() {
        setTitle("Sorting Homework");
        setSize(650, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - 650) / 2;
        int y = (screenSize.height - 200) / 2;
        setLocation(x, y);

        randomize();

        JPanel topPanel = new JPanel();
        sortingComboBox = new JComboBox<>(SORTING_ALGORITHMS);
        topPanel.add(sortingComboBox);

        JButton sortButton = new JButton("Sort");
        sortButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedAlgorithm = (String) sortingComboBox.getSelectedItem();
                if(selectedAlgorithm.equals("Bubble sort"))
                    bubbleSort(array, ARRAY_SIZE);
                else if(selectedAlgorithm.equals("Selection sort"))
                    selectionSort(array, ARRAY_SIZE);
                else if(selectedAlgorithm.equals("Insertion sort"))
                    insertionSort(array, ARRAY_SIZE);
                else if(selectedAlgorithm.equals("Merge sort"))
                    mergeSort(array, 0, ARRAY_SIZE-1);
                else if(selectedAlgorithm.equals("Quick sort"))
                    quickSort(array, 0, ARRAY_SIZE-1);
                else if(selectedAlgorithm.equals("Heap sort"))
                    heapSort(array, ARRAY_SIZE);
                displayImages();
            }
        });
        topPanel.add(sortButton);

        JButton randButton = new JButton("Randomize");
        randButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                randomize();
                displayImages();
            }
        });
        topPanel.add(randButton);

        add(topPanel, BorderLayout.NORTH);

        centerPanel = new JPanel();
        labels = new JLabel[7];
        displayImages();
        add(centerPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    public void randomize() {
        Random random = new Random();
        int n = array.length;
        for (int i = 0; i < n; i++) {
            int randomIndex = random.nextInt(i + 1);
            int temp = array[randomIndex];
            array[randomIndex] = array[i];
            array[i] = temp;
        }
    }
    public void displayImages() {
        centerPanel.removeAll();
        int width = 10;
        for (int i = 0; i < 7; i++) {
            try {
                BufferedImage originalImage = ImageIO.read(new File("res/ball.png"));
                Image scaledImage = originalImage.getScaledInstance(array[i], array[i], Image.SCALE_DEFAULT);
                JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
                labels[i] = new JLabel(new ImageIcon(scaledImage));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            centerPanel.add(labels[i]);
        }
        centerPanel.revalidate();
        centerPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }

    /*SORT ALGORITHMS*/
    void swap(int[] arr, int i, int j)
    {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    void bubbleSort(int[] arr, int n){
        int temp;
        for(int i = 0; i < n-1; i++){
            for(int j = 0; j < n - i - 1; j++){
                if(arr[j] > arr[j+1]){
                    temp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = temp;
                }
            }
        }
    }
    void selectionSort(int[] arr, int n){
        int temp, minId;
        for(int i = 0; i < n-1; i++){
            minId = i;
            for(int j = i+1; j < n - 1; j++){
                if(arr[j] < arr[minId]){
                    minId = j;
                }
            }
            if(minId!=i){
                temp = arr[i];
                arr[i] = arr[minId];
                arr[minId] = temp;
            }
        }
    }
    void insertionSort(int[] arr, int n){
        int temp, j;
        for(int i = 0; i < n; i++){
            temp = arr[i];
            for(j = i-1; j >=0 && arr[j]>temp; j--){
                arr[j+1] = arr[j];
            }
            arr[j+1] = temp;
        }
    }

    int partition(int arr[], int l, int r){
        int pivot = arr[r];
        int pivotId = l-1;
        for(int i = l; i <= r; i++){
            if(arr[i] < pivot){
                pivotId++;
                swap(arr,i, pivotId);
            }
        }
        pivotId++;
        swap(arr,r, pivotId);
        return pivotId;
    }
    void quickSort(int arr[], int l, int r){
        if(l<r){
            int part = partition(arr,l,r);
            quickSort(arr,l,part-1);
            quickSort(arr,part+1,r);
        }
    }

    void heapSort(int arr[], int n){
        for(int i = n/2-1; i>= 0; i--){
            heapify(arr,n,i);
        }
        for(int i = n-1; i>= 0; i--){
            swap(arr, 0, i);
            heapify(arr,i,0);
        }
    }
    void heapify(int arr[], int n, int i){
        int largest = i;
        int l = i*2+1;
        int r = i*2+2;
        if(l<n && arr[l]>arr[i]){
            largest = l;
        }
        if(r<n && arr[r]>arr[i]){
            largest = r;
        }
        if(largest != i){
            swap(arr,largest, i);
            heapify(arr, n, largest);
        }
    }

    void merge(int arr[], int l, int m, int r){
        int n1 = m-l+1;
        int n2 = r-m;
        int[] A = new int[n1];
        int[] B = new int[n2];
        for(int i = l; i <=m; i++) A[i-l] = arr[i];
        for(int i = m+1; i <=r; i++) B[i-m-1] = arr[i];
        int i = 0;
        int j = 0;
        for(int k = l; k <= r; k++) {
            if (j == n2) {
                arr[k] = A[i];
                i++;
            } else if(i==n1){
                arr[k] = B[j];
                j++;
            } else if (A[i] < B[j]) {
                arr[k] = A[i];
                i++;
            } else {
                arr[k] = B[j];
                j++;
            }
        }
    }
    void mergeSort(int arr[], int l, int r){
        if(l < r){
            int m = (l+r)/2;
            mergeSort(arr, l, m);
            mergeSort(arr, m+1, r);
            merge(arr,l,m,r);
        }
    }
}
