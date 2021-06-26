package it.polito.tdp.PremierLeague.model;

public class Arco {
	Player p1, p2;
	Double peso;
	public Arco(Player p1, Player p2) {
		super();
		this.p1 = p1;
		this.p2 = p2;
		
	}
	public Player getP1() {
		return p1;
	}
	public void setP1(Player p1) {
		this.p1 = p1;
	}
	public Player getP2() {
		return p2;
	}
	public void setP2(Player p2) {
		this.p2 = p2;
	}
	public Double getPeso() {
		return peso;
	}
	public void setPeso(Double peso) {
		this.peso = peso;
	}
}
