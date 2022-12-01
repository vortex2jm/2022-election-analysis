package br.ufes.edu.jh.util.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDate;

import br.ufes.edu.jh.domain.Election;
import br.ufes.edu.jh.domain.PoliticalParty;
import br.ufes.edu.jh.util.exceptions.CandidatesFileProcessException;
import br.ufes.edu.jh.util.exceptions.VotesFileProcessException;

public class InputServices {
    // ======PUBLIC======//
    // ==================================================================================================//
    /**
     * @param args caminho para o arquivo a ser lido
     * @return um bufferedReader que será utilizado em outras funções
     * @throws Exception caso o arquivo não seja encontrado
     */
    public static BufferedReader createReadingBuffer(String args) throws IOException {
        try {
            BufferedReader buffer = new BufferedReader(new FileReader(new File(args), Charset.forName("ISO-8859-1")));
            return buffer;
        } catch (IOException e) {
            throw e;
        }
    }

    // ==================================================================================================//
    /**
     * @param bufferCandidates buffer utilizado para o arquivo de candidatos
     * @param election         a eleição que está sendo analisada
     * @throws Exception caso algum problema seja encontrado no processamento do
     *                   arquivo
     */
    public static void processCandidatesFile(BufferedReader bufferCandidates, Election election)
            throws CandidatesFileProcessException {

        String currentLine;
        String[] currentData;
        PoliticalParty party;

        try {
            bufferCandidates.readLine(); // Eliminando linha de cabeçalho
            while ((currentLine = bufferCandidates.readLine()) != null) {

                // Removendo aspas e separando por ";"
                currentData = inputFormatter(currentLine);

                // Atualizando mapa de partidos
                party = updateParties(election, currentData);

                // caso candidatura válida
                if (candidateIsValid(currentData[13], currentData[68], election.getType())) {
                    updateCandidates(election, currentData, party);
                    continue;
                }
                // caso candidatura inválida mas candidato de interesse(importante para
                // processamento de potenciais votos de legenda)
                if (election.getType() == Integer.parseInt(currentData[13])
                        && currentData[67].compareTo("Válido (legenda)") == 0) {
                    updateInvalidCandidates(election, currentData, party);
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler uma linha do arquivo de candidatos");
            throw new CandidatesFileProcessException();
        } catch (NumberFormatException e) {
            System.out.println("A string não pode ser convertida em um tipo numérico");
            throw new CandidatesFileProcessException();
        }
    }

    // ==================================================================================================//
    /**
     * @param bufferVotes buffer utilizado para a leitura do arquivo de votação
     * @param election    a eleição a ser analisada
     * @throws IOException
     * @throws Exception   caso algum problema seja encontrado no processamento
     */
    public static void processVotesFile(BufferedReader bufferVotes, Election election)
            throws VotesFileProcessException {

        String currentLine;
        String[] currentData;

        try {
            bufferVotes.readLine(); // Removendo linha de cabeçalho
            while ((currentLine = bufferVotes.readLine()) != null) {

                // Removendo aspas e separando por ";"
                currentData = inputFormatter(currentLine);

                // Se o voto é válido, é processado
                if (voteIsValid(currentData[17], election.getType(), currentData[19])) {
                    processValidCandidatesVotes(election, currentData);
                    processInvalidCandidatesVotes(election, currentData);
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler uma linha do arquivo de votos");
            throw new VotesFileProcessException();
        } catch (NumberFormatException e) {
            System.out.println("A string não pode ser convertida em um tipo numérico");
            throw new VotesFileProcessException();
        }
    }

    // ========PRIVATE=======//
    // ==================================================================================================//
    /**
     * @param cdCargo                codigo do candidato para indicar se é federal
     *                               ou estadual
     * @param cdDetalhesSituacaoCand código para indicar se o candidato é válido ou
     *                               não
     * @param type                   tipo de análise da eleição (nível federal ou
     *                               estadual)
     * @return booleano indicando se é (ou não) um candidato de interesse
     */
    private static boolean candidateIsValid(String cdCargo, String cdDetalhesSituacaoCand, int type)
            throws NumberFormatException {
        int cdC, cdD;

        try {
            cdC = Integer.parseInt(cdCargo);
            cdD = Integer.parseInt(cdDetalhesSituacaoCand);
        } catch (Exception e) {
            System.out.println("A string não pode ser convertida em um tipo numérico");
            throw e;
        }

        if (cdC == type && (cdD == 2 || cdD == 16))
            return true;
        return false;
    }

    // ===========================================================================//
    /**
     * @param cdCargo   codigo do candidato para indicar se é federal ou estadual
     * @param type      tipo de análise da eleição (nível federal ou estadual)
     * @param nrVotavel numero do candidato na urna
     * @return booleano indicando se os votos do candidato são (ou não) de interesse
     */
    private static boolean voteIsValid(String cdCargo, int type, String nrVotavel) throws NumberFormatException {
        int cdC, nrV;

        try {
            cdC = Integer.parseInt(cdCargo);
            nrV = Integer.parseInt(nrVotavel);

        } catch (Exception e) {
            System.out.println("A string não pode ser convertida em um tipo numérico");
            throw e;
        }

        if (cdC == type && nrV != 95 && nrV != 96 && nrV != 97 && nrV != 98)
            return true;
        return false;
    }

    // ==================================================================================================//
    /**
     * @param election a eleição a ser analisada
     * @param data     linha de dados do arquivo de candidatos
     * @return um partido político novo
     */
    private static PoliticalParty updateParties(Election election, String[] data) throws NumberFormatException {
        int nrPartido, nrFederacao;
        String sgPartido = data[28];

        try {
            nrPartido = Integer.parseInt(data[27]);
            nrFederacao = Integer.parseInt(data[30]);
        } catch (Exception e) {
            System.out.println("A string não pode ser convertida em um tipo numérico");
            throw e;
        }

        // Se o mapa já possui o partido, ele apenas retorna o partido sem criar um novo
        if (election.getPartiesMap().containsKey(nrPartido)) {
            return election.getPartiesMap().get(nrPartido);
        }
        return election.addPartie(nrPartido, sgPartido, nrFederacao);
    }

    // ==================================================================================================//
    /**
     * @param line linha de dados de um arquivo csv do TSE
     * @return a linha com os dados prontos para serem utilizados
     */
    private static String[] inputFormatter(String line) {
        String[] currentData = line.split(";");
        for (int i = 0; i < currentData.length; i++) {
            currentData[i] = currentData[i].replaceAll("\"", "");
        }
        return currentData;
    }

    // ==================================================================================================//
    /**
     * @param election eleição a ser analisada
     * @param data     linha de dado do arquivo de candidatos
     * @param party    partido do candidato
     */
    private static void updateCandidates(Election election, String[] data, PoliticalParty party)
            throws NumberFormatException {
        // Instanciando uma data

        int day;
        int month;
        int year;
        int nrCandidato;
        int cdGenero;
        String[] date = data[42].split("/");
        String nmUrnaCandidato = data[18];
        String nmTipoDestinoVotos = data[67];
        LocalDate dtNsc;
        boolean situation;

        try {
            day = Integer.parseInt(date[0]);
            month = Integer.parseInt(date[1]);
            year = Integer.parseInt(date[2]);
            dtNsc = LocalDate.of(year, month, day);
            situation = isElectedCandidate(data[56]);
            nrCandidato = Integer.parseInt(data[16]);
            cdGenero = Integer.parseInt(data[45]);
            election.addCandidate(nrCandidato, nmUrnaCandidato, nmTipoDestinoVotos, dtNsc, situation, cdGenero, party);
        } catch (Exception e) {
            System.out.println("A string não pode ser convertida em um tipo numérico");
            throw e;
        }
    }

    // ==================================================================================================//
    /**
     * @param election a eleição a ser analisada
     * @param data     linha de dados do arquivo de candidatos
     * @param party    partido do candidato
     */
    private static void updateInvalidCandidates(Election election, String[] data, PoliticalParty party)
            throws NumberFormatException {

        int nrCandidato;
        try {
            nrCandidato = Integer.parseInt(data[16]);
        } catch (Exception e) {
            System.out.println("A string não pode ser convertida em um tipo numérico");
            throw e;
        }

        election.addLegendsCandidatesParties(nrCandidato, party);
    }

    // ==================================================================================================//
    /**
     * @param sit código de situação do candidato ao fim do turno
     * @return booleano indicando se candidato foi eleito
     */
    private static boolean isElectedCandidate(String sit) throws NumberFormatException {
        int situation;

        try {
            situation = Integer.parseInt(sit);
        } catch (Exception e) {
            System.out.println("A string não pode ser convertida em um tipo numérico");
            throw e;
        }

        if (situation == 2 || situation == 3)
            return true;
        return false;
    }

    // ==================================================================================================//
    /**
     * @param election eleição a ser analisada
     * @param data     linha de dados do arquivo de votos
     */
    private static void processValidCandidatesVotes(Election election, String[] data) throws NumberFormatException {
        int nrVotavel, qtVotos;

        try {
            nrVotavel = Integer.parseInt(data[19]);
            qtVotos = Integer.parseInt(data[21]);
        } catch (Exception e) {
            System.out.println("A string não pode ser convertida em um tipo numérico");
            throw e;
        }

        // Se o número é de um candidato, conta como voto nominal
        if (election.getCandidatesMap().containsKey(nrVotavel)) {
            election.getCandidatesMap().get(nrVotavel).setQtVotos(qtVotos);
            election.setNominalVotes(qtVotos);
            return;
        }

        // Se o número é de um partido, conta como voto de legenda
        if (election.getPartiesMap().containsKey(nrVotavel)) {
            election.getPartiesMap().get(nrVotavel).setLegendVotes(qtVotos);
            election.setLegendVotes(qtVotos);
        }
    }

    // ==================================================================================================//
    /**
     * @param election eleição a ser analisada
     * @param data     linha de dados do arquivo de votos
     */
    private static void processInvalidCandidatesVotes(Election election, String[] data) throws NumberFormatException {
        int nrVotavel, qtVotos;

        try {
            nrVotavel = Integer.parseInt(data[19]);
            qtVotos = Integer.parseInt(data[21]);
        } catch (Exception e) {
            System.out.println("A string não pode ser convertida em um tipo numérico");
            throw e;
        }

        // Se o número do candidato está no mapa de candidatos inválidos, conta como
        // voto de legenda
        if (election.getLegendsCandidatesParties().containsKey(nrVotavel)) {
            election.getLegendsCandidatesParties().get(nrVotavel).setLegendVotes(qtVotos);
            election.setLegendVotes(qtVotos);
        }
    }
}
