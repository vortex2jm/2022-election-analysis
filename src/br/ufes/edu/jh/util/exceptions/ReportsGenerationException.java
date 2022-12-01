package br.ufes.edu.jh.util.exceptions;

public class ReportsGenerationException extends Exception {
    @Override
    public String getMessage() {
        return "Erro no processamento de saída";
    }
}
