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
        // Formatting numbers
        Locale localeBr = Locale.forLanguageTag("pt-BR");
        NumberFormat nf = NumberFormat.getInstance(localeBr);
        NumberFormat nfDec = NumberFormat.getInstance(localeBr);
        nfDec.setMinimumFractionDigits(2);
        nfDec.setMaximumFractionDigits(2);

        // Generating outputs
        vacanciesNumber(election, printer, nf);
        electedCandidates(election, printer, nf);
        mostVotedCandidates(election, printer, nf);
        harmedCandidates(election, printer, nf);
        benefitedCandidates(election, printer, nf);
        partyVotingAndElectedCandidates(election, printer, nf);
        firstAndLastCandidatesFromParties(election, printer, nf);
        electedByAge(election, printer, nf, nfDec);
        electedByGender(election, printer, nf, nfDec);
        allVoting(election, printer, nf, nfDec);
    }

    //===========================================================================================================//
    private static void vacanciesNumber(Election election, PrintWriter printer, NumberFormat nf) throws IOException {
        printer.println("Número de vagas: " + nf.format(election.electedAmount()));
        printer.println();
    }

    //===========================================================================================================//
    private static void electedCandidates(Election election, PrintWriter printer, NumberFormat nf) throws IOException {
        String category="";
        if(election.getType() == 6)
            category = "federais";
        else if(election.getType() == 7)
            category = "estaduais";
            
        printer.printf("Deputados %s eleitos:\n", category);

        String vot;

        for(Candidate c: election.electedCandidates()){
            printer.printf("%s - ", nf.format(c.getElectedPosition()));
            if(c.getParty().getFederation() != -1)
                printer.print("*");

            vot = "voto";
            vot = pluralSingularFilter(c.getQtVotos(), vot);
            printer.printf("%s (%s, %s %s)\n", c.getNmUrnaCandidato(), c.getParty().getSg(), nf.format(c.getQtVotos()), vot);
        }
        printer.printf("\n");
    }

    //===========================================================================================================//
    private static void mostVotedCandidates(Election election, PrintWriter printer, NumberFormat nf) throws IOException {
        printer.println("Candidatos mais votados (em ordem decrescente de votação e respeitando número de vagas):");
        
        String vot;
        
        for(Candidate c: election.getBestCandidates()){
            printer.printf("%s - ", nf.format(c.getGeralPosition()));
            if(c.getParty().getFederation() != -1)
                printer.print("*");

            vot = "voto";
            vot = pluralSingularFilter(c.getQtVotos(), vot);
            printer.printf("%s (%s, %s %s)\n", c.getNmUrnaCandidato(), c.getParty().getSg(), nf.format(c.getQtVotos()), vot);
        }
        printer.print("\n");
    }

    //===========================================================================================================//
    private static void harmedCandidates(Election election, PrintWriter printer, NumberFormat nf) throws IOException {
        printer.println("Teriam sido eleitos se a votação fosse majoritária, e não foram eleitos:");
        printer.println("(com sua posição no ranking de mais votados)");

        String vot;

        for(Candidate c: election.electedIfMajorElection()){
            printer.printf("%s - ", nf.format(c.getGeralPosition()));
            if(c.getParty().getFederation() != -1)
                printer.printf("*");
            
            vot = "voto";
            vot = pluralSingularFilter(c.getQtVotos(), vot);
            printer.printf("%s (%s, %s %s)\n", c.getNmUrnaCandidato(), c.getParty().getSg(), nf.format(c.getQtVotos()), vot);
        }
        printer.print("\n");
    }

    //===========================================================================================================//
    private static void benefitedCandidates(Election election, PrintWriter printer, NumberFormat nf) throws IOException {
        printer.println("Eleitos, que se beneficiaram do sistema proporcional:");
        printer.println("(com sua posição no ranking de mais votados)");

        String vot;

        for(Candidate c: election.electedByProportional()){
            printer.printf("%s - ", nf.format(c.getGeralPosition()));
            if(c.getParty().getFederation() != -1)
                printer.print("*");
            
            vot = "voto";
            vot = pluralSingularFilter(c.getQtVotos(), vot);
            printer.printf("%s (%s, %s %s)\n", c.getNmUrnaCandidato(), c.getParty().getSg(), nf.format(c.getQtVotos()), vot);
        }
        printer.print("\n");
    }

    //===========================================================================================================//
    private static void partyVotingAndElectedCandidates(Election election, PrintWriter printer, NumberFormat nf) throws IOException {
        printer.println("Votação dos partidos e número de candidatos eleitos:");

        String vot, nom, cand, ele;

        for(PoliticalParty p: election.getParties()){
            vot = "voto";
            nom = "nominal";
            cand = "candidato";
            ele = "eleito";

            vot = pluralSingularFilter(p.getTotalVotes(), vot);
            nom = pluralSingularFilter(p.getNominalVotes(), nom);
            cand = pluralSingularFilter(p.getElectedAmount(), cand);
            ele = pluralSingularFilter(p.getElectedAmount(), ele);

            printer.printf("%s - %s - %s, %s %s (%s %s e %s de legenda), %s %s %s\n",
            nf.format(p.getPosition()), p.getSg(), nf.format(p.getNumber()), nf.format(p.getTotalVotes()), vot,
            nf.format(p.getNominalVotes()), nom, nf.format(p.getLegendVotes()), nf.format(p.getElectedAmount()), cand, ele);
        }
        printer.print("\n");
    }

    //===========================================================================================================//
    private static void firstAndLastCandidatesFromParties(Election election, PrintWriter printer, NumberFormat nf) throws IOException {
        printer.println("Primeiro e último colocados de cada partido:");

        String mostVot, leastVot;

        for(PoliticalParty p: election.getPartiesOrderedByCandidates()){
            mostVot = "voto";
            leastVot = "voto";
            mostVot = pluralSingularFilter(p.mostVotedCandidate().getQtVotos(), mostVot);
            leastVot= pluralSingularFilter(p.leastVotedCandidate().getQtVotos(), leastVot);

            printer.printf("%s - %s - %s, %s (%d, %s %s) / %s (%d, %s %s)\n",
            nf.format(p.getPosition()), p.getSg(), nf.format(p.getNumber()), p.mostVotedCandidate().getNmUrnaCandidato(), 
            p.mostVotedCandidate().getNrCandidato(), nf.format(p.mostVotedCandidate().getQtVotos()), mostVot,
            p.leastVotedCandidate().getNmUrnaCandidato(), p.leastVotedCandidate().getNrCandidato(),
            nf.format(p.leastVotedCandidate().getQtVotos()), leastVot);
        }
        printer.print("\n");
    }

    //===========================================================================================================//
    private static void electedByAge(Election election, PrintWriter printer, NumberFormat nf, NumberFormat nfDec) throws IOException {
        printer.println("Eleitos, por faixa etária (na data da eleição):");

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
        printer.printf("30 <= Idade < 40: %s (%s%%)\n", nf.format(f2), nfDec.format(p2));
        printer.printf("40 <= Idade < 50: %s (%s%%)\n", nf.format(f3), nfDec.format(p3));
        printer.printf("50 <= Idade < 60: %s (%s%%)\n", nf.format(f4), nfDec.format(p4));
        printer.printf("60 <= Idade     : %s (%s%%)\n\n", nf.format(f5), nfDec.format(p5));
    }

    //===========================================================================================================//
    private static void electedByGender(Election election, PrintWriter printer, NumberFormat nf, NumberFormat nfDec) throws IOException {
        int totalElected = election.electedAmount();
        printer.println("Eleitos, por gênero:");

        int men = election.electedMen();
        int women = election.electedWomen();

        float pmen = ((float)men/(float)totalElected)*100;
        float pwomen = ((float)women/(float)totalElected)*100;

        printer.printf("Feminino:  %s (%s%%)\n", nf.format(women), nfDec.format(pwomen));
        printer.printf("Masculino: %s (%s%%)\n\n", nf.format(men), nfDec.format(pmen));
    }

    //===========================================================================================================//
    private static void allVoting(Election election, PrintWriter printer, NumberFormat nf, NumberFormat nfDec) throws IOException {
        int validVotes = election.getLegendVotes() + election.getNominalVotes();
        int nominal = election.getNominalVotes();
        int legend = election.getLegendVotes();

        float pNominal = ((float)nominal/(float)validVotes) * 100;
        float pLegend = ((float)legend/(float)validVotes) * 100;

        printer.printf("Total de votos válidos:    %s\n", nf.format(validVotes));
        printer.printf("Total de votos nominais:   %s (%s%%)\n", nf.format(nominal), nfDec.format(pNominal));
        printer.printf("Total de votos de legenda: %s (%s%%)\n", nf.format(legend), nfDec.format(pLegend));
    }

    //===========================================================================================================//
    private static String pluralSingularFilter(int value, String out){
        if(value > 1){
            if(out.compareTo("nominal") == 0){
                return "nominais";
            }
            return out.concat("s");
        }
        return out;
    }

    //===========================================================================================================//
    public static PrintWriter createWritingBuffer(String filePath) throws IOException {
        try {
            PrintWriter printer = new PrintWriter(new FileWriter(filePath, Charset.forName("UTF-8")));
            return printer;
        } catch (Exception e) {
            throw e;
        }
    }
}
