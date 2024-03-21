package bg.smg;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class MainPanel extends JPanel {
    Queue<int[]> queue =  new LinkedList<>();
    int[] last = null;

    public MainPanel(int[] array) {
        queue.add(array);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        System.out.println(queue.size());
        int[] array;
        if(queue.isEmpty()){
            array = last;
        }
        else {
            array = queue.poll();
        }
        System.out.println(array[0]);
        int width = 10;
        for (int i = 0; i < Main.ARRAY_SIZE; i++) {
            try {
                BufferedImage originalImage = ImageIO.read(new File("res/ball.png"));
                g.drawImage(originalImage, width, 0, array[i], array[i], null);
                width += 10 + array[i];
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!queue.isEmpty()) repaint();
            }
        });
        timer.start();
        last = array;
    }
}
