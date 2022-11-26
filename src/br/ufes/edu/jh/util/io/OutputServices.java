package br.ufes.edu.jh.util.io;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.text.NumberFormat;
import java.util.Locale;

import br.ufes.edu.jh.domain.Candidate;
import br.ufes.edu.jh.domain.Election;
import br.ufes.edu.jh.domain.PoliticalParty;

public class OutputServices {
    
    public static void generateReports(Election election, PrintWriter printer) throws IOException{

        Locale localeBr = Locale.forLanguageTag("pt-BR");
        NumberFormat nf = NumberFormat.getInstance(localeBr);
        NumberFormat nfDec = NumberFormat.getInstance(localeBr);
        nfDec.setMinimumFractionDigits(2);
        nfDec.setMaximumFractionDigits(2);

        printer.println("Número de vagas: " + nf.format(election.electedAmount()));
        printer.println();

        System.out.println("Número de vagas: " + nf.format(election.electedAmount()));
        System.out.println();

        //===============================================================//
        String category="";
        if(election.getType() == 6)
            category = "federais";
        else if(election.getType() == 7)
            category = "estaduais";
            
        printer.printf("Deputados %s eleitos:\n", category);
        System.out.printf("Deputados %s eleitos:\n", category);

        for(Candidate c: election.electedCandidates()){
            printer.printf("%s - ", nf.format(c.getPosition()));
            System.out.printf("%s - ", nf.format(c.getPosition()));
            if(c.getParty().getFederation() != -1)
                printer.print("*");
                System.out.print("*");

            printer.printf("%s (%s, %s votos)\n", c.getNmUrnaCandidato(), c.getParty().getSg(), c.getQtVotos());
            System.out.printf("%s (%s, %s votos)\n", c.getNmUrnaCandidato(),
            c.getParty().getSg(), c.getQtVotos());
        }
        printer.printf("\n");
        System.out.print("\n");

        
        //===============================================================//
        printer.println("Candidatos mais votados (em ordem decrescente de votação e respeitando número de vagas):");
        System.out.println("Candidatos mais votados (em ordem decrescente de votação e respeitando número de vagas):");
        for(Candidate c: election.getBestCandidates()){
            printer.printf("%s - ", nf.format(c.getPosition()));
            System.out.printf("%s - ", nf.format(c.getPosition()));
            if(c.getParty().getFederation() != -1)
                printer.print("*");
                System.out.print("*");

            printer.printf("%s (%s, %s votos)\n", c.getNmUrnaCandidato(), c.getParty().getSg(), c.getQtVotos());
            System.out.printf("%s (%s, %s votos)\n", c.getNmUrnaCandidato(),
            c.getParty().getSg(), c.getQtVotos());
        }
        printer.print("\n");
        System.out.print("\n");
        

        //===============================================================//
        printer.println("Teriam sido eleitos se a votação fosse majoritária, e não foram eleitos:");
        printer.println("(com sua posição no ranking de mais votados)");
        System.out.println("Teriam sido eleitos se a votação fosse majoritária, e não foram eleitos:");
        System.out.println("(com sua posição no ranking de mais votados)");

        for(Candidate c: election.electedIfMajorElection()){
            printer.printf("%s - ", nf.format(c.getPosition()));
            System.out.printf("%s - ", nf.format(c.getPosition()));
            if(c.getParty().getFederation() != -1)
                printer.printf("*");
                System.out.print("*");
            
            printer.printf("%s (%s, %s votos)\n", c.getNmUrnaCandidato(), c.getParty().getSg(), c.getQtVotos());
            System.out.printf("%s (%s, %s votos)\n", c.getNmUrnaCandidato(),
                c.getParty().getSg(), c.getQtVotos());
        }
        printer.print("\n");
        System.out.print("\n");

        //===============================================================//
        printer.println("Eleitos, que se beneficiaram do sistema proporcional:");
        printer.println("(com sua posição no ranking de mais votados)");
        System.out.println("Eleitos, que se beneficiaram do sistema proporcional:");
        System.out.println("(com sua posição no ranking de mais votados)");
        for(Candidate c: election.electedByProportional()){
            printer.printf("%s - ", nf.format(c.getPosition()));
            System.out.printf("%s - ", nf.format(c.getPosition()));
            if(c.getParty().getFederation() != -1)
                printer.print("*");
                System.out.print("*");
            printer.printf("%s (%s, %s votos)\n", c.getNmUrnaCandidato(), c.getParty().getSg(), c.getQtVotos());
            System.out.printf("%s (%s, %s votos)\n", c.getNmUrnaCandidato(),
                c.getParty().getSg(), c.getQtVotos());
        }
        printer.print("\n");
        System.out.print("\n");

        //===============================================================//
        printer.println("Votação dos partidos e número de candidatos eleitos:");
        System.out.println("Votação dos partidos e número de candidatos eleitos:");
        for(PoliticalParty p: election.getParties()){
            printer.printf("%s - %s - %s, %s votos (%s nominais e %s de legenda), %s candidatos eleitos\n",
            nf.format(p.getPosition()), p.getSg(), nf.format(p.getNumber()), nf.format(p.getTotalVotes()),
            nf.format(p.getNominalVotes()), nf.format(p.getLegendVotes()), nf.format(p.getElectedAmount()));

            System.out.printf("%s - %s - %s, %s votos (%s nominais e %s de legenda), %s candidatos eleitos\n",
            nf.format(p.getPosition()), p.getSg(), nf.format(p.getNumber()), nf.format(p.getTotalVotes()),
            nf.format(p.getNominalVotes()), nf.format(p.getLegendVotes()), nf.format(p.getElectedAmount()));
        }
        printer.print("\n");
        System.out.print("\n");
        
        //===============================================================//
        printer.println("Primeiro e último colocados de cada partido:");
        System.out.println("Primeiro e último colocados de cada partido:");
        for(PoliticalParty p: election.getPartiesOrderedByCandidates()){
            printer.printf("%s - %s - %s, %s (%d, %s votos) / %s (%d, %s votos)\n",
            nf.format(p.getPosition()), p.getSg(), nf.format(p.getNumber()), p.mostVotedCandidate().getNmUrnaCandidato(), 
            p.mostVotedCandidate().getNrCandidato(), nf.format(p.mostVotedCandidate().getQtVotos()),
            p.leastVotedCandidate().getNmUrnaCandidato(), p.leastVotedCandidate().getNrCandidato(),
            nf.format(p.leastVotedCandidate().getQtVotos()));

            System.out.printf("%s - %s - %s, %s (%d, %s votos) / %s (%d, %s votos)\n",
            nf.format(p.getPosition()), p.getSg(), nf.format(p.getNumber()), p.mostVotedCandidate().getNmUrnaCandidato(), 
            p.mostVotedCandidate().getNrCandidato(), nf.format(p.mostVotedCandidate().getQtVotos()),
            p.leastVotedCandidate().getNmUrnaCandidato(), p.leastVotedCandidate().getNrCandidato(),
            nf.format(p.leastVotedCandidate().getQtVotos()));
        }
        printer.print("\n");
        System.out.print("\n");

        //===============================================================//
        printer.println("Eleitos, por faixa etária (na data da eleição):");
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

        printer.printf("      Idade < 30: %s (%s%%)\n", nf.format(f1), nfDec.format(p1));
        printer.printf("30 <= Idade < 40: %s (%s%%)\n", nf.format(f1), nfDec.format(p2));
        printer.printf("40 <= Idade < 50: %s (%s%%)\n", nf.format(f3), nfDec.format(p3));
        printer.printf("50 <= Idade < 60: %s (%s%%)\n", nf.format(f4), nfDec.format(p4));
        printer.printf("60 <= Idade     : %s (%s%%)\n\n", nf.format(f5), nfDec.format(p5));

        System.out.printf("      Idade < 30: %s (%s%%)\n", nf.format(f1), nfDec.format(p1));
        System.out.printf("30 <= Idade < 40: %s (%s%%)\n", nf.format(f1), nfDec.format(p2));
        System.out.printf("40 <= Idade < 50: %s (%s%%)\n", nf.format(f3), nfDec.format(p3));
        System.out.printf("50 <= Idade < 60: %s (%s%%)\n", nf.format(f4), nfDec.format(p4));
        System.out.printf("60 <= Idade     : %s (%s%%)\n\n", nf.format(f5), nfDec.format(p5));

        //DONE UNTIL THIS POINT
        //===============================================================//
        printer.println("Eleitos, por gênero:");
        System.out.println("Eleitos, por gênero:");
        int men = election.electedMen();
        int women = election.electedWomen();

        float pmen = ((float)men/(float)totalElected)*100;
        float pwomen = ((float)women/(float)totalElected)*100;


        printer.printf("Feminino: %s (%s%%)\n", nf.format(women), nfDec.format(pwomen));
        printer.printf("Masculino: %s (%s%%)\n\n", nf.format(men), nfDec.format(pmen));
        System.out.printf("Feminino: %s (%s%%)\n", nf.format(women), nfDec.format(pwomen));
        System.out.printf("Masculino: %s (%s%%)\n\n", nf.format(men), nfDec.format(pmen));

        //===============================================================//
        int validVotes = election.getLegendVotes() + election.getNominalVotes();
        int nominal = election.getNominalVotes();
        int legend = election.getLegendVotes();

        float pNominal = ((float)nominal/(float)validVotes) * 100;
        float pLegend = ((float)legend/(float)validVotes) * 100;

        printer.printf("Total de votos válidos:    %s\n", nf.format(validVotes));
        printer.printf("Total de votos nominais:   %s (%s%%)\n", nf.format(nominal), nfDec.format(pNominal));
        printer.printf("Total de votos de legenda: %s (%s%%)\n", nf.format(legend), nfDec.format(pLegend));

        System.out.printf("Total de votos válidos:    %s\n", nf.format(validVotes));
        System.out.printf("Total de votos nominais:   %s (%s%%)\n", nf.format(nominal), nfDec.format(pNominal));
        System.out.printf("Total de votos de legenda: %s (%s%%)\n", nf.format(legend), nfDec.format(pLegend));
    }


    public static PrintWriter createWritingBuffer(String filePath) throws IOException {

        try {
            PrintWriter printer = new PrintWriter(new FileWriter(filePath, Charset.forName("UTF-8")));
            return printer;
        } catch (Exception e) {
            throw e;
        }
    }
}
