/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gescommongo;

import com.mongodb.BasicDBObject;
import controleur.ControleurCategorie;
import controleur.ControleurCommande;
import controleur.ControleurProduit;
import controleur.Router;
import java.util.List;
import modele.Categorie;
import modele.CategorieDao;
import modele.Commande;
import modele.CommandeDao;
import modele.Connexion;
import modele.Produit;
import modele.ProduitDao;
import vue.CategorieVue;
import vue.PrincipaleVue;

/**
 *
 * @author Formation
 */
public class GescomMongo {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
   //     Connexion.getConnexion();
       // Categorie cat = new Categorie(2,"CD"); //création de l'objet catégorie
        //CategorieDao catDao = new CategorieDao();//création de l'objet DaoCat 
        
        //insertion du l'objet cat à la collection categorie;
  //      catDao.addCategorie(cat);
        ///catDao.deleteCategorie(cat);
        //System.out.println(catDao.getOneCategorie(2));
        
        //CategorieVue catVue = new CategorieVue();
       // catVue.setVisible(true);
       
       // ControleurCategorie controlCat = new ControleurCategorie();
      //  ProduitDao pdao = new ProduitDao();
        
       // Produit prod = new Produit(2,"Trois petits cochons","Test",
            //    15, 25, new Categorie(1,"LIVRE"));
        
        //pdao.deleteProduit(prod);
      //  System.out.println(pdao.getOneProduit(2));
      
       // ControleurProduit cprod = new ControleurProduit();
        CommandeDao cdao = new CommandeDao();
        
        Commande cmd = new Commande(6,"Francis","26/06/2019");
        
        //cdao.ajouterCommande(cmd);
        //Commande cmd2 = new Commande(4,"Lisa","20/06/2019");
        //cdao.ajouterCommande(cmd);
       Produit prod = new Produit(2,"Android pour les nuls","Test",
           10, 25, new Categorie(1,"LIVRE"));
       //cdao.ajouterProdCommande(cmd, prod);
      /*  cmd.getListeProd().add(new BasicDBObject("_id", 1)
                .append("nomProd", "Java")
                .append("qteCmd", 5)
                .append("categorie", new BasicDBObject("_id",1)
                           .append("libelle", "Livre"))
        );*/
        //cdao.ajouterProdCommande(cmd,prod);
        //cdao.supprimerProdCommande(cmd, prod);
        //cdao.supprimerCommande(cmd);
        //cdao.modifierProdCommande(cmd, prod);
    //    System.out.println(cdao.getOneCommande(6));
        
       /* List<Commande> listCmd = cdao.getAllCommande();
        
        for(Commande cmd1 : listCmd){
            System.out.println(cmd1);
        }*/
       // ControleurCommande controleurCmd = new ControleurCommande();
        Router router = new Router();
      }
    
}
