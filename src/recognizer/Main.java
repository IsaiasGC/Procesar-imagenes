/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package recognizer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Isaias
 */
public class Main extends JFrame implements ActionListener{
    
    private Dibujo dibujos;
    private Procesador procesar;
    private File imagen;
    private JFrame esta;
    private JLabel original;
    private JButton histograma, pasaBaja, pasaAlta, pasaBanda, negativo,
            quitarRuido, contorno, mediana, sobel;
    private int[][] matriz;
    private int m, n;
    JPanel panel;
    
    public Main(){
        esta=this;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setSize(500, 500);
        setTitle("Reconocedor de perros");
        pedirImagen();
    }
    public void pedirImagen(){
        panel=new JPanel();
        panel.setLayout(new FlowLayout());
        setLayout(null);
        JFileChooser selector=new JFileChooser(getClass().getResource("").getPath()+"../../../dataset");
        selector.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileNameExtensionFilter filtro = new FileNameExtensionFilter("Imagenes JPG", "jpg");
        selector.setFileFilter(filtro);
        
        JButton iniciar=new JButton("Iniciar");
        iniciar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selector.showOpenDialog(esta);
                imagen=selector.getSelectedFile();
                if ((imagen == null) || (imagen.getName().equals(""))) {
                    JOptionPane.showMessageDialog(null, "Nombre de archivo inválido", "Selecciona una archivo .jpg", JOptionPane.ERROR_MESSAGE);
                    imagen=null;
                }else{
                    esta.setVisible(false);
                    init();
                }
            }
        });
        
        panel.add(iniciar);
        panel.setBounds(0, 0, 500, 500);
        
        this.add(panel);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
    public void init(){
        remove(panel);
        validate();
        panel=new JPanel();
        panel.setBackground(new Color(240, 244, 255));
        panel.setBounds(0, 0, 1100, 600);
        setSize(1100, 600);
        setResizable(false);
        panel.setLayout(null);
        
        dibujos=new Dibujo();
        dibujos.setBounds(350, 100, 550, 500);
        
        procesar=new Procesador();
        
        original=new JLabel("Original");
        original.setBounds(50, 150, 300, 200);
        JPanel x=new JPanel();
        x.setLayout(null);
        x.setBackground(new Color(240, 244, 255));
        x.setBounds(0, 100, 350, 500);
        ImageIcon icon=new ImageIcon(imagen.getPath());
        original.setIcon(new ImageIcon(icon.getImage().getScaledInstance(original.getWidth(), original.getHeight(), Image.SCALE_DEFAULT)));
        x.add(original);
        
        JPanel botones=new JPanel();
        botones.setBackground(new Color(240, 244, 255));
        botones.setLayout(new FlowLayout());
        botones.setBounds(0, 0, 1100, 100);
        pasaBaja=new JButton("Pasa Baja");
        pasaBaja.setBackground(Color.WHITE);
        pasaBaja.addActionListener(this);
        pasaAlta=new JButton("Pasa Alta");
        pasaAlta.setBackground(Color.WHITE);
        pasaAlta.addActionListener(this);
        pasaBanda=new JButton("Pasa Banda");
        pasaBanda.setBackground(Color.WHITE);
        pasaBanda.addActionListener(this);
        negativo=new JButton("Negativo");
        negativo.setBackground(Color.WHITE);
        negativo.addActionListener(this);
        quitarRuido=new JButton("Quitar Ruido");
        quitarRuido.setBackground(Color.WHITE);
        quitarRuido.addActionListener(this);
        contorno=new JButton("Contorno");
        contorno.setBackground(Color.WHITE);
        contorno.addActionListener(this);
        mediana=new JButton("Mediana");
        mediana.setBackground(Color.WHITE);
        mediana.addActionListener(this);
        sobel=new JButton("Sobel");
        sobel.setBackground(Color.WHITE);
        sobel.addActionListener(this);
        histograma=new JButton("Histograma");
        histograma.setBackground(new Color(107, 255, 117));
        histograma.addActionListener(this);
        histograma.setBounds(900, 250, 100, 50);
        botones.add(pasaBaja);
        botones.add(pasaAlta);
        botones.add(pasaBanda);
        botones.add(negativo);
        botones.add(quitarRuido);
        botones.add(contorno);
        botones.add(mediana);
        botones.add(sobel);
        
        panel.add(x);
        panel.add(dibujos);
        panel.add(botones);
        panel.add(histograma);
        add(panel);
        
        obtenerMatriz();
        dibujos.setMatriz(matriz);
        dibujos.paint(dibujos.getGraphics());
        setLocationRelativeTo(null);
        setVisible(true);
    }
    public void obtenerMatriz(){
        int r, g, b, rgb;
        try {
            BufferedImage img=ImageIO.read(imagen);
            m=img.getWidth();
            n=img.getHeight();
            matriz=new int[m][n];
            for (int i=0; i<m-1; i++) {
                for (int j=0; j<n-1; j++) {
                    rgb=img.getRGB(i,j); 
                    r=(rgb & 0x00ff0000) >> 16;
                    g=(rgb & 0x0000ff00) >> 8;
                    b=(rgb & 0x000000ff);
                    rgb=(r+g+b)/3;
                    matriz[i][j]=rgb;
                }
            }
            matriz=procesar.fractal(matriz);
        } catch (IOException ex) {
            matriz=null;
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        int menor, mayor;
        boolean dibujar=true;
        switch(e.getActionCommand()){
            case "Pasa Baja":
                menor=Integer.parseInt(JOptionPane.showInputDialog("Ingrese el umbral de pasa Baja"));
                matriz=procesar.pasaBaja(matriz, menor);
                break;
            case "Pasa Alta":
                mayor=Integer.parseInt(JOptionPane.showInputDialog("Ingrese el umbral de pasa Alta"));
                matriz=procesar.pasaAlta(matriz, mayor);
                break;
            case "Pasa Banda":
                menor=Integer.parseInt(JOptionPane.showInputDialog("Ingrese el Valor menor de pasa Banda"));
                mayor=Integer.parseInt(JOptionPane.showInputDialog("Ingrese el Valor mayor de pasa Banda"));
                matriz=procesar.pasaBanda(matriz, menor, mayor);
                break;
            case "Negativo":
                matriz=procesar.negativo(matriz);
                break;
            case "Quitar Ruido":
                matriz=procesar.quitarRuido(matriz);
                break;
            case "Contorno":
                matriz=procesar.contorno(matriz);
                break;
            case "Mediana":
                matriz=procesar.mediana(matriz);
                break;
            case "Sobel":
                //matriz=procesar.sobel(matriz);
                break;
            case "Histograma":
                new Histograma(this, false, matriz);
                dibujar=false;
                break;
            default:
                System.out.println(e.getActionCommand());
        }
        dibujos.setMatriz(matriz);
        if(dibujar)
            dibujos.paint(dibujos.getGraphics());
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new Main();
    }
}
