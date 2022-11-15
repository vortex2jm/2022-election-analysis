import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Services {

    public static int validateArgs(String args) throws Exception {
        if (args.compareTo("--estadual") == 0)
            return 7;
        if (args.compareTo("--federal") == 0)
            return 6;
        throw new Exception("Argumento inválido");
    }

    public static BufferedReader createBuffer(String args) throws Exception {
        try {
            BufferedReader buffer = new BufferedReader(new FileReader(new File(args)));
            return buffer;
        } catch (Exception e) {
            throw e;
        }
    }

    public static Boolean candidateIsValid(String cdCargo, String cdDetalhesSituacaoCand, int type) {
        int cdC = Integer.parseInt(cdCargo);
        int cdD = Integer.parseInt(cdDetalhesSituacaoCand);
        if (cdC == type && (cdD == 2 || cdD == 16))
            return true;
        return false;
    }

    //Overload
    public static Boolean candidateIsValid(String cdCargo, int type, String nrVotavel){
        int cdC = Integer.parseInt(cdCargo);
        int nrV = Integer.parseInt(nrVotavel);
        if(cdC == type && nrV != 95 && nrV != 96 && nrV != 97 && nrV != 98)
            return true;
        return false;
    }

    public static PoliticalParty updateParties(Election election, String[] data) {

        int nrPartido = Integer.parseInt(data[27]);
        String sgPartido = data[28];
        int nrFederacao = Integer.parseInt(data[30]);

        if (election.parties.containsKey(nrPartido)) {
            return election.parties.get(nrPartido);
        }

        PoliticalParty p = new PoliticalParty(nrPartido, sgPartido, nrFederacao);
        election.parties.put(nrPartido, p);
        return p;
    }

    public static String[] dataFormatter(String line) {
        String[] currentData = line.split(";");
        for (int i = 0; i < currentData.length; i++) {
            currentData[i] = currentData[i].replaceAll("\"", "");
        }
        return currentData;
    }

    public static void updateCandidates(Election election, String[] data, PoliticalParty party) throws Exception {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date dtNsc = dateFormat.parse(data[42]);
        Boolean situation = isElectedCandidate(data[56]);
        int nrCandidato = Integer.parseInt(data[16]);
        String nmUrnaCandidato = data[18];
        int cdGenero = Integer.parseInt(data[45]);

        election.candidates.put(nrCandidato,
                new Candidate(nrCandidato, nmUrnaCandidato, dtNsc, situation, cdGenero, party));
        
    }

    public static Boolean isElectedCandidate(String sit) {
        int situation = Integer.parseInt(sit);
        if (situation == 2 || situation == 3)
            return true;
        return false;
    }

    public static void processCandidatesFile(BufferedReader bufferCandidates, Election election, int type)
            throws Exception {
        String currentLine;
        String[] currentData;
        PoliticalParty party;

        bufferCandidates.readLine(); // Eliminando linha de legenda
        while ((currentLine = bufferCandidates.readLine()) != null) {

            // Formatando os dados
            currentData = Services.dataFormatter(currentLine);

            // Atualizando lista de candidatos e partidos
            if (Services.candidateIsValid(currentData[13], currentData[24], type)) {
                party = Services.updateParties(election, currentData);
                Services.updateCandidates(election, currentData, party);
            }
        }
    }

    public static void processVotesFile(BufferedReader bufferVotes, Election election, int type) throws Exception {

        String currentLine;
        String[] currentData;

        bufferVotes.readLine();
        while ((currentLine = bufferVotes.readLine()) != null) {

            currentData = dataFormatter(currentLine);

            if(candidateIsValid(currentData[17], type, currentData[19])){
                processCandidatesVotes(election, currentData);
            }
        }

    }

    public static void processCandidatesVotes(Election election, String[] data){
        int nrVotavel = Integer.parseInt(data[19]);
        int qtVotos = Integer.parseInt(data[21]);

        if(election.candidates.containsKey(nrVotavel)){
            election.candidates.get(nrVotavel).setQtVotos(qtVotos);
            election.setNominalVotes(qtVotos);
            return;
        }
        if(election.parties.containsKey(nrVotavel)){
            election.parties.get(nrVotavel).setLegendVotes(qtVotos);
            election.setLegendVotes(qtVotos);
        }       
    }
}
