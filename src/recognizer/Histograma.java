/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package recognizer;

import java.awt.Color;
import java.awt.Graphics;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Isaias
 */
public class Histograma extends JDialog{
    private int[] histo;
    private int total;
    private JPanel panel;
    public Histograma(JFrame parent, boolean modal, int[][] matriz){
        super(parent, modal);
        setSize(700, 600);
        setTitle("Histograma");
        setLayout(null);
        panel=new JPanel();
        panel.setBounds(0, 0, 700, 600);
        panel.setBackground(Color.WHITE);
        add(panel);
        setLocationRelativeTo(null);
        setVisible(true);
        histo=new int[256];
        hacerHisto(matriz);
        dibujarHisto();
    }
    public void hacerHisto(int[][] matriz){
        total=matriz.length*matriz[0].length;
        for (int i=0; i<matriz.length; i++) {
            for (int j=0; j<matriz[0].length; j++) {
                histo[matriz[i][j]]++;
            }
        }
    }
    public void dibujarHisto(){
        int c;
        Graphics g=panel.getGraphics();
        g.setColor(Color.BLACK);
        g.drawLine(100, 100, 100, 550);
        g.drawLine(50, 540, 655, 540);
        for (int i=0; i<256; i++) {
            c=histo[i];
            if(c>255)
                c=255;
            g.setColor(new Color(c, 0, 255-c));
            g.fillRect(100+i*2, 540-histo[i], 2, histo[i]);
        }
    }
    public void paint(Graphics g){
        super.paint(g);
        if(histo.length>0)
            dibujarHisto();
    }
}
