/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TrackerRMI;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author berenice
 */
public class FileInformation implements Serializable{
    private static final long serialVersionUID = 42L;
    private String nombreArchivo;
    private ArrayList<String> listaIPs;
    private int numPeersQueTienenArchivo,numPartesArchivo,numConjuntos;
    
    public FileInformation(String nombreArchivo, String firstIP, int partesArchivo){
        this.nombreArchivo = nombreArchivo;
        listaIPs = new ArrayList<>();
        //listaIPs.add(firstIP);
        this.numPeersQueTienenArchivo = 1;
        this.numPartesArchivo = partesArchivo;//(int)Math.ceil( (new File(nombreArchivo).length()) / 60000);
        if (numPartesArchivo == 0){
            numPartesArchivo = 1;
        }
        modifyData(firstIP);   
    }
    
    public void modifyData(String ip){
        listaIPs.add(ip);
        this.numPeersQueTienenArchivo = listaIPs.size();
        if (numPartesArchivo<numPeersQueTienenArchivo){
            this.numConjuntos = numPartesArchivo;
        }else{
            this.numConjuntos = numPeersQueTienenArchivo;
        }
    }

    public ArrayList<String> getListaIPs() {
        return listaIPs;
    }

    public int getNumPartesArchivo() {
        return numPartesArchivo;
    }

    public int getNumConjuntos() {
        return numConjuntos;
    }
}
