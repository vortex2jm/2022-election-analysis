package br.ufes.edu.jh.util.io;

import java.text.NumberFormat;
import java.util.Locale;

import br.ufes.edu.jh.domain.Candidate;
import br.ufes.edu.jh.domain.Election;
import br.ufes.edu.jh.domain.PoliticalParty;

public class OutputServices {
    
    public static void generateReports(Election election){

        //Instância do Number Format para modificar as saídas (Será necessário modificar o printf para println)
        Locale localeBr = Locale.forLanguageTag("pt-BR");
        NumberFormat nf = NumberFormat.getInstance(localeBr);
        NumberFormat nfDec = NumberFormat.getInstance(localeBr);
        nfDec.setMinimumFractionDigits(2);
        nfDec.setMaximumFractionDigits(2);

        System.out.println("Número de vagas: " + nf.format(election.electedAmount()));
        System.out.println();
        
        //===============================================================//
        String category="";
        if(election.getType() == 6)
            category = "federais";
        else if(election.getType() == 7)
            category = "estaduais";
            
        System.out.printf("Deputados %s eleitos:\n", category);
        for(Candidate c: election.electedCandidates()){
            System.out.printf("%s - ", nf.format(c.getPosition()));
            if(c.getParty().getFederation() != -1)
                System.out.print("*");
            System.out.println(c);
        }
        System.out.print("\n");
        
        //===============================================================//
        System.out.println("Candidatos mais votados (em ordem decrescente de votação e respeitando número de vagas):");
        for(Candidate c: election.getBestCandidates()){
            System.out.printf("%s - ", nf.format(c.getPosition()));
            System.out.println(c);
        }
        System.out.print("\n");

        //===============================================================//
        System.out.println("Teriam sido eleitos se a votação fosse majoritária, e não foram eleitos:");
        System.out.println("(com sua posição no ranking de mais votados)");
        for(Candidate c: election.electedIfMajorElection()){
            System.out.printf("%s - ", nf.format(c.getPosition()));
            System.out.println(c);
        }
        System.out.print("\n");

        //===============================================================//
        System.out.println("Eleitos, que se beneficiaram do sistema proporcional:");
        System.out.println("(com sua posição no ranking de mais votados)");
        for(Candidate c: election.electedByProportional()){
            System.out.printf("%s - ", nf.format(c.getPosition()));
            System.out.println(c);
        }
        System.out.print("\n");

        //===============================================================//
        System.out.println("Votação dos partidos e número de candidatos eleitos:");
        for(PoliticalParty p: election.getParties()){
            System.out.printf("%s - %s - %s, %s votos (%s nominais e %s de legenda), %s candidatos eleitos\n",
            nf.format(p.getPosition()), p.getSg(), nf.format(p.getNumber()), nf.format(p.getTotalVotes()),
            nf.format(p.getNominalVotes()), nf.format(p.getLegendVotes()), nf.format(p.getElectedAmount()));
        }
        System.out.print("\n");
        
        //===============================================================//
        System.out.println("Primeiro e último colocados de cada partido:");
        for(PoliticalParty p: election.getPartiesOrderedByCandidates()){
            System.out.printf("%s - %s - %s, %s (%d, %s votos) / %s (%d, %s votos)\n",
            nf.format(p.getPosition()), p.getSg(), nf.format(p.getNumber()), p.mostVotedCandidate().getNmUrnaCandidato(), 
            p.mostVotedCandidate().getNrCandidato(), nf.format(p.mostVotedCandidate().getQtVotos()),
            p.leastVotedCandidate().getNmUrnaCandidato(), p.leastVotedCandidate().getNrCandidato(),
            nf.format(p.leastVotedCandidate().getQtVotos()));
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

        System.out.printf("      Idade < 30: %s (%s%%)\n", nf.format(f1), nfDec.format(p1));
        System.out.printf("30 <= Idade < 40: %s (%s%%)\n", nf.format(f1), nfDec.format(p2));
        System.out.printf("40 <= Idade < 50: %s (%s%%)\n", nf.format(f3), nfDec.format(p3));
        System.out.printf("50 <= Idade < 60: %s (%s%%)\n", nf.format(f4), nfDec.format(p4));
        System.out.printf("60 <= Idade     : %s (%s%%)\n\n", nf.format(f5), nfDec.format(p5));

        //===============================================================//
        System.out.println("Eleitos, por gênero:");
        int men = election.electedMen();
        int women = election.electedWomen();

        float pmen = ((float)men/(float)totalElected)*100;
        float pwomen = ((float)women/(float)totalElected)*100;

        System.out.printf("Feminino: %s (%s%%)\n", nf.format(women), nfDec.format(pwomen));
        System.out.printf("Masculino: %s (%s%%)\n\n", nf.format(men), nfDec.format(pmen));

        //===============================================================//
        int validVotes = election.getLegendVotes() + election.getNominalVotes();
        int nominal = election.getNominalVotes();
        int legend = election.getLegendVotes();

        float pNominal = ((float)nominal/(float)validVotes) * 100;
        float pLegend = ((float)legend/(float)validVotes) * 100;

        System.out.printf("Total de votos válidos:    %s\n", nf.format(validVotes));
        System.out.printf("Total de votos nominais:   %s (%s%%)\n", nf.format(nominal), nfDec.format(pNominal));
        System.out.printf("Total de votos de legenda: %s (%s%%)\n", nf.format(legend), nfDec.format(pLegend));
    }
}
