package bg.smg;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;


public class Main extends JFrame {

    private static final String[] SORTING_ALGORITHMS = {"Bubble sort", "Selection sort", "Insertion sort", "Merge sort", "Quick sort", "Heap sort"};

    private JComboBox<String> sortingComboBox;
    private JLabel[] labels;
    private JPanel centerPanel;
    private MainPanel mainPanel;
    int[] array = {50, 60, 70, 80, 90, 100, 110, 120, 130};
    final static int ARRAY_SIZE = 9;
    public Main() {
        setTitle("Sorting Homework");
        setSize(930, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - 930) / 2;
        int y = (screenSize.height - 250) / 2;
        setLocation(x, y);

        mainPanel = new MainPanel(array);

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
                    heapSort(array);
                mainPanel.repaint();
            }
        });
        topPanel.add(sortButton);

        JButton randButton = new JButton("Randomize");
        randButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                randomize();
                mainPanel.queue.add(array);
                mainPanel.repaint();
            }
        });
        topPanel.add(randButton);

        add(topPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
    int kr = 10;
    private void updateProgress(int[] arr) {
        mainPanel.queue.add(Arrays.copyOf(arr, ARRAY_SIZE));
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
                    updateProgress(arr);
                }
            }
        }
    }
    void selectionSort(int[] arr, int n){
        int temp, minId;
        for(int i = 0; i < n; i++){
            minId = i;
            for(int j = i+1; j < n; j++){
                if(arr[j] < arr[minId]){
                    minId = j;
                }
            }
            if(minId!=i){
                temp = arr[i];
                arr[i] = arr[minId];
                arr[minId] = temp;
            }
            updateProgress(arr);
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
            updateProgress(arr);
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
        updateProgress(arr);
        return pivotId;
    }
    void quickSort(int arr[], int l, int r){
        if(l<r){
            int part = partition(arr,l,r);
            quickSort(arr,l,part-1);
            quickSort(arr,part+1,r);
        }
    }

    public void heapSort(int arr[]) {
        int N = arr.length;
        for (int i = N / 2 - 1; i >= 0; i--)
            heapify(arr, N, i);
        updateProgress(arr);
        for (int i = N - 1; i > 0; i--) {
            int temp = arr[0];
            arr[0] = arr[i];
            arr[i] = temp;

            heapify(arr, i, 0);
            updateProgress(arr);
        }
    }

    void heapify(int arr[], int n, int i) {
        int largest = i;
        int l = 2 * i + 1;
        int r = 2 * i + 2;

        if (l < n && arr[l] > arr[largest])
            largest = l;

        if (r < n && arr[r] > arr[largest])
            largest = r;

        if (largest != i) {
            int swap = arr[i];
            arr[i] = arr[largest];
            arr[largest] = swap;

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
        updateProgress(arr);
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
