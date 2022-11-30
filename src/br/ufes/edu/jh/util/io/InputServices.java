package br.ufes.edu.jh.util.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.charset.Charset;
import java.time.LocalDate;

import br.ufes.edu.jh.domain.Election;
import br.ufes.edu.jh.domain.PoliticalParty;

public class InputServices {
                                        //======PUBLIC======//
    //==================================================================================================//
    public static BufferedReader createReadingBuffer(String args) throws Exception {
        try {
            BufferedReader buffer = new BufferedReader(new FileReader(new File(args),Charset.forName("ISO-8859-1")));
            return buffer;
        } catch (Exception e) {
            throw e;
        }
    }

    //==================================================================================================//
    public static void processCandidatesFile(BufferedReader bufferCandidates, Election election)
            throws Exception {
        
        String currentLine;
        String[] currentData;
        PoliticalParty party;

        bufferCandidates.readLine(); // Eliminando linha de legenda
        while ((currentLine = bufferCandidates.readLine()) != null) {

            // Formatando os dados
            currentData = InputServices.dataFormatter(currentLine);

            // Atualizando lista de candidatos e partidos
            party = InputServices.updateParties(election, currentData);
            //caso candidatura válida e candidato de interesse
            if (InputServices.candidateIsValid(currentData[13], currentData[68], election.getType())) {
                InputServices.updateCandidates(election, currentData, party);
                continue;
            }
            //caso candidatura inválida mas candidato de interesse(importante para processamento de potenciais votos de legenda)
            else if(election.getType() == Integer.parseInt(currentData[13])){
                InputServices.updateInvalidCandidates(election, currentData, party);
                continue;
            }
        }
    }

    //==================================================================================================//
    public static void processVotesFile(BufferedReader bufferVotes, Election election) throws Exception {

        String currentLine;
        String[] currentData;

        bufferVotes.readLine();//eliminates caption line
        while ((currentLine = bufferVotes.readLine()) != null) {

            currentData = dataFormatter(currentLine);

            if(candidateIsValid(currentData[17], election.getType(), currentData[19])){
                processCandidatesVotes(election, currentData);
            }
            //caso candidato inválido, mas de interesse pelos votos de legenda
            else if(election.getType() == Integer.parseInt(currentData[17])){
                processInvalidCandidatesVotes(election, currentData);
            }
        }
    }

                                    //========PRIVATE=======//
    //==================================================================================================//
    private static boolean candidateIsValid(String cdCargo, String cdDetalhesSituacaoCand, int type) {
        int cdC = Integer.parseInt(cdCargo);
        int cdD = Integer.parseInt(cdDetalhesSituacaoCand);
        if (cdC == type && (cdD == 2 || cdD == 16))
            return true; 
        return false;
    }

    //Overload===========================================================================//
    private static boolean candidateIsValid(String cdCargo, int type, String nrVotavel){
        int cdC = Integer.parseInt(cdCargo);
        int nrV = Integer.parseInt(nrVotavel);
        if(cdC == type && nrV != 95 && nrV != 96 && nrV != 97 && nrV != 98)
            return true;
        return false;
    }

    //==================================================================================================//
    private static PoliticalParty updateParties(Election election, String[] data) {
        int nrPartido = Integer.parseInt(data[27]);
        String sgPartido = data[28];
        int nrFederacao = Integer.parseInt(data[30]);

        if (election.getPartiesMap().containsKey(nrPartido)) {
            return election.getPartiesMap().get(nrPartido);
        }
        return election.addPartie(nrPartido, sgPartido, nrFederacao);
    }

    //==================================================================================================//
    private static String[] dataFormatter(String line) {
        String[] currentData = line.split(";");
        for (int i = 0; i < currentData.length; i++) {
            currentData[i] = currentData[i].replaceAll("\"", "");
        }
        return currentData;
    }

    //==================================================================================================//
    private static void updateCandidates(Election election, String[] data, PoliticalParty party) throws Exception {

        String[] date = data[42].split("/");
        int day = Integer.parseInt(date[0]);
        int month = Integer.parseInt(date[1]);
        int year = Integer.parseInt(date[2]);
        LocalDate dtNsc = LocalDate.of(year, month, day);

        boolean situation = isElectedCandidate(data[56]);
        int nrCandidato = Integer.parseInt(data[16]);
        String nmUrnaCandidato = data[18];
        int cdGenero = Integer.parseInt(data[45]);
        String nmTipoDestinoVotos = data[67];

        election.addCandidate(nrCandidato, nmUrnaCandidato, nmTipoDestinoVotos, dtNsc, situation, cdGenero, party);
    }
    //==================================================================================================//
    private static void updateInvalidCandidates(Election election, String[] data, PoliticalParty party) throws Exception {


        try {
            String[] date = data[42].split("/");
        int day = Integer.parseInt(date[0]);
        int month = Integer.parseInt(date[1]);
        int year = Integer.parseInt(date[2]);
        LocalDate dtNsc = LocalDate.of(year, month, day);

        boolean situation = isElectedCandidate(data[56]);
        int nrCandidato = Integer.parseInt(data[16]);
        String nmUrnaCandidato = data[18];
        int cdGenero = Integer.parseInt(data[45]);
        String nmTipoDestinoVotos = data[67];

        election.addInvalidCandidate(nrCandidato, nmUrnaCandidato, nmTipoDestinoVotos, dtNsc, situation, cdGenero, party);
        } catch (Exception e) {
            return;
        }

    }

    //==================================================================================================//
    private static boolean isElectedCandidate(String sit) {
        int situation = Integer.parseInt(sit);
        if (situation == 2 || situation == 3)
            return true;
        return false;
    }

    //==================================================================================================//
    private static void processCandidatesVotes(Election election, String[] data){
        int nrVotavel = Integer.parseInt(data[19]);
        int qtVotos = Integer.parseInt(data[21]);

        
        if(election.getCandidatesMap().containsKey(nrVotavel)){
            if(election.getCandidatesMap().get(nrVotavel).getNmTipoDestinoVotos().equals("Válido (legenda)")){
                election.getCandidatesMap().get(nrVotavel).getParty().setLegendVotes(qtVotos);
                election.setLegendVotes(qtVotos);
                return;
            }
            election.getCandidatesMap().get(nrVotavel).setQtVotos(qtVotos);
            election.setNominalVotes(qtVotos);
            return;
        }

        if(election.getPartiesMap().containsKey(nrVotavel)){
            election.getPartiesMap().get(nrVotavel).setLegendVotes(qtVotos);
            election.setLegendVotes(qtVotos);
        }
    }

    //==================================================================================================//
    private static void processInvalidCandidatesVotes(Election election, String[] data){
        int nrVotavel = Integer.parseInt(data[19]);
        int qtVotos = Integer.parseInt(data[21]);

        
        if(election.getInvalidCandidatesMap().containsKey(nrVotavel)){
            if(election.getInvalidCandidatesMap().get(nrVotavel).getNmTipoDestinoVotos().equals("Válido (legenda)")){
                election.getInvalidCandidatesMap().get(nrVotavel).getParty().setLegendVotes(qtVotos);
                election.setLegendVotes(qtVotos);
                return;
            }
        }      
    }
}
