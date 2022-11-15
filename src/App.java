import java.io.BufferedReader;

public class App {
    public static void main(String[] args) throws Exception {

        int type = Services.validateArgs(args[0]);
        BufferedReader bufferCandidates = Services.createBuffer(args[1]);
        BufferedReader bufferVotes = Services.createBuffer(args[2]);

        Election election2022 = new Election(type);

        Services.processCandidatesFile(bufferCandidates, election2022);
        Services.processVotesFile(bufferVotes, election2022);
        Services.generateReports(election2022);
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
