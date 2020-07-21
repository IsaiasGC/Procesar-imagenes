/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package recognizer;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.ws.soap.MTOM;

/**
 *
 * @author Isaias
 */
public class Procesador {
    
    public int[][] pasaBaja(int[][] matriz, int umbral){
        for (int i=0; i<matriz.length; i++) {
            for (int j=0; j<matriz[0].length; j++) {
                if(matriz[i][j]>umbral) {
                    matriz[i][j]=255;
                }
            }
        }
        return matriz;
    }
    public int[][] pasaAlta(int[][] matriz, int umbral){
        for (int i=0; i<matriz.length; i++) {
            for (int j=0; j<matriz[0].length; j++) {
                if(matriz[i][j]<umbral) {
                    matriz[i][j]=0;
                }
            }
        }
        return matriz;
    }
    public int[][] pasaBanda(int[][] matriz, int menor, int mayor){
        for (int i=0; i<matriz.length; i++) {
            for (int j=0; j<matriz[0].length; j++) {
                if(matriz[i][j]<menor || matriz[i][j]>mayor) {
                    matriz[i][j]=255;
                }
            }
        }
        return matriz;
    }
    public int[][] negativo(int[][] matriz){
        for (int i=0; i<matriz.length; i++) {
            for (int j=0; j<matriz[0].length; j++) {
                matriz[i][j]=255-matriz[i][j];
            }
        }
        return matriz;
    }
    public int[][] quitarRuido(int[][] matriz){
        int[][] aux=new int[matriz.length][matriz[0].length];
        int prom;
        for (int i=0; i<matriz.length-2; i++) {
            for (int j=0; j<matriz[0].length-2; j++) {
                prom=0;
                for (int k=i; k<i+2; k++) {
                    for (int l=j; l<j+2; l++) {
                        prom+=matriz[k][l];
                    }
                }
                prom/=9;
                aux[i+1][j+1]=prom;
            }
        }
        return aux;
    }
    public int[][] contorno(int[][] matriz){
        int[][] aux=new int[matriz.length][matriz[0].length];
        int e;
        for (int i=0; i<matriz.length-2; i++) {
            for (int j=0; j<matriz[0].length-2; j++) {
                e=Math.abs(matriz[i][j]-matriz[i+2][j+2]);
                e+=Math.abs(matriz[i+1][j]-matriz[i+1][j+2]);
                e+=Math.abs(matriz[i+2][j]-matriz[i][j+2]);
                e+=Math.abs(matriz[i][j+1]-matriz[i+2][j+1]);
                e/=4;
                aux[i+1][j+1]=e;
            }
        }
        return aux;
    }
    public int[][] mediana(int[][] matriz){
        int[][] aux=new int[matriz.length][matriz[0].length];
        int[] vec=new int[9];
        int cont;
        for (int i=0; i<matriz.length-2; i++) {
            for (int j=0; j<matriz[0].length-2; j++) {
                cont=0;
                for (int k=0; k<3; k++) {
                    for (int l=0; l<3; l++) {
                        vec[cont]=matriz[i+k][j+l];
                        cont++;
                    }
                }
                vec=ordenar(vec);
                aux[i+1][j+1]=vec[4];
            }
        }
        return aux;
    }
    public int[] ordenar(int[] vec){
        int aux;
        for(int i=0; i<vec.length-1; i++){
            for(int j=i+1; j<vec.length; j++){
                if(vec[i]>vec[j]){
                    aux=vec[i];
                    vec[i]=vec[j];
                    vec[j]=aux;
                }
            }
        }
        return vec;
    }
    public int[][] sobel(int[][] matriz){
        int[][] aux=new int[matriz.length][matriz[0].length];
        for (int i=0; i<matriz.length-2; i++) {
            for (int j=0; j<matriz[0].length-2; j++) {
                aux[i][j+1]=matriz[i][j]-matriz[i][j+2];
                aux[i+2][j+1]=matriz[i+2][j]-matriz[i+2][j+2];
                aux[i][j+1]=matriz[i][j]-matriz[i][j+2];
            }
        }
        return aux;
    }
    public int[] histograma(int[][] matriz){
        int[] histo=new int[256];
        for (int i=0; i<matriz.length; i++) {
            for (int j=0; j<matriz[0].length; j++) {
                histo[matriz[i][j]]++;
            }
        }
        return histo;
    }
    public int[][] fractal(int[][] matriz){
        double escala=1;
        int x, y=0;
        int m=matriz.length;
        int n=matriz[0].length;
        while(m>410 || n>510){
            m=(int)(matriz.length*escala);
            n=(int)(matriz[0].length*escala);
            escala-=.001;
        }
        System.out.println(m+"X"+n);
        int[][] nueva=new int[m][n];
        for (x=0; x<matriz.length; x++) {
            for (y=0; y<matriz[0].length; y++) {
                if(x*escala<=m && y*escala<=n)
                    nueva[(int)(x*escala)][(int)(y*escala)]=matriz[x][y];
            }
        }
        System.out.println(x+"X"+y);
        return nueva;
    }
}
