package br.ufes.edu.jh.util.exceptions;

public class VotesFileProcessException extends Exception {
    @Override
    public String getMessage() {
        return "Falha ao processar arquivo de votação (votacao.csv)";
    }
}
