package com.java2d.pracs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.Arrays;

public class WorleyNoise extends JPanel implements ActionListener {

    public Timer timer = new Timer(1, this);
    public static final int WIDTH = 600;
    public static final int HEIGHT = 600;
    public Color[] pixels = new Color[WIDTH * HEIGHT];
    public final int pointSize = 50;
    public Point2D[] points = new Point2D[pointSize];
    public boolean toggle = true;

    WorleyNoise() {
        this.setSize(WIDTH, HEIGHT);
        for (int i = 0; i < pointSize; i++) {
            points[i] = new Point((int) Math.round(Math.random() * WIDTH), (int) Math.round(Math.random() * WIDTH));
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        Runnable runnable = () -> {
            timer.start();
            super.paintComponents(g);

            Graphics2D graphics = (Graphics2D) g;

            for (int x=0; x< WIDTH; x++) {
                for (int y=0; y < HEIGHT; y++) {
                    float[] distance = new float[points.length];
                    for (int i=0; i < points.length; i ++) {
                        float d = (float) points[i].distance(new Point(x, y));
                        distance[i] = d;
                    }
                    Arrays.sort(distance);
                    int n = 0;
                    int index = x + y * WIDTH;
                    float noise = distance[n] / 100;
                    int color = Math.min(Math.round(noise * 255), 255);
                    pixels[index] = new Color(color, color, color);
                }
            }
            updatePixel(graphics);

            for (Point2D point : points) {
//                graphics.setColor(Color.GREEN);
//                graphics.setStroke(new BasicStroke(2));
//                graphics.drawRect((int) Math.round(point.getX()), (int) Math.round(point.getY()), 1, 1);

                if (toggle) point.setLocation(new Point((int) (point.getX() + 2), (int) (point.getY() + 2)));
                else point.setLocation(new Point((int) (point.getX() - 2), (int) (point.getY() - 2)));

                toggle = !toggle;
            }

        };
        runnable.run();
        Toolkit.getDefaultToolkit().sync();
    }

    public float dist(int x1, int y1, int x2, int y2) {
        return (float) Math.sqrt((y2-y1)^2 + (x2-x1)^2);
    }

    public void updatePixel(Graphics2D graphics) {
        for (int x=0; x< WIDTH; x++) {
            for (int y=0; y < HEIGHT; y++) {
                int index = x + y * WIDTH;
                graphics.setColor(pixels[index]);
                graphics.drawRect(x, y, 1, 1);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Runnable runnable = this::repaint;
        runnable.run();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame main = new JFrame();
                WorleyNoise worleyNoise = new WorleyNoise();
                main.setLayout(new BorderLayout());
                main.setTitle("Worley Noise");
                main.add(worleyNoise, BorderLayout.CENTER);
                main.setSize(new Dimension(WIDTH, HEIGHT));
                main.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                main.setVisible(true);
            }
        });
    }
}
