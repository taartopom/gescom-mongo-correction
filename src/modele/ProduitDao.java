/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Formation
 */
public class ProduitDao implements ProduitInterface{
   
    private DB db;
    private DBCollection collectionProd;

    public ProduitDao() {
        this.db = Connexion.getConnexion();
        this.collectionProd = this.db.getCollection("produit");
    }
    
    @Override
    public List<Produit> getAllProduit() {
        
      List<Produit> listeProd = new ArrayList<>(); 
        DBCursor cursor = this.collectionProd.find();    
        while(cursor.hasNext()){
            DBObject obj = cursor.next();
            Produit prod = new Produit();
            prod.setIdProd((int)obj.get("_id"));
            prod.setDescriptionProd(obj.get("description").toString());
            prod.setNomProd(obj.get("nomProd").toString());
            prod.setPrixProd((double)obj.get("prix"));
            prod.setQteProd((int)obj.get("qte"));
            
            /*******Création du document pour récupérer la cat dans le document Prod *****/
            DBObject objCat  = (DBObject)obj.get("idCat");
           
            ///****mise à jour de la cat dans l'objet produit
            prod.setCatProd(new Categorie((int)objCat.get("_id"),
                    objCat.get("libelle").toString()));
            //ajout du produit à la liste
            listeProd.add(prod);       
        }
        return listeProd;
    }

    @Override
    public void addProduit(Produit prod) {
        //création du document produit
        BasicDBObject docProd = new BasicDBObject();
        docProd.append("_id", prod.getIdProd())
                .append("nomProd", prod.getNomProd())
                .append("description", prod.getDescriptionProd())
                .append("prix",prod.getPrixProd())
                .append("qte", prod.getQteProd())
                .append("idCat",new BasicDBObject(
                        "_id", prod.getCatProd().getIdCat())
                        .append("libelle",prod.getCatProd().getLibelle()));
        
           this.collectionProd.insert(docProd);
    }

    @Override
    public void deleteProduit(Produit prod) {
        BasicDBObject docProd = new BasicDBObject();
        docProd.append("_id", prod.getIdProd());    
         this.collectionProd.remove(docProd);        
    }

    @Override
    public void updateProduit(Produit prod) {
        
        BasicDBObject docProdNew = new BasicDBObject();
        docProdNew.append("_id", prod.getIdProd())
                .append("nomProd", prod.getNomProd())
                .append("description", prod.getDescriptionProd())
                .append("prix", prod.getPrixProd())
                .append("qte", prod.getQteProd())
                .append("idCat", new BasicDBObject(
                        "_id", prod.getCatProd().getIdCat())
                        .append("libelle", prod.getCatProd().getLibelle()));
        
        BasicDBObject docProdOld = new BasicDBObject();
        docProdOld.append("_id", prod.getIdProd());
        
        this.collectionProd.update(docProdOld, docProdNew); 
    }

    @Override
    public Produit getOneProduit(int idProd) {
        BasicDBObject id = new BasicDBObject("_id", idProd);
        
        DBObject obj = this.collectionProd.findOne(id);

        Produit prod = new Produit();
        prod.setIdProd((int) obj.get("_id"));
        prod.setDescriptionProd(obj.get("description").toString());
        prod.setNomProd(obj.get("nomProd").toString());
        prod.setPrixProd((double) obj.get("prix"));
        prod.setQteProd((int) obj.get("qte"));
        /**
         * *****Création du document pour récupérer la cat dans le document Prod ****
         */
        DBObject objCat = (DBObject) obj.get("idCat");
        ///****mise à jour de la cat dans l'objet produit
        prod.setCatProd(new Categorie((int) objCat.get("_id"),
                objCat.get("libelle").toString()));
        //ajout du produit à la liste
        //listeProd.add(prod);       
        return prod;
    }
    
}
