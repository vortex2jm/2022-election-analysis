package br.ufes.edu.jh;
import java.io.BufferedReader;

import br.ufes.edu.jh.domain.Election;
import br.ufes.edu.jh.util.io.InputServices;
import br.ufes.edu.jh.util.io.OutputServices;

public class App {
    public static void main(String[] args) throws Exception {

        int type = validateArgs(args[0]);
        BufferedReader bufferCandidates = InputServices.createBuffer(args[1]);
        BufferedReader bufferVotes = InputServices.createBuffer(args[2]);

        Election election2022 = new Election(type);

        InputServices.processCandidatesFile(bufferCandidates, election2022);
        InputServices.processVotesFile(bufferVotes, election2022);
        OutputServices.generateReports(election2022);
    }

    public static int validateArgs(String args) throws Exception {
        if (args.compareTo("--estadual") == 0)
            return 7;
        if (args.compareTo("--federal") == 0)
            return 6;
        throw new Exception("Argumento inválido");
    }
}
