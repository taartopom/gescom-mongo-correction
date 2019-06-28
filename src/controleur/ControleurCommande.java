/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controleur;

import com.mongodb.BasicDBObject;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modele.Commande;
import modele.CommandeDao;
import modele.Produit;
import modele.ProduitDao;
import vue.CommandeVue;
import vue.LigneCommande;

/**
 *
 * @author Formation
 */
public class ControleurCommande implements ActionListener,MouseListener{

    private CommandeVue cmdVue;
    private CommandeDao cmdDao;
    private ProduitDao pdao;
    private LigneCommande ligneCmd;
    private DefaultTableModel modelCmd,modelLigneCmd;
    private int idProd;
    
    public ControleurCommande() {
        this.cmdVue = new CommandeVue();
        this.cmdDao = new CommandeDao();
        this.ligneCmd = new LigneCommande();
        this.pdao = new ProduitDao();

        this.cmdVue.getTxtDateCmd().setText(getDateJour());

        nextId();
        initModelCmd();
        addListerner();
        initBtnD();
        getAllProd();
        
        this.cmdVue.setVisible(true);

    }

    /**
     * cette méthode retour la date du jour formatée
     *
     * @return
     */
    public String getDateJour() {
        Date aujourdhui = new Date();

        SimpleDateFormat formater = new SimpleDateFormat("dd/MM/Y");

        return formater.format(aujourdhui);
    }
    public void getAllProd(){
        List<Produit> listeProd = this.pdao.getAllProduit();
        for(Produit prod : listeProd){
            this.ligneCmd.getComboProd().addItem(
                    Integer.toString(prod.getIdProd())+" "+
                            prod.getNomProd());
        }
   }

    /**
     * cette méthode retourne l'id max de la collection Catégorie
     *
     * @return
     */
    public int maxId() {
        List<Commande> listeCmd = this.cmdDao.getAllCommande();
        List<Integer> listeId = new ArrayList<>();

        for (Commande cmd : listeCmd) {
            listeId.add(cmd.getIdCmd());
        }
        if (listeId.isEmpty()) {
            listeId.add(0);
        }
        return Collections.max(listeId);
    }
    /**
     * cette méthode met à jour le champs idCat avec l'id max +1 de la base
     */
    public void nextId() {
        this.cmdVue.getTxtIdCmd().setText(Integer.toString(maxId() + 1));
    }
      /**
     * 
     */
    public void initModelCmd(){
       modelCmd = new DefaultTableModel();   
          //création du modele catégorie
        //Ajout des Colonnes du dodele Catégorie
        modelCmd.addColumn("ID Commande");
        modelCmd.addColumn("Nom Client");
        modelCmd.addColumn("Date Commande");
        modelCmd.addColumn("Nombre de produit");
        //inserer les lignes dans le medele cat
        List<Commande> allCmd = new ArrayList<>();
        allCmd = this.cmdDao.getAllCommande();
        
        for (Commande cmd : allCmd) {
            modelCmd.addRow(new Object[]{cmd.getIdCmd(),
                cmd.getNomClient(),cmd.getDate(),cmd.getListeProd().size()});
        }
        this.cmdVue.getjTable1().setModel(modelCmd);     
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //gestion de la suppression d'une commande
        if(e.getSource().equals(this.cmdVue.getBtnSupprimerCmd())){
            this.cmdDao.supprimerCommande(new Commande(Integer.parseInt(
                    this.cmdVue.getTxtIdCmd().getText()),null));
            
            initModelCmd();
        }
        //création de la commande
        if(e.getSource().equals(this.cmdVue.getBtnAjouterCmd())){
            Commande cmd = new Commande();
            cmd.setIdCmd(Integer.parseInt(this.cmdVue.getTxtIdCmd().getText()));
            cmd.setNomClient(this.cmdVue.getTxtNomClient().getText());
            cmd.setDate(this.cmdVue.getTxtDateCmd().getText());
            
            this.cmdDao.ajouterCommande(cmd);
            
            nextId();
            initModelCmd();
        }
        if(e.getSource().equals(this.cmdVue.getBtnResetCmd())){
            initBtnD();
            this.cmdVue.getTxtDateCmd().setText(getDateJour());
            this.cmdVue.getTxtNomClient().setText("");
            nextId();
        }
        if(e.getSource().equals(this.cmdVue.getBtnAjouterProdCmd())){
            this.ligneCmd.setVisible(true);
        }
        if(e.getSource().equals(this.ligneCmd.getComboProd())){
            String prodId = this.ligneCmd.getComboProd().getSelectedItem().toString();
            Produit prod = this.pdao.getOneProduit(findIdProd(prodId));
            this.ligneCmd.getTxtPU().setText(Double.toString(prod.getPrixProd()));
            this.ligneCmd.getTxtCatProd().setText(Integer.toString(prod.getCatProd().getIdCat()) +" "
            + prod.getCatProd().getLibelle());
        }
        if(e.getSource().equals(this.ligneCmd.getBtnAjouterProdCmd())){                   
          if((!"".equals(this.ligneCmd.getQteProdCmd().getText()))){ 
            //création de l'objet commande
            Commande cmd = new Commande(Integer.parseInt(this.ligneCmd.getTxtIdLigneCmd().getText()));
           //création du produit 
            String prodId = this.ligneCmd.getComboProd().getSelectedItem().toString();
            Produit prod = this.pdao.getOneProduit(findIdProd(prodId));
            prod.setQteProd(Integer.parseInt(this.ligneCmd.getQteProdCmd().getText()));
            //Ajout du produit à la commande
            this.cmdDao.ajouterProdCommande(cmd, prod);          
            //appel de la méthode permettant de rafraîchir le medele 
            initModelLigneCmd(cmd.getIdCmd());
            initModelCmd();
             this.ligneCmd.getTxtPrixTotal().setText(Double.toString(prixTotalCmd(cmd.getIdCmd())));
          }else{
              JOptionPane.showMessageDialog(null,"Renseignez la quantité SVP");
          }
        }
        if(e.getSource().equals(this.ligneCmd.getBtnSupprimerProdCmd())){         
         if(this.idProd!=0){   
            Commande cmd = new Commande(Integer.parseInt(this.ligneCmd.getTxtIdLigneCmd().getText()));
           //création du produit 
           // String prodId = this.ligneCmd.getComboProd().getSelectedItem().toString();
            Produit prod = this.pdao.getOneProduit(this.idProd);           
            this.cmdDao.supprimerProdCommande(cmd, prod);
            //appel de la méthode permettant de rafraîchir le medele 
            initModelLigneCmd(cmd.getIdCmd());
            initModelCmd();
            this.ligneCmd.getTxtPrixTotal().setText(Double.toString(prixTotalCmd(cmd.getIdCmd())));
         }else{
             JOptionPane.showMessageDialog(null,"Selectionnez le produit SVP");
         }
        }
        if(e.getSource().equals(this.ligneCmd.getBtnModifierProdCmd())){                   
          if((!"".equals(this.ligneCmd.getQteProdCmd().getText()))){ 
            //création de l'objet commande
            Commande cmd = new Commande(Integer.parseInt(this.ligneCmd.getTxtIdLigneCmd().getText()));
           //création du produit 
            //String prodId = this.ligneCmd.getComboProd().getSelectedItem().toString();
            Produit prod = this.pdao.getOneProduit(this.idProd);
            
            prod.setQteProd(Integer.parseInt(this.ligneCmd.getQteProdCmd().getText()));
            //Ajout du produit à la commande
            this.cmdDao.modifierProdCommande(cmd, prod);          
            //appel de la méthode permettant de rafraîchir le medele 
            initModelLigneCmd(cmd.getIdCmd());
            initModelCmd();
            this.ligneCmd.getTxtPrixTotal().setText(Double.toString(prixTotalCmd(cmd.getIdCmd())));
          }else{
              JOptionPane.showMessageDialog(null,"Renseignez la quantité SVP");
          }
        }
        if(e.getSource().equals(this.ligneCmd.getBtnResetProdCmd())){
            initBtnD();
        }
    }
        /**
     * Cette méthode récupère l'idCat dans la chaine de caractère formée de 
     * idcat et du libelle
     * @param chaine
     * @return 
     */
    public int findIdProd(String chaine){
        String [] tabIdCat = chaine.split(" ");  
        return Integer.parseInt(tabIdCat[0]);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getSource().equals(this.cmdVue.getjTable1())){    
         int ligne = this.cmdVue.getjTable1().getSelectedRow(); 
         //id de la commande
         int idCmd =Integer.parseInt(modelCmd.getValueAt(ligne,0).toString());
         
         this.cmdVue.getTxtIdCmd().setText(modelCmd.getValueAt(ligne,0).toString());
         this.cmdVue.getTxtNomClient().setText(modelCmd.getValueAt(ligne,1).toString());
         this.cmdVue.getTxtDateCmd().setText(modelCmd.getValueAt(ligne,2).toString());
         
         initBtnM();
         
         this.ligneCmd.getTxtIdLigneCmd().setText(modelCmd.getValueAt(ligne,0).toString());
         this.ligneCmd.getTxtNomClientLigneCmd().setText(modelCmd.getValueAt(ligne,1).toString());
         this.ligneCmd.getTxtDateLigneCmd().setText(modelCmd.getValueAt(ligne,2).toString());
         
         this.ligneCmd.getTxtPrixTotal().setText(Double.toString(prixTotalCmd(idCmd)));
         
         initModelLigneCmd(idCmd);
        }
        if(e.getSource().equals(this.ligneCmd.getjTable1())){
           int ligne = this.ligneCmd.getjTable1().getSelectedRow(); 
            //id de la commande
            this.idProd =Integer.parseInt(modelLigneCmd.getValueAt(ligne,0).toString());          
            initBtnMLigneProduit();            
        }
    }
    public void initModelLigneCmd(int idCmd){      
        /*****************modele ligne Commande************/
        modelLigneCmd = new DefaultTableModel();   
          //création du modele catégorie
        //Ajout des Colonnes du dodele Catégorie
        modelLigneCmd.addColumn("ID Prod");
        modelLigneCmd.addColumn("Nom Prod");
        modelLigneCmd.addColumn("QTE Commandée");
        modelLigneCmd.addColumn("Catégorie");
        //inserer les lignes dans le medele cat
        List<Commande> allCmd = new ArrayList<>();
        Commande cmd = this.cmdDao.getOneCommande(idCmd);
        
        for (BasicDBObject obj : cmd.getListeProd()) {          
            modelLigneCmd.addRow(new Object[]{obj.get("_id"),
                obj.get("nomProd"),obj.get("qteCmd")});
        }
        this.ligneCmd.getjTable1().setModel(modelLigneCmd);   
    }
    public double prixTotalCmd(int idCmd) {
        double prixt = 0;
        List<Commande> allCmd = new ArrayList<>();
        Commande cmd = this.cmdDao.getOneCommande(idCmd);
        for (BasicDBObject obj : cmd.getListeProd()) {
            int qte = (int)obj.get("qteCmd");
            Produit prod = this.pdao.getOneProduit((int)obj.get("_id"));
            prixt = prixt + (qte*prod.getPrixProd());
            //System.out.println(prixt);
        }       
        return prixt;
    }

    @Override
    public void mousePressed(MouseEvent e) {
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseReleased(MouseEvent e) {
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseEntered(MouseEvent e) {
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseExited(MouseEvent e) {
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
   public void addListerner(){
       this.cmdVue.getjTable1().addMouseListener(this);
       this.cmdVue.getBtnSupprimerCmd().addActionListener(this);
       this.cmdVue.getBtnAjouterCmd().addActionListener(this);
       this.cmdVue.getBtnResetCmd().addActionListener(this);
       this.cmdVue.getBtnAjouterProdCmd().addActionListener(this);
       
       this.ligneCmd.getComboProd().addActionListener(this);
       this.ligneCmd.getBtnAjouterProdCmd().addActionListener(this);
       this.ligneCmd.getBtnSupprimerProdCmd().addActionListener(this);
       this.ligneCmd.getBtnModifierProdCmd().addActionListener(this);
       this.ligneCmd.getBtnResetProdCmd().addActionListener(this);
       
       this.ligneCmd.getjTable1().addMouseListener(this);
    }
   public void initBtnD(){
       this.cmdVue.getBtnAjouterCmd().setEnabled(true);
       this.cmdVue.getBtnAjouterProdCmd().setEnabled(false);
       this.cmdVue.getBtnSupprimerCmd().setEnabled(false);
       
       this.ligneCmd.getBtnAjouterProdCmd().setEnabled(true);
       this.ligneCmd.getBtnModifierProdCmd().setEnabled(false);
       this.ligneCmd.getBtnSupprimerProdCmd().setEnabled(false);
   }
  public void initBtnM(){
       this.cmdVue.getBtnAjouterCmd().setEnabled(false);
       this.cmdVue.getBtnAjouterProdCmd().setEnabled(true);
       this.cmdVue.getBtnSupprimerCmd().setEnabled(true);
   }
  public void initBtnMLigneProduit(){
       this.ligneCmd.getBtnAjouterProdCmd().setEnabled(false);
       this.ligneCmd.getBtnModifierProdCmd().setEnabled(true);
       this.ligneCmd.getBtnSupprimerProdCmd().setEnabled(true);
  }
   
}
