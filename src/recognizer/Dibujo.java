package recognizer;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Isaias
 */
public class Dibujo extends JPanel{
    
    private Graphics g;
    private int x, y;
    private int[][] matriz;
    int m=500, n=550;
    public Dibujo(){
        matriz=null;
    }
    @Override
    public void paint(Graphics g){
        super.paint(g);
        if(matriz.length>0){
            x=(m-matriz.length)/2;
            this.setBackground(new Color(240, 244, 255));
            for (int i=0; i<matriz.length; i++) {
                y=(n-matriz[0].length)/2;
                for (int j=0; j<matriz[0].length; j++) {
                    g.setColor(new Color(matriz[i][j], matriz[i][j], matriz[i][j]));
                    g.fillRect(x, y, 1, 1);
                    y++;
                }
                x++;
            }
        }
    }
    public void setMatriz(int[][] nueva){
        this.matriz=nueva;
    }
    public void setG(Graphics g){
        this.g=g;
    }
}
