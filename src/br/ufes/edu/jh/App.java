package br.ufes.edu.jh;

import java.io.BufferedReader;
import java.time.LocalDate;

import br.ufes.edu.jh.domain.Election;
import br.ufes.edu.jh.util.exceptions.CandidatesFileInputException;
import br.ufes.edu.jh.util.exceptions.ElectionDateException;
import br.ufes.edu.jh.util.exceptions.VotesFileInputException;
import br.ufes.edu.jh.util.io.InputServices;
import br.ufes.edu.jh.util.io.OutputServices;

public class App {
    public static void main(String[] args) throws Exception {
        // Checando o tipo da eleição
        int type;
        if (args[0].compareTo("--estadual") == 0)
            type = 7;
        else if (args[0].compareTo("--federal") == 0)
            type = 6;
        else
            throw new Exception("Argumento inválido");

        // Criando buffer de entrada dos arquivos
        BufferedReader bufferCandidates, bufferVotes;
        try {
            bufferCandidates = InputServices.createReadingBuffer(args[1]);
        } catch (Exception e) {
            throw new CandidatesFileInputException();
        }
        try {
            bufferVotes = InputServices.createReadingBuffer(args[2]);
        } catch (Exception e) {
            throw new VotesFileInputException();
        }

        // Instanciando a data da eleição
        String[] date = args[3].split("/");
        int day;
        int month;
        int year;
        try {
            day = Integer.parseInt(date[0]);
            month = Integer.parseInt(date[1]);
            year = Integer.parseInt(date[2]);
        } catch (Exception e) {
            System.out.println("A string não pode ser convertida em um tipo numérico");
            throw new ElectionDateException();
        }

        // Instanciando a eleição
        Election election = new Election(type, LocalDate.of(year, month, day));

        try {
            // Processando os dados de entrada
            InputServices.processCandidatesFile(bufferCandidates, election);
            InputServices.processVotesFile(bufferVotes, election);

            // Gerando a saída
            OutputServices.generateReports(election);
        } catch (Exception e) {
            throw e;
        }
    }
}
