/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Formation
 */
public class CommandeDao implements CommandeInterface {

    private DB db;
    private DBCollection collectionCmd;
    
    public CommandeDao() {
        this.db = Connexion.getConnexion();
        this.collectionCmd = this.db.getCollection("commande");
    }

    @Override
    public void ajouterCommande(Commande cmd) {
        //List<BasicDBObject> listProdCmd = new ArrayList<>();
        BasicDBObject docCmd = new BasicDBObject("_id", cmd.getIdCmd())
                .append("nomClient", cmd.getNomClient())
                .append("dateCmd", cmd.getDate())
                .append("contenir", cmd.getListeProd());
        this.collectionCmd.insert(docCmd);
    }

    @Override
    public void supprimerCommande(Commande cmd) {
        //création de l'ancienne commande avec juste l'id
        BasicDBObject docCmdOld = new BasicDBObject("_id", cmd.getIdCmd());
        //recherche de la commande
        DBObject obj = this.collectionCmd.findOne(docCmdOld);
        //récupération de la liste des produits de la commande
        BasicDBList listProd = (BasicDBList) obj.get("contenir");
        //ici nous vérifions si la commande ne contient pas de produits
        if (listProd.isEmpty()) {
            this.collectionCmd.remove(obj);
            JOptionPane.showMessageDialog(null, "Opération effectuée avec succès");
        } else {
            JOptionPane.showMessageDialog(null, "la commande n°:  "
                    + cmd.getIdCmd() + " contient des produits ");
        }
    }

    @Override
    public void ajouterProdCommande(Commande cmd, Produit prod) {
        //création de l'ancienne commande avec juste l'id
        BasicDBObject docCmdOld = new BasicDBObject("_id", cmd.getIdCmd());
        //recherche de la commande
        DBObject obj = this.collectionCmd.findOne(docCmdOld);
        //récupération de la liste des produits de la commande
        BasicDBList listProd = (BasicDBList) obj.get("contenir");

        //ajout du nouveau produit à la listProdCmd
        BasicDBObject newProd = new BasicDBObject("_id", prod.getIdProd())
                .append("nomProd", prod.getNomProd())
                .append("qteCmd", prod.getQteProd())
                .append("categorie", new BasicDBObject("_id", prod.getCatProd().getIdCat())
                        .append("libelle", prod.getCatProd().getLibelle()));
        //recherche du document produit passé en paramètre dans la liste
        boolean existe = false;
        for (int i = 0; i < listProd.size() && !existe; i++) {
            DBObject objProd = (DBObject) listProd.get(i);
            if (objProd.get("_id").equals(newProd.get("_id"))) {
                int qte = (int) objProd.get("qteCmd");
                newProd.replace("qteCmd", (qte + prod.getQteProd()));
                //si le document existe, on modifie seulement sa quantité  
                listProd.set(i, newProd);
                existe = true;
            }
        }
        //si le produit n'existe pas, on ajoute à la liste
        if (!existe) {
            listProd.add(newProd);
        }
        //création du document commande pour la mise à jour
        BasicDBObject docCmdNew = new BasicDBObject();
        docCmdNew = (BasicDBObject) obj;
        docCmdNew.replace("contenir", listProd);
        //mise à jour de la commande
        this.collectionCmd.update(docCmdOld, docCmdNew);
        //listProd.removeAll(listProd);
    }

    @Override
    public List<Commande> getAllCommande() {
        List<Commande> listeCmd = new ArrayList<>();
        //stockage de toutes les catégories dans l'objet DBCusor
        DBCursor cursor = this.collectionCmd.find();
        //parcours du cursor      
        while (cursor.hasNext()) { //hasNext permet de tester la fin du curseur
            DBObject objCmd = cursor.next(); //next permet de récupérer le document
            //dans le curseur àprès chaque itération
            Commande cmd = new Commande();
            cmd.setIdCmd((int) objCmd.get("_id"));
            cmd.setNomClient(objCmd.get("nomClient").toString());
            cmd.setDate(objCmd.get("dateCmd").toString());
            cmd.setListeProd((List) objCmd.get("contenir"));

            listeCmd.add(cmd);
        }
        return listeCmd;
    }

    @Override
    public Commande getOneCommande(int idCmd) {

        //creation d'un document avec l'id passé en paramètre
        BasicDBObject docCmd = new BasicDBObject("_id", idCmd);
        DBObject objCmd = this.collectionCmd.findOne(docCmd);

        Commande cmd = new Commande();
        cmd.setIdCmd((int) objCmd.get("_id"));
        cmd.setNomClient(objCmd.get("nomClient").toString());
        cmd.setDate(objCmd.get("dateCmd").toString());
        cmd.setListeProd((List) objCmd.get("contenir"));

        return cmd;
    }

    @Override
    public void supprimerProdCommande(Commande cmd, Produit prod) {
        //création de l'ancienne commande avec juste l'id
        BasicDBObject docCmdOld = new BasicDBObject("_id", cmd.getIdCmd());
        //recherche de la commande
        DBObject obj = this.collectionCmd.findOne(docCmdOld);
        //récupération de la liste des produits de la commande
        BasicDBList listProd = (BasicDBList) obj.get("contenir");
        
        //System.out.println("Avant : " + listProd);
        boolean supprimer = false;
        //recherche du document produit passé en paramètre dans la liste
        for (int i = 0; i < listProd.size() && !supprimer; i++) {
            DBObject objProd = (DBObject) listProd.get(i);
            System.out.println("entrée 1 avec id : " + prod.getIdProd());
            if (objProd.get("_id").equals(prod.getIdProd())) {
                System.out.println("entrée 2");
                listProd.remove(i);
                supprimer = true;
            }
        }
        //création du document commande pour la mise à jour
        BasicDBObject docCmdNew = new BasicDBObject();
        docCmdNew = (BasicDBObject) obj;
        docCmdNew.replace("contenir", listProd);
        //mise à jour de la commande
        this.collectionCmd.update(docCmdOld, docCmdNew);
    }

    @Override
    public void modifierProdCommande(Commande cmd, Produit prod) {
        //création de l'ancienne commande avec juste l'id
        BasicDBObject docCmdOld = new BasicDBObject("_id", cmd.getIdCmd());
        //recherche de la commande
        DBObject obj = this.collectionCmd.findOne(docCmdOld);
        //récupération de la liste des produits de la commande
        BasicDBList listProd = (BasicDBList) obj.get("contenir");

        //recherche du document produit passé en paramètre dans la liste
        for (int i = 0; i < listProd.size(); i++) {
            DBObject objProd = (DBObject) listProd.get(i);
            if (objProd.get("_id").equals(prod.getIdProd())) {
                // supprimer l'objet de la liste
                listProd.remove(i);
                //création de l'objet avec la nouvelle quantité
                //ajout du nouveau produit à la listProdCmd
                BasicDBObject newProd = new BasicDBObject("_id", prod.getIdProd())
                        .append("nomProd", prod.getNomProd())
                        .append("qteCmd", prod.getQteProd())
                        .append("categorie", new BasicDBObject("_id", prod.getCatProd().getIdCat())
                                .append("libelle", prod.getCatProd().getLibelle()));
                //ajout de l'objet à la liste
                listProd.add(newProd);
            }
        }
        //création du document commande pour la mise à jour
        BasicDBObject docCmdNew = new BasicDBObject();
        docCmdNew = (BasicDBObject) obj;
        docCmdNew.replace("contenir", listProd);
        //mise à jour de la commande
        this.collectionCmd.update(docCmdOld, docCmdNew);
    }

}
