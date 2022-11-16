package br.ufes.edu.jh.util.io;

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
        for(Candidate c: election.electedCandidates()){
            System.out.printf("%d - ",c.getPosition());
            if(c.getParty().getFederation() != -1)
                System.out.print("*");
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
        
        //===============================================================//
        System.out.println("Primeiro e último colocados de cada partido:");
        for(PoliticalParty p: election.getPartiesOrderedByCandidates()){
            System.out.printf("%d - %s - %d, %s (%d, %d votos) / %s (%d, %d votos)\n",
            p.getPosition(), p.getSg(), p.getNumber(), p.mostVotedCandidate().getNmUrnaCandidato(), 
            p.mostVotedCandidate().getNrCandidato(), p.mostVotedCandidate().getQtVotos(),
            p.leastVotedCandidate().getNmUrnaCandidato(), p.leastVotedCandidate().getNrCandidato(),
            p.leastVotedCandidate().getQtVotos());
        }
        System.out.print("\n");

        //===============================================================//
        System.out.println("Eleitos, por faixa etária (na data da eleição):");
        int totalElected = election.electedAmount();
        
        int f1 = election.electedAmountByAge(0, 30);
        int f2 = election.electedAmountByAge(30, 40);
        int f3 = election.electedAmountByAge(40, 50);
        int f4 = election.electedAmountByAge(50, 60);
        int f5 = election.electedAmountByAge(60, 120);

        float p1 = ((float)f1 / (float)totalElected)*100;
        float p2 = ((float)f2 / (float)totalElected)*100;
        float p3 = ((float)f3 / (float)totalElected)*100;
        float p4 = ((float)f4 / (float)totalElected)*100;
        float p5 = ((float)f5 / (float)totalElected)*100;

        System.out.printf("      Idade < 30: %d (%.2f%%)\n", f1, p1);
        System.out.printf("30 <= Idade < 40: %d (%.2f%%)\n", f1, p2);
        System.out.printf("40 <= Idade < 50: %d (%.2f%%)\n", f3, p3);
        System.out.printf("50 <= Idade < 60: %d (%.2f%%)\n", f4, p4);
        System.out.printf("60 <= Idade     : %d (%.2f%%)\n\n", f5, p5);

        //===============================================================//
        System.out.println("Eleitos, por gênero:");
        int men = election.electedMen();
        int women = election.electedWomen();

        float pmen = ((float)men/(float)totalElected)*100;
        float pwomen = ((float)women/(float)totalElected)*100;

        System.out.printf("Feminino: %d (%.2f%%)\n", women, pwomen);
        System.out.printf("Masculino: %d (%.2f%%)\n", men, pmen);

        //===============================================================//
        int validVotes = election.getLegendVotes() + election.getNominalVotes();
        int nominal = election.getNominalVotes();
        int legend = election.getLegendVotes();

        float pNominal = ((float)nominal/(float)validVotes) * 100;
        float pLegend = ((float)legend/(float)validVotes) * 100;

        System.out.printf("Total de votos válidos:    %d\n", validVotes);
        System.out.printf("Total de votos nominais:   %d (%.2f%%)\n", nominal, pNominal);
        System.out.printf("Total de votos de legenda: %d (%.2f%%)\n", legend, pLegend);
    }
}
