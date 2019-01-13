package model;

import java.util.Vector;

public class Signal {
	Vector<Double> echantillons;
	double frequence;
	double nb_bits;
	String unite_phys;
	
	public Signal() {
		this.echantillons = new Vector<Double>();
	}
	
	// Methodes : Get, set, ajout et enlevement d'echantillons 
	
	public double get_echantillon(int indice) {
		return echantillons.get(indice);
	}
	
	public double get_frequence() {
		return this.frequence;
	}
	
	public double get_nb_bits() {
		return this.nb_bits;
	}
	
	public String get_unite_phys() {
		return this.unite_phys;
	}
	
	public int get_size() {
		return this.echantillons.size();
	}
	
	public void set_echantillon(int indice, double valeur) {
		this.echantillons.set(indice,valeur);
	}
	
	public void set_tab_echantillons(Vector<Double> new_echantillons) {
		this.echantillons = new_echantillons;
	}
	
	public void set_frequence(double new_frequence) {
		this.frequence = new_frequence;
	}
	
	public void set_nb_bits(double new_nb_bits) {
		this.nb_bits = new_nb_bits;
	}
	
	public void set_unite_phys(String new_unite_phys) {
		this.unite_phys = new_unite_phys;
	}
	
	public void add_echantillon(double valeur, int indice) {
		this.echantillons.add(indice,valeur);
	}
	
	public void add_echantillon_end(double valeur) {
		this.echantillons.add(new Double(valeur));
	}
	
	
	public void remove_echantillon(int indice) {
		this.echantillons.remove(indice);
	}
	
}