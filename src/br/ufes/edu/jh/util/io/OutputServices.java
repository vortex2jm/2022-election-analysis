package br.ufes.edu.jh.util.io;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;

import br.ufes.edu.jh.domain.Candidate;
import br.ufes.edu.jh.domain.Election;
import br.ufes.edu.jh.domain.PoliticalParty;
import br.ufes.edu.jh.util.exceptions.ReportsGenerationException;

public class OutputServices {
    /**
     * Função que gera todos os relatórios
     * 
     * @param election: eleição que foi analisada
     * @throws ReportsGenerationException: Caso haja uma exceção de qualquer
     *                                     natureza dentro desta função
     */
    public static void generateReports(Election election) throws ReportsGenerationException {
        // Formatando os números no padrão brasileiro
        Locale localeBr = Locale.forLanguageTag("pt-BR");
        NumberFormat nf = NumberFormat.getInstance(localeBr);
        NumberFormat nfDec = NumberFormat.getInstance(localeBr);
        nfDec.setMinimumFractionDigits(2);
        nfDec.setMaximumFractionDigits(2);

        try {
            // Gerando as saídas
            vacanciesNumber(election, nf);
            electedCandidates(election, nf);
            mostVotedCandidates(election, nf);
            harmedCandidates(election, nf);
            benefitedCandidates(election, nf);
            partyVotingAndElectedCandidates(election, nf);
            firstAndLastCandidatesFromParties(election, nf);
            electedByAge(election, nf, nfDec);
            electedByGenre(election, nf, nfDec);
            allVoting(election, nf, nfDec);
        } catch (Exception e) {
            throw new ReportsGenerationException();
        }
    }

    // ===========================================================================================================//
    /**
     * Função que gera a quantidade de vagas da eleição em questão
     * 
     * @param election: eleição que foi analisada
     * @param nf:       o number format que deve ser utilizado
     * @throws IOException
     */
    private static void vacanciesNumber(Election election, NumberFormat nf) throws IOException {
        System.out.println("Número de vagas: " + nf.format(election.electedAmount()));
        System.out.println();
    }

    // ===========================================================================================================//
    /**
     * Função que gera os candidatos eleitos da eleição em questão
     * 
     * @param election: eleição que foi analisada
     * @param nf:       o number format que deve ser utilizado
     * @throws IOException
     */
    private static void electedCandidates(Election election, NumberFormat nf) throws IOException {
        String category = "";
        if (election.getType() == 6)
            category = "federais";
        else if (election.getType() == 7)
            category = "estaduais";

        System.out.printf("Deputados %s eleitos:\n", category);

        String vot;

        for (Candidate c : election.electedCandidates()) {
            System.out.printf("%s - ", nf.format(c.getElectedPosition()));
            if (c.getParty().getFederation() != -1)
                System.out.print("*");

            vot = "voto";
            vot = pluralSingularFilter(c.getQtVotos(), vot);
            System.out.printf("%s (%s, %s %s)\n", c.getNmUrnaCandidato(), c.getParty().getSg(),
                    nf.format(c.getQtVotos()), vot);
        }
        System.out.printf("\n");
    }

    // ===========================================================================================================//
    /**
     * Fuunção que gera os candidatos mais votados da eleição em questão
     * 
     * @param election: eleição que foi analisada
     * @param nf:       o number format que deve ser utilizado
     * @throws IOException
     */
    private static void mostVotedCandidates(Election election, NumberFormat nf) throws IOException {
        System.out.println("Candidatos mais votados (em ordem decrescente de votação e respeitando número de vagas):");

        String vot;

        for (Candidate c : election.getBestCandidates()) {
            System.out.printf("%s - ", nf.format(c.getGeralPosition()));
            if (c.getParty().getFederation() != -1)
                System.out.print("*");

            vot = "voto";
            vot = pluralSingularFilter(c.getQtVotos(), vot);
            System.out.printf("%s (%s, %s %s)\n", c.getNmUrnaCandidato(), c.getParty().getSg(),
                    nf.format(c.getQtVotos()), vot);
        }
        System.out.print("\n");
    }

    // ===========================================================================================================//
    /**
     * Função que gera candidatos que não foram eleitos, mas que seriam se a eleição
     * fosse majoritária
     * 
     * @param election: eleição que foi analisada
     * @param nf:       o number format que deve ser utilizado
     * @throws IOException
     */
    private static void harmedCandidates(Election election, NumberFormat nf) throws IOException {
        System.out.println("Teriam sido eleitos se a votação fosse majoritária, e não foram eleitos:");
        System.out.println("(com sua posição no ranking de mais votados)");

        String vot;

        for (Candidate c : election.electedIfMajorElection()) {
            System.out.printf("%s - ", nf.format(c.getGeralPosition()));
            if (c.getParty().getFederation() != -1)
                System.out.printf("*");

            vot = "voto";
            vot = pluralSingularFilter(c.getQtVotos(), vot);
            System.out.printf("%s (%s, %s %s)\n", c.getNmUrnaCandidato(), c.getParty().getSg(),
                    nf.format(c.getQtVotos()), vot);
        }
        System.out.print("\n");
    }

    // ===========================================================================================================//
    /**
     * Função que gera candidatos eleitos que se beneficiaram do sistema
     * proporcional
     * 
     * @param election: eleição que foi analisada
     * @param nf:       o number format que deve ser utilizado
     * @throws IOException
     */
    private static void benefitedCandidates(Election election, NumberFormat nf) throws IOException {
        System.out.println("Eleitos, que se beneficiaram do sistema proporcional:");
        System.out.println("(com sua posição no ranking de mais votados)");

        String vot;

        for (Candidate c : election.electedByProportional()) {
            System.out.printf("%s - ", nf.format(c.getGeralPosition()));
            if (c.getParty().getFederation() != -1)
                System.out.print("*");

            vot = "voto";
            vot = pluralSingularFilter(c.getQtVotos(), vot);
            System.out.printf("%s (%s, %s %s)\n", c.getNmUrnaCandidato(), c.getParty().getSg(),
                    nf.format(c.getQtVotos()), vot);
        }
        System.out.print("\n");
    }

    // ===========================================================================================================//
    /**
     * Função que gera os partidos e suas informações
     * 
     * @param election eleição que foi analisada
     * @param nf       o number format que deve ser utilizado
     * @throws IOException
     */
    private static void partyVotingAndElectedCandidates(Election election, NumberFormat nf) throws IOException {
        System.out.println("Votação dos partidos e número de candidatos eleitos:");

        String vot, nom, cand, ele;

        for (PoliticalParty p : election.getParties()) {
            vot = "voto";
            nom = "nominal";
            cand = "candidato";
            ele = "eleito";

            vot = pluralSingularFilter(p.getTotalVotes(), vot);
            nom = pluralSingularFilter(p.getNominalVotes(), nom);
            cand = pluralSingularFilter(p.getElectedAmount(), cand);
            ele = pluralSingularFilter(p.getElectedAmount(), ele);

            System.out.printf("%s - %s - %s, %s %s (%s %s e %s de legenda), %s %s %s\n",
                    nf.format(p.getPosition()), p.getSg(), nf.format(p.getNumber()), nf.format(p.getTotalVotes()), vot,
                    nf.format(p.getNominalVotes()), nom, nf.format(p.getLegendVotes()), nf.format(p.getElectedAmount()),
                    cand, ele);
        }
        System.out.print("\n");
    }

    // ===========================================================================================================//
    /**
     * Função que gera o primeiro e último colocado de cada partido
     * 
     * @param election eleição que foi analisada
     * @param nf       o number format que deve ser utilizado
     * @throws IOException
     */
    private static void firstAndLastCandidatesFromParties(Election election, NumberFormat nf) throws IOException {
        System.out.println("Primeiro e último colocados de cada partido:");

        String mostVot, leastVot;

        for (PoliticalParty p : election.getPartiesOrderedByCandidates()) {
            mostVot = "voto";
            leastVot = "voto";
            mostVot = pluralSingularFilter(p.mostVotedCandidate().getQtVotos(), mostVot);
            leastVot = pluralSingularFilter(p.leastVotedCandidate().getQtVotos(), leastVot);

            System.out.printf("%s - %s - %s, %s (%d, %s %s) / %s (%d, %s %s)\n",
                    nf.format(p.getPosition()), p.getSg(), nf.format(p.getNumber()),
                    p.mostVotedCandidate().getNmUrnaCandidato(),
                    p.mostVotedCandidate().getNrCandidato(), nf.format(p.mostVotedCandidate().getQtVotos()), mostVot,
                    p.leastVotedCandidate().getNmUrnaCandidato(), p.leastVotedCandidate().getNrCandidato(),
                    nf.format(p.leastVotedCandidate().getQtVotos()), leastVot);
        }
        System.out.print("\n");
    }

    // ===========================================================================================================//
    /**
     * Função que gera a quantidade de deputados eleitos por faixa etária
     * 
     * @param election eleição que foi analisada
     * @param nf       o number format que deve ser utilizado
     * @param nfDec    o number format que deve ser utilizado para os números em
     *                 porcentagem
     * @throws IOException
     */
    private static void electedByAge(Election election, NumberFormat nf, NumberFormat nfDec) throws IOException {
        System.out.println("Eleitos, por faixa etária (na data da eleição):");

        int totalElected = election.electedAmount();

        int f1 = election.electedAmountByAge(0, 30);
        int f2 = election.electedAmountByAge(30, 40);
        int f3 = election.electedAmountByAge(40, 50);
        int f4 = election.electedAmountByAge(50, 60);
        int f5 = election.electedAmountByAge(60, 120);

        float p1 = ((float) f1 / (float) totalElected) * 100;
        float p2 = ((float) f2 / (float) totalElected) * 100;
        float p3 = ((float) f3 / (float) totalElected) * 100;
        float p4 = ((float) f4 / (float) totalElected) * 100;
        float p5 = ((float) f5 / (float) totalElected) * 100;

        System.out.printf("      Idade < 30: %s (%s%%)\n", nf.format(f1), nfDec.format(p1));
        System.out.printf("30 <= Idade < 40: %s (%s%%)\n", nf.format(f2), nfDec.format(p2));
        System.out.printf("40 <= Idade < 50: %s (%s%%)\n", nf.format(f3), nfDec.format(p3));
        System.out.printf("50 <= Idade < 60: %s (%s%%)\n", nf.format(f4), nfDec.format(p4));
        System.out.printf("60 <= Idade     : %s (%s%%)\n\n", nf.format(f5), nfDec.format(p5));
    }

    // ===========================================================================================================//
    /**
     * Função que gera a quantidade de deputados eleitos por genero
     * 
     * @param election eleição que foi analisada
     * @param nf       o number format que deve ser utilizado
     * @param nfDec    o number format que deve ser utilizado para os números em
     *                 porcentagem
     * @throws IOException
     */
    private static void electedByGenre(Election election, NumberFormat nf, NumberFormat nfDec) throws IOException {
        int totalElected = election.electedAmount();
        System.out.println("Eleitos, por gênero:");

        int men = election.electedMen();
        int women = election.electedWomen();

        float pmen = ((float) men / (float) totalElected) * 100;
        float pwomen = ((float) women / (float) totalElected) * 100;

        System.out.printf("Feminino:  %s (%s%%)\n", nf.format(women), nfDec.format(pwomen));
        System.out.printf("Masculino: %s (%s%%)\n\n", nf.format(men), nfDec.format(pmen));
    }

    // ===========================================================================================================//
    /**
     * Função que gera a quantidade de votos nominais, de legenda e totais
     * 
     * @param election eleição que foi analisada
     * @param nf       o number format que deve ser utilizado
     * @param nfDec    o number format que deve ser utilizado para os números em
     *                 porcentagem
     * @throws IOException
     */
    private static void allVoting(Election election, NumberFormat nf, NumberFormat nfDec) throws IOException {
        int validVotes = election.getLegendVotes() + election.getNominalVotes();
        int nominal = election.getNominalVotes();
        int legend = election.getLegendVotes();

        float pNominal = ((float) nominal / (float) validVotes) * 100;
        float pLegend = ((float) legend / (float) validVotes) * 100;

        System.out.printf("Total de votos válidos:    %s\n", nf.format(validVotes));
        System.out.printf("Total de votos nominais:   %s (%s%%)\n", nf.format(nominal), nfDec.format(pNominal));
        System.out.printf("Total de votos de legenda: %s (%s%%)\n", nf.format(legend), nfDec.format(pLegend));
    }

    // ===========================================================================================================//
    /**
     * Função que verifica se o texto estará no plural ou no singular com base em um
     * valor inteiro
     * 
     * @param value valor referencial para decidir singular ou plural
     * @param out   String que deve ser tratada
     * @return String passada no parâmetro no plural ou singular
     */
    private static String pluralSingularFilter(int value, String out) {
        if (value > 1) {
            if (out.compareTo("nominal") == 0) {
                return "nominais";
            }
            return out.concat("s");
        }
        return out;
    }
}
