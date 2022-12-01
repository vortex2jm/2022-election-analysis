package br.ufes.edu.jh.util.exceptions;

public class CandidatesFileProcessException extends Exception {
    @Override
    public String getMessage() {
        return "Falha ao processar arquivo de candidatos (candidatos.csv)";
    }
}
