/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import vue.PrincipaleVue;

/**
 *
 * @author Formation
 */
public class Router implements ActionListener{
    private PrincipaleVue principale ;

    public Router() {
        this.principale = new PrincipaleVue();
         ajouterListener();
        this.principale.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(this.principale.getMenuCat())){
            ControleurCategorie controlCat = new ControleurCategorie();
        }
        if(e.getSource().equals(this.principale.getMenuCommandes2())){
            ControleurCommande controlCmd = new ControleurCommande();
        }
        if(e.getSource().equals( this.principale.getMenuProd())){
            ControleurProduit controlProd = new ControleurProduit();
        }
        
    }
    public void ajouterListener(){
        this.principale.getMenuCat().addActionListener(this);
        //this.principale.getMenuCommande().addActionListener(this);
        this.principale.getMenuProd().addActionListener(this);
        this.principale.getMenuCommandes2().addActionListener(this);
    }
}
