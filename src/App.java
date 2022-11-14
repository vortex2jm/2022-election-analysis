import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class App {
    public static void main(String[] args) throws Exception {

        int type = Services.validateArgs(args[0]);
        BufferedReader bufferCandidates = Services.createBuffer(args[1]);
        Election election2022 = new Election();


        String[] firstLine = bufferCandidates.readLine().split(";");
        int x=0;
        for(String s: firstLine){
            System.out.printf("%d - ",x);
            System.out.println(s);
            x++;
        }

        // Referentes ao arquivo de candidatos
        //int cdCargoCand;  13
        //int cdDetalheSituacaoCand;  24
        int nrCandidato;
        String nmUrnaCandidato;
        //int nrPartido;  27
        //String sgPartido;  28
        //int nrFederacao;  30
        Date dtNascimento;
        int cdSitTotTurno;
        int cdGenero;

        // Referentes ao arquivo de partidos
        int cdCargoPart;
        int nrVotavel;
        int qtVotos;

        String currentLine;
        String[] currentData;
        PoliticalParty party;
        while((currentLine = bufferCandidates.readLine()) != null){

            // Retirando as aspas
            currentData = currentLine.split(";");
            for(int i=0; i<currentData.length; i++){
                currentData[i] = currentData[i].replaceAll("\"","");
            } 

            if(Services.candidateIsValid(currentData[13], currentData[24], type)){

                party = Services.updateParties(election2022, currentData);

                //Criar serviço para ler os dados do candidato e atualizar a lista da eleição

            }


        }
    }
}
