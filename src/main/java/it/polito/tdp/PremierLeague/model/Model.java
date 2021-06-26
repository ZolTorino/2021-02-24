package it.polito.tdp.PremierLeague.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	
	PremierLeagueDAO dao;
	private SimpleDirectedWeightedGraph<Player, DefaultWeightedEdge> grafo; 
	Map<Integer , Player> idMap;
	public Model() {
		
		dao= new PremierLeagueDAO();
		idMap=new HashMap<>();
	}
	
	public List<Match> tuttiMatches() {
		return dao.listAllMatches();
	}
	
	public String creaGrafo(Match m) {
		grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		dao.getVertices(m.getMatchID(), idMap);
		Graphs.addAllVertices(grafo, idMap.values());
		String s="";
		s+="Vertici "+grafo.vertexSet().size();
		LinkedList<Arco> archi= new LinkedList<>();
		archi= new LinkedList<>(dao.getEdges(m.getMatchID(), idMap));
		for(Arco a: archi)
		{
			if(grafo.containsVertex(a.p1)&&grafo.containsVertex(a.p2))
			{
				if(grafo.getEdge(a.p1, a.p2)==null&&grafo.getEdge(a.p2, a.p1)==null)
				{
					if(a.p1.efficiency>a.p2.efficiency)
					{
						Graphs.addEdge(grafo,a.p1, a.p2, a.p1.efficiency-a.p2.efficiency);
					}
					else
					{
						Graphs.addEdge(grafo,a.p2, a.p1, a.p2.efficiency-a.p1.efficiency);
					}
				}
			}
		}
		s+=" Archi: "+grafo.edgeSet().size();
		return s;
	}
	double migliore;
	Player migliorep;
	public String migliore() {
		 migliore=0;
		 migliorep=null;
		for(Player p: grafo.vertexSet())
		{
			double uscenti=0;
			for(DefaultWeightedEdge e:grafo.outgoingEdgesOf(p))
			{
				uscenti+= grafo.getEdgeWeight(e);
				
			}
			double entranti=0;
			for(DefaultWeightedEdge e:grafo.incomingEdgesOf(p))
			{
				entranti+= grafo.getEdgeWeight(e);
			}
			double delta=uscenti-entranti;
			
			if(delta>migliore)
			{
				migliorep=p;
				migliore=delta;
			}
			
		}
		
		return migliorep.getName()+ " "+migliore;
	}
	
	Team t1;
	Team t2;
	public String init(Match m, int n) {
		 t1= new Team(m.teamHomeID, m.teamHomeNAME);
		 t2= new Team(m.teamAwayID, m.teamAwayNAME);
		return simula(n);
	}
	public String simula(int n) {
		
		for(int i=0; i<n;i++)
		{
			int random = ThreadLocalRandom.current().nextInt(1, 101);
			System.out.println(random);
			if(random<=50)//GOAL
			{
				if(t1.getIncampo()>t2.getIncampo())
				{
					t1.setGoals(t1.getGoals()+1);
				}
				if(t2.getIncampo()>t1.getIncampo())
				{
					t2.setGoals(t2.getGoals()+1);
				}
				if(t1.getIncampo()==t2.getIncampo())
				{
					if(migliorep.team==t1.getTeamID())
					{
						t1.setGoals(t1.getGoals()+1);
					}
					else
					{
						t2.setGoals(t2.getGoals()+1);
					}
					
				}
			}
			else if(random>=51&&random<=80)//ESPULSIONE
			{
				int random1 = ThreadLocalRandom.current().nextInt(1, 101);
				if(random1<=60)
				{
					if(migliorep.team==t1.getTeamID())
					{
						t1.setIncampo(t1.getIncampo()-1);
					}
					else
					{
						t2.setIncampo(t2.getIncampo()-1);
					}
				}
				else
				{
					if(migliorep.team==t1.getTeamID())
					{
						t2.setIncampo(t2.getIncampo()-1);
					}
					else
					{
						t1.setIncampo(t1.getIncampo()-1);
					}
				}
			}
			else //INFORTUNIO
			{
				Random rand = new Random();
				boolean val = rand.nextInt(1)==0;
				if(val)
					n=n+2;
				else
					n=n+3;
			}
		}
		 String s= "Team 1 - Team2 "+t1.getGoals()+" - "+t2.getGoals()+ " Espulsi in t1: "+(11-t1.getIncampo())+ " Espulsi in t2: "+(11-t2.getIncampo());
		 return s;
	}
}
