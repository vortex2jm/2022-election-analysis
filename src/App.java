import java.io.BufferedReader;


public class App {
    public static void main(String[] args) throws Exception {

        int type = Services.validateArgs(args[0]);
        BufferedReader bufferCandidates = Services.createBuffer(args[1]);
        //BufferedReader bufferParties = Services.createBuffer(args[2]);

        Election election2022 = new Election();

        Services.processCandidatesFile(bufferCandidates, election2022, type);
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

//======Método para ver o indice da legenda
//String[] firstLine = bufferCandidates.readLine().split(";");
//int x=0;
//for(String s: firstLine){
//    System.out.printf("%d - ",x);
//    System.out.println(s);
//    x++;
//}
