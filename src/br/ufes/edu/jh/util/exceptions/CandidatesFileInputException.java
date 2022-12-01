package br.ufes.edu.jh.util.exceptions;

public class CandidatesFileInputException extends Exception {
    @Override
    public String getMessage() {
        return "Falha ao abrir ou encontrar arquivo de candidatos (candidatos.csv)";
    }
}
