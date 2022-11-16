package br.ufes.edu.jh.util;

import br.ufes.edu.jh.domain.Candidate;
import br.ufes.edu.jh.domain.Election;
import br.ufes.edu.jh.domain.PoliticalParty;

public class OutputServices {
    
    public static void generateReports(Election election){

        System.out.println("Número de vagas: "+election.electedAmount());
        System.out.print("\n");
        
        //===============================================================//
        String category="";
        if(election.getType() == 6)
            category = "federais";
        else if(election.getType() == 7)
            category = "estaduais";
        System.out.printf("Deputados %s eleitos:\n", category);
        
        //===============================================================//
        for(Candidate c: election.electedCandidates()){
            System.out.printf("%d - ",c.getPosition());
            System.out.println(c);
        }
        System.out.print("\n");
        
        //===============================================================//
        System.out.println("Candidatos mais votados (em ordem decrescente de votação e respeitando número de vagas):");
        for(Candidate c: election.getBestCandidates()){
            System.out.printf("%d - ", c.getPosition());
            System.out.println(c);
        }
        System.out.print("\n");

        //===============================================================//
        System.out.println("Teriam sido eleitos se a votação fosse majoritária, e não foram eleitos:");
        System.out.println("(com sua posição no ranking de mais votados)");
        for(Candidate c: election.electedIfMajorElection()){
            System.out.printf("%d - ", c.getPosition());
            System.out.println(c);
        }
        System.out.print("\n");

        //===============================================================//
        System.out.println("Eleitos, que se beneficiaram do sistema proporcional:");
        System.out.println("(com sua posição no ranking de mais votados)");
        for(Candidate c: election.electedByProportional()){
            System.out.printf("%d - ", c.getPosition());
            System.out.println(c);
        }
        System.out.print("\n");

        //===============================================================//
        System.out.println("Votação dos partidos e número de candidatos eleitos:");
        for(PoliticalParty p: election.getParties()){
            System.out.printf("%d - %s - %d, %d votos (%d nominais e %d de legenda), %d candidatos eleitos\n",
            p.getPosition(), p.getSg(), p.getNumber(), p.getTotalVotes(),
             p.getNominalVotes(), p.getLegendVotes(), p.getElectedAmount());
        }
        System.out.print("\n");
        
        // Adicionar função de faixa etária por data via argumentos
    }
}
