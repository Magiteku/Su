/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudoku;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author smoukoka
 */
public class Sudoku {
    
    private Joueur j;
    private Grille g;
    private Pile listeCoup;
    private Grille solution;

    public Sudoku(Joueur j, Grille g, Grille solution) {
        this.j = j;
        this.g = g;
        this.solution = solution;
        this.listeCoup = new Pile();
    }
    
    public void jouerUnCoup(){
        
        int taille = g.getTaille();
        int ligne = -1;
        int colonne = -1;
        System.out.println("Veuillez entrer la ligne et la colonne de votre case:");
        while (ligne < 1 || ligne > taille*taille) {
            
            System.out.print("Ligne(1 à " + taille*taille + "):");
            ligne = Clavier.Clavier.getInt();
        }
        ligne-=1;
        
        while(colonne < 1 || colonne > taille*taille) {
        
            System.out.print("Colonne(1 à " + taille*taille + "):");
            colonne = Clavier.Clavier.getInt();
        
        }
        colonne-=1;
        
        int position_case = taille*taille*ligne + colonne;
        ArrayList<Case> cases = g.getEnsembleCases();
        int valeur = -1;
        if (cases.get(position_case).estModifiable()){
            
            while (valeur < 1 || valeur > taille*taille) {
                System.out.print("Entrer la valeur de la case (1 à " + taille*taille +"):");
                valeur = Clavier.Clavier.getInt();
                if (valeur < 1 || valeur > taille*taille){
                    System.out.println("Valeur hors de la plage d'utilisation !");
                }
                else {
                    cases.get(position_case).setValeur(valeur);
                    g.setEnsembleCases(cases);
                    listeCoup.push(new Coup(position_case,taille, valeur));
                    
                }
            }
        
        }
        else {
            System.out.println("La case choisie n'est pas modifiable");
        }
       
    
    }
    
    
    public void play(){
        g.afficheGrille();
        while(!g.pleine()){
            this.jouerUnCoup();
            g.afficheGrille();
        }
        
        if (g.equals(solution)){
            System.out.println("Vous avez trouvé la bonne solution !");
        }
        else {
            System.out.println("Faux !");
        }
               
    }
    
    
    public void saveGame (){
        try{
            FileWriter fichier = new FileWriter("partie_"+ j.getNom() +".txt");
            fichier.write("#nom du joueur: "+j.getNom()+" score: "+String.valueOf(j.getScore())+"\n");
            fichier.write("#taille: "+String.valueOf(g.getTaille())+"\n");
            fichier.write("#grille du joueur: \n");
            fichier.write("%;");
            for (int i = 0; i<g.getEnsembleCases().size(); i++){
                fichier.write(g.getEnsembleCases().get(i).getValeur()+";");
            }
            fichier.write("\n"+"#solution de la partie: \n");
            for(int i = 0; i<solution.getEnsembleCases().size(); i++){
                fichier.write(solution.getEnsembleCases().get(i).getValeur()+";");
            }
            
            String position;
            String valeurCoup;
            /*if (!listeCoup.empty()){
                fichier.write("\nliste des coups joués:");
                for (int i=0; i<listeCoup.size(); i++){
                    System.out.println(listeCoup.pop());
                    
                }
            }
            System.out.println(listeCoup.peek());
            for (int i = 0; i<listeCoup.size(); i++){
               System.out.println(listeCoup.peek().toString());
                //fichier.write(position);
            }*/
            fichier.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    
    public void chargerGrille (String nomDuFichier){
        try{
            BufferedReader fichier = new BufferedReader (new FileReader(nomDuFichier+".txt"));//rajouter condition si txt déjà dans nom du fichier
            while (fichier.ready()){
                String ligne;
                ligne = fichier.readLine();
                if (ligne.startsWith("#")){
                    if (ligne.contains("taille")){
                    this.g.setTaille(Integer.parseInt(ligne.substring(9, 10)));
                    //System.out.println(this.g.getTaille());
                    }
                } else if (ligne.startsWith("%")){
                    String champs[] = ligne.split(";");
                    int tailleAuCarre = this.g.getTaille()*this.g.getTaille();
                    //System.out.println(tailleCarre*tailleCarre);
                    for (int i = 1; i<tailleAuCarre*tailleAuCarre+1; i++){
                        //System.out.print(champs[i]+" ");
                        int valeurCase = Integer.parseInt(champs[i]);
                        ArrayList<Integer> candidats = new ArrayList<Integer>();
                        if(valeurCase!=0){
                            Case nouvelleCase = new Case(g.getTaille(), valeurCase, candidats,true);
                            this.g.getEnsembleCases().set(i-1, nouvelleCase);
                        } else {
                            Case nouvelleCase = new Case(g.getTaille(), valeurCase, candidats,false);
                            this.g.getEnsembleCases().set(i-1, nouvelleCase);
                        }
                    }
                //System.out.print("\n");
                } else {
                    String champs[] = ligne.split(";");
                    int tailleAuCarre = this.g.getTaille()*this.g.getTaille();
                    for (int i = 0; i<tailleAuCarre*tailleAuCarre; i++){
                        //System.out.print(champs[i]+" ");
                        int valeurCase = Integer.parseInt(champs[i]);
                        ArrayList<Integer> candidats = new ArrayList<Integer>();
                        Case nouvelleCase = new Case(g.getTaille(), valeurCase, candidats, false);
                        this.solution.getEnsembleCases().set(i, nouvelleCase);
                    }
                } 
            }
            fichier.close();
        }catch (IOException e){
            e.printStackTrace();
        } this.g.afficheGrille();
    }
        
  
 
}
    
    

