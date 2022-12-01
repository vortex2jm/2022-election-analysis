package br.ufes.edu.jh.util.exceptions;

public class ElectionDateException extends Exception {
    @Override
    public String getMessage() {
        return "Falha ao gerar data da eleição";
    }
}
