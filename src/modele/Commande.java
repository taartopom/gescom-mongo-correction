/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

import com.mongodb.BasicDBObject;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Formation
 */
public class Commande {
    private int idCmd;
    private String nomClient;
    private String date;
    private List<BasicDBObject> listeProd;

    public Commande() {
         listeProd = new ArrayList<>();
    }

    public Commande(int idCmd) {
        this.idCmd = idCmd;
    }

    public Commande(int idCmd, String nomClient, String date) {
        this.idCmd = idCmd;
        this.nomClient = nomClient;
        this.date = date;
        listeProd = new ArrayList<>();
    }

    public Commande(int idCmd, String nomClient) {
        this.idCmd = idCmd;
        this.nomClient = nomClient;
         listeProd = new ArrayList<>();
    }

    public List<BasicDBObject> getListeProd() {
        return listeProd;
    }

    public void setListeProd(List<BasicDBObject> listeProd) {
        this.listeProd = listeProd;
    }

    public int getIdCmd() {
        return idCmd;
    }

    public void setIdCmd(int idCmd) {
        this.idCmd = idCmd;
    }

    public String getNomClient() {
        return nomClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    @Override
    public String toString() {
        return "Commande{" + "idCmd=" + idCmd + ", nomClient=" + nomClient + ", date=" + date + ", listeProd=" + listeProd + '}';
    }
    
    
}
