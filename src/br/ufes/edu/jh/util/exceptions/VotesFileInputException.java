package br.ufes.edu.jh.util.exceptions;

public class VotesFileInputException extends Exception {
    @Override
    public String getMessage() {
        return "Falha ao abrir ou encontrar arquivo de votação (votacao.csv)";
    }
}
