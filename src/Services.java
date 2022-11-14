import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

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
}
