package br.ufes.edu.jh;
import java.io.BufferedReader;
import java.time.LocalDate;

import br.ufes.edu.jh.domain.Election;
import br.ufes.edu.jh.util.io.InputServices;
import br.ufes.edu.jh.util.io.OutputServices;

public class App {
    public static void main(String[] args) throws Exception {

        //checks the desired processing
        int type;
        if(args[0].compareTo("--estadual") == 0)
            type = 7;
        else if(args[0].compareTo("--federal") == 0)
            type = 6;
        else
            throw new Exception("Argumento inválido"); 

        //creates buffers for the target files
        BufferedReader bufferCandidates = InputServices.createBuffer(args[1]);
        BufferedReader bufferVotes = InputServices.createBuffer(args[2]);

        //deals with the date format
        String[] date = args[3].split("/");
        int day = Integer.parseInt(date[0]);
        int month = Integer.parseInt(date[1]);
        int year = Integer.parseInt(date[2]);

        Election election = new Election(type, LocalDate.of(year, month, day));

        InputServices.processCandidatesFile(bufferCandidates, election);
        InputServices.processVotesFile(bufferVotes, election);
        election.setParties(InputServices.checksParties(election));

        OutputServices.generateReports(election);
    }
}
