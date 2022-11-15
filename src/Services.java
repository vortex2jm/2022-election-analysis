import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Services {
    
    public static int validateArgs(String args) throws Exception{
        if(args.compareTo("--estadual") == 0)
            return 7;
        if(args.compareTo("--federal") == 0)
            return 6;
        throw new Exception("Argumento inválido");
    }
    
    public static BufferedReader createBuffer(String args) throws Exception{
        try {
            BufferedReader buffer = new BufferedReader(new FileReader(new File (args)));
            return buffer;
        } catch (Exception e) {
            throw e;
        }
    }

    public static Boolean candidateIsValid(String cdCargo, String cdDetalhesSituacaoCand, int type){
        int cdC = Integer.parseInt(cdCargo);
        int cdD = Integer.parseInt(cdDetalhesSituacaoCand);
        if(cdC == type && (cdD == 2 || cdD == 16))
            return true;
        return false;
    }

    public static PoliticalParty updateParties(Election election, String[] data){

        for(int x=0; x<election.parties.size(); x++){
            if(election.parties.get(x).getNumber() == Integer.parseInt(data[27])){
                return election.parties.get(x);
            }   
        }

        PoliticalParty p = new PoliticalParty(Integer.parseInt(data[27]), data[28], Integer.parseInt(data[30]));
        election.parties.add(p);
        return p;
    }

    public static String[] dataFormatter(String line){
        String[] currentData = line.split(";");
        for(int i=0; i<currentData.length; i++){
            currentData[i] = currentData[i].replaceAll("\"","");
        }
        return currentData;
    }

    public static void updateCandidates(Election election, String[] data, PoliticalParty party) throws Exception{
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date dtNsc = dateFormat.parse(data[42]);
        Boolean situation = isElectedCandidate(data[56]);

        election.candidates.add(new Candidate(Integer.parseInt(data[16]), data[18],
        dtNsc, situation, Integer.parseInt(data[45]), party));
    }

    public static Boolean isElectedCandidate(String sit){
        int situation = Integer.parseInt(sit);
        if(situation == 2 || situation == 3)
            return true;
        return false;
    }

    public static void processCandidatesFile(BufferedReader bufferCandidates, Election election, int type) throws Exception{
        String currentLine;
        String[] currentData;
        PoliticalParty party;
        
        bufferCandidates.readLine();    //Eliminando linha de legenda
        while((currentLine = bufferCandidates.readLine()) != null){

            // Formatando os dados
            currentData = Services.dataFormatter(currentLine);
            
            // Atualizando lista de candidatos e partidos
            if(Services.candidateIsValid(currentData[13], currentData[24], type)){
                party = Services.updateParties(election, currentData);
                Services.updateCandidates(election, currentData, party);
            }
        }
    }
}
