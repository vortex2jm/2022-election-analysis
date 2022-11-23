package br.ufes.edu.jh.util.io;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import br.ufes.edu.jh.domain.Election;
import br.ufes.edu.jh.domain.PoliticalParty;

public class InputServices {
                                        //======PUBLIC======//
    //==================================================================================================//
    public static BufferedReader createBuffer(String args) throws Exception {
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
            
            if (InputServices.candidateIsValid(currentData[13], currentData[24], election.getType())) {
                InputServices.updateCandidates(election, currentData, party);
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
        }

    }

    //==================================================================================================//
    public static void checksParties(Election election) {

        List<PoliticalParty> parties = election.getParties();
        Map<Integer,PoliticalParty> current = election.getPartiesMap();

        for(PoliticalParty p: parties){
            if(p.getCandidatesList().size() == 0){
                current.remove(p.getNumber());
            }
        }
        election.setParties(current);
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

        election.addCandidate(nrCandidato, nmUrnaCandidato, dtNsc, situation, cdGenero, party);
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
            election.getCandidatesMap().get(nrVotavel).setQtVotos(qtVotos);
            election.setNominalVotes(qtVotos);
            return;
        }
        if(election.getPartiesMap().containsKey(nrVotavel)){
            election.getPartiesMap().get(nrVotavel).setLegendVotes(qtVotos);
            election.setLegendVotes(qtVotos);
        }       
    }
}
