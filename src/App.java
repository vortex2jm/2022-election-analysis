import java.io.BufferedReader;
import java.util.List;

public class App {
    public static void main(String[] args) throws Exception {

        int type = Services.validateArgs(args[0]);
        BufferedReader bufferCandidates = Services.createBuffer(args[1]);
        BufferedReader bufferVotes = Services.createBuffer(args[2]);

        Election election2022 = new Election();

        Services.processCandidatesFile(bufferCandidates, election2022, type);
        Services.processVotesFile(bufferVotes, election2022, type);


        // Testando as saídas
        System.out.println(election2022.getNominalVotes());
        System.out.println(election2022.getLegendVotes());
        System.out.println(election2022.electedAmount());

        List<Candidate> elctdCand = election2022.electedCandidates();
        
        int men =0;
        int women =0;
        int x=1;
        for(Candidate c: elctdCand){
            System.out.printf("%d - ",x);
            System.out.println(c);
            x++;
            if(c.getCdGenero() == 2)
                men++;
            if(c.getCdGenero() == 4)
                women++;
        }
        System.out.println(men);
        System.out.println(women);        
    }
}

//====== Referentes ao arquivo de candidatos
// cdCargoCand;  13
// cdDetalheSituacaoCand;  2

// nrPartido;  27
// sgPartido;  28
// nrFederacao;  30

// nrCandidato;  16
// nmUrnaCandidato; 18
// dtNascimento; 42
// cdSitTotTurno;  56
// cdGenero;  45

//====== Referentes ao arquivo de partidos
// cdCargoPart;
// nrVotavel;
// qtVotos;
