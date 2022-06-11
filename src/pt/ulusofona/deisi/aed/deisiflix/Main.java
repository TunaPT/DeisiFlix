package pt.ulusofona.deisi.aed.deisiflix;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.HashMap;

class QueryResult {
    String valor; // O parâmetro valor deve conter o valor calculado pela query
    long tempo; // O parâmetro tempo deve ter o tempo (em ms) que a query demorou a ser executada

    // Página 13 - Mais info

    QueryResult() {}
    QueryResult(String valor, long tempo) {
        this.valor = valor;
        this.tempo = tempo;
    }
}

public class Main {
    static boolean functionMoviesWithActors(int length, int k, String[] parts) {
        for (int i=2;i<length;i++) {
            if (filmesAno.get(parts[i]).contains(filmesAno.get(parts[0]).get(k)) == false) {
                return false;
            }
        }
        return true;
    }

    static HashMap<String,Integer> dicionario = new HashMap<String,Integer>(); //HASHMAP
    static HashMap<String,ArrayList> filmesAno = new HashMap<String,ArrayList>();



    static HashMap<Integer,String[]> pesquisaPorIDFilme = new HashMap<Integer,String[]>();

    //COUNT_ACTORS_3_YEARS
    static HashMap<String,ArrayList> actoresDiferentes = new HashMap<>(); //Key Ano de Filme | Devolve Arraylist com IDs dos filmes       //nomear função para insertDateGetIDFilme
    static HashMap<Integer,ArrayList> actoresDiferentes2 = new HashMap<>(); //Key ID Filme | Devolve Arraylist com IDs dos participantes no filme


    //TOP_MOVIES_WITH_GENDER_BIAS
    static HashMap<Integer,ArrayList> apenasActoresDeUmFilme = new HashMap<>(); // Key IDFilme | Devolve Arraylist com IDs dos actores no filme
    static HashMap<Integer,String> insertIDActorGetGenero = new HashMap<>(); //Key ID Actor | Devolve Genero Actor

    //GET_RECENT_TITLES_SAME_AVG_VOTES_ONE_SHARED_ACTOR
    static HashMap<Integer,ArrayList> actoresDeUmFilme = new HashMap<>(); // Key IDFilme | Devolve Arraylist com IDs dos participantes no filme
    static HashMap<Integer,String> tituloDeUmFilme = new HashMap<>(); // Key IDFilme | Devolve o titulo do filme
    static HashMap<Integer,String> dataDeUmFilme = new HashMap<>(); // Key IDFilme | Devolve data do filme
    static HashMap<Double,ArrayList> votacaoMediaFilme = new HashMap<>(); // Key Media Filme | Devolve Arraylist com IDs do filme com essa média

    //GET_TOP_N_YEARS_BEST_AVG_VOTES
    static HashSet<String> anoFilmes = new HashSet<>();
    static HashMap<Integer,Double> idFilmeMediaVotos = new HashMap<>(); // Key IDFilme | Devolve Float MediaFilmes
    static HashMap<String,Double> anoPontuacao = new HashMap<>();

    //GET_TOP_N_MOVIES_RATIO
    static HashMap<Integer,Integer> insertIDFilmeGetNumeroVotos = new HashMap<>();

    //GET_TOP_ACTOR_YEAR
    static HashMap<String,ArrayList> topActorYear = new HashMap<>();
    static HashMap<Integer,String> getPersonNameById = new HashMap<>();

    //TOP_6_DIRECTORS_WITHIN_FAMILY
    static HashMap<Integer,ArrayList> insertIDFilmeGetIDDirector = new HashMap<>(); // Key ID Filme | Devolve Arraylist com IDs dos realizadores/directors
    static HashMap<Integer,String> insertDirectorIDGetName = new HashMap<>(); // Key ID Realizador/Director | Devolve o seu nome


    static ArrayList<Filme> linhasVálidas = new ArrayList<Filme>();
    static ArrayList<String> linhasIgnoradas = new ArrayList<String>();
    static ArrayList<String> deisiPeople = new ArrayList<String>();
    static ArrayList<String> deisiGenres = new ArrayList<String>();
    static ArrayList<String> deisiMovieVotes = new ArrayList<String>();

    public static ArrayList<String> quickSort(ArrayList<String> list) {

        if (list.isEmpty()) {
            return list; // start with recursion base case
        }
        ArrayList<String> sorted;  // this shall be the sorted list to return, no needd to initialise
        ArrayList<String> smaller = new ArrayList<String>(); // Vehicles smaller than pivot
        ArrayList<String> greater = new ArrayList<String>(); // Vehicles greater than pivot
        String[] parts = list.get(0).split(":");
        int pivot = Integer.parseInt(parts[parts.length-1]);  // first Vehicle in list, used as pivot
        int i;
        int j;     // Variable used for Vehicles in the loop
        for (i=1;i<list.size();i++)
        {
            String[] parts2 = list.get(i).split(":");
            j=Integer.parseInt(parts2[parts2.length-1]);
            if (j>pivot) {  // make sure Vehicle has proper compareTo method
                smaller.add(list.get(i));
            } else {
                greater.add(list.get(i));
            }
        }
        smaller=quickSort(smaller);  // capitalise 's'
        greater=quickSort(greater);  // sort both halfs recursively
        smaller.add(list.get(0));          // add initial pivot to the end of the (now sorted) smaller Vehicles
        smaller.addAll(greater);     // add the (now sorted) greater Vehicles to the smaller ones (now smaller is essentially your sorted list)
        sorted = smaller;            // assign it to sorted; one could just as well do: return smaller

        return sorted;
    }

    // Partition do 'quickSortPorRacio'
    private static int paritionPorRacio(ArrayList<RacioFilme> movies, int left, int right) {
        RacioFilme pivot = movies.get(right);
        int leftIndex = left;
        int rightIndex = right - 1;

        while (leftIndex <= rightIndex) {
            if (movies.get(leftIndex).racio > pivot.racio && movies.get(rightIndex).racio < pivot.racio) {
                RacioFilme temp = movies.get(leftIndex);
                movies.set(leftIndex, movies.get(rightIndex));
                movies.set(rightIndex, temp);
            }

            if (movies.get(leftIndex).racio <= pivot.racio) {
                leftIndex++;
            }

            if (movies.get(rightIndex).racio >= pivot.racio) {
                rightIndex--;
            }
        }

        movies.set(right, movies.get(leftIndex));
        movies.set(leftIndex, pivot);
        return leftIndex;
    }

    // QuickSort - Ordena um ArrayList com objetos da classe 'RacioFilme' pelo racio (double)
    private static ArrayList<RacioFilme> quicksortPorRacio(
            ArrayList<RacioFilme> movies, int left, int right
    ) {
        if (left < right) {
            int pivotPos = paritionPorRacio(movies, left, right - 1);

            movies = quicksortPorRacio(movies, left, pivotPos);
            movies = quicksortPorRacio(movies, pivotPos + 1, right);
        }
        return movies;
    }

    // Method overloading do 'quickSortPorRacio'
    public static void quickSortPorRacio(ArrayList<RacioFilme> movies) {
        quicksortPorRacio(movies, 0, movies.size());
    }

    public static QueryResult perguntar(String pergunta) {
        QueryResult query = new QueryResult();
        if (pergunta.contains("COUNT_MOVIES_ACTOR")) { // Feito
            long initialTime = System.currentTimeMillis();
            String[] parts = pergunta.split("COUNT_MOVIES_ACTOR");
            String nomeAtor = parts[1].trim(); // Nome completo do autor
            if (dicionario.get(nomeAtor) == null) {
                long finalTime = System.currentTimeMillis();
                query = new QueryResult("0",finalTime-initialTime);
            } else {
                long finalTime = System.currentTimeMillis();
                query = new QueryResult(Integer.toString(dicionario.get(nomeAtor)),finalTime-initialTime);
            }
        } else if (pergunta.contains("GET_MOVIES_ACTOR_YEAR")) { // Feito
            long initialTime = System.currentTimeMillis();
            String[] parts = pergunta.split(" ");
            String part1 = parts[0].trim();
            String firstName = parts[1].trim();
            String secondName = parts[2].trim();
            String year = parts[3].trim();
            String outputFinal = "";
            if (filmesAno.get(firstName + " " + secondName) != null) {
                ArrayList<Integer> arrayListIDsFilmes = filmesAno.get(firstName + " " + secondName); // Variável com arraylist dos ids dos filmes
                ArrayList<InfoMoviesActorYear> TitulosAndDatas = new ArrayList<>();
                for (int i=0;i<arrayListIDsFilmes.size();i++) {
                    String[] dateFormat = pesquisaPorIDFilme.get(arrayListIDsFilmes.get(i))[1].split("-");
                    if (year.equals(dateFormat[2])) {
                        TitulosAndDatas.add(new InfoMoviesActorYear(pesquisaPorIDFilme.get(arrayListIDsFilmes.get(i))[0],pesquisaPorIDFilme.get(arrayListIDsFilmes.get(i))[1]));
                    }
                }
                ArrayList<String> arrayListDatas = new ArrayList<>();
                HashMap<String,String> dadosFilmes = new HashMap<>();
                for (int i=0;i<TitulosAndDatas.size();i++) {
                    String[] dateFormat = TitulosAndDatas.get(i).data.split("-");
                    String dateFormatFinal = String.join("-", dateFormat[2], dateFormat[1], dateFormat[0]);
                    dadosFilmes.put(dateFormatFinal,TitulosAndDatas.get(i).tituloFilme);
                    arrayListDatas.add(dateFormatFinal);
                    //System.out.println(TitulosAndDatas.get(i).tituloFilme + " " + dateFormatFinal); //Data sem estar ordenada
                }
                Collections.sort(arrayListDatas,Collections.reverseOrder()); //ordena por data decrescente
                for (int i=0;i<TitulosAndDatas.size();i++) {
                    if (i==TitulosAndDatas.size()-1) {
                        outputFinal = outputFinal + dadosFilmes.get(arrayListDatas.get(i)) + " (" + arrayListDatas.get(i) + ")";
                    } else {
                        outputFinal = outputFinal + dadosFilmes.get(arrayListDatas.get(i)) + " (" + arrayListDatas.get(i) + ")\n";
                    }
                }
            }
            long finalTime = System.currentTimeMillis();
            query = new QueryResult(outputFinal,finalTime-initialTime);
        } else if (pergunta.contains("COUNT_MOVIES_WITH_ACTORS")) { // Feito
            long initialTime = System.currentTimeMillis();
            String[] partes = pergunta.split("COUNT_MOVIES_WITH_ACTORS");
            String[] parts = partes[1].trim().split(";");
            int count = 0;
            if (parts.length > 1) {
                for (int k=0; k<filmesAno.get(parts[0]).size(); k++) {
                    if (filmesAno.get(parts[1]).contains(filmesAno.get(parts[0]).get(k))) { //Se a linha seguinte tiver um elemento da linha anterior, avança
                        if (functionMoviesWithActors(parts.length, k, parts) == true) {
                            count++;
                        }
                    }
                }
            }
            long finalTime = System.currentTimeMillis();
            query = new QueryResult(Integer.toString(count),finalTime-initialTime);
        } else if (pergunta.contains("COUNT_ACTORS_3_YEARS")) { // Feito

            /*static HashMap<String,ArrayList> actoresDiferentes = new HashMap<>();  //Key Ano de Filme | Devolve Arraylist com IDs dos filmes
            static HashMap<Integer,ArrayList> actoresDiferentes2 = new HashMap<>(); //Key ID Filme | Devolve Arraylist com IDs dos participantes no filme*/

            long initialTime = System.currentTimeMillis();
            String[] parts = pergunta.split(" ");
            HashMap<String,ArrayList> insertAnoGetIDsActoresParticipantesEmFilmes = new HashMap<>(); // Key Ano Filme | Devolve Arraylist com IDs dos actores que participaram em filmes desse ano
            int count = 0;
            for (int i=1;i<parts.length;i++) { // Para o cumprimento 4
                ArrayList<Integer> idsFilmes = actoresDiferentes.get(parts[i]); // Devolve um arraylist com os IDs dos filmes de um certo ano
                ArrayList<Integer> idsActores = new ArrayList<>();
                for (int k=0;k<idsFilmes.size();k++) { // Vou percorrer o arraylist com os IDs dos filmes de um certo ano
                    int idFilme = idsFilmes.get(k); // Obtenho o id do filme
                    ArrayList<Integer> idsParticipantesNoFilme = actoresDiferentes2.get(idFilme); // Obtenho um arraylist com os IDs dos participantes no filme
                    if (idsParticipantesNoFilme != null) { // Verifico se o arraylist com os IDs dos participantes no filme existe, ou seja, existem participantes
                        for (int l=0;l<idsParticipantesNoFilme.size();l++) { // Vou percorrer o arraylist com os IDs dos participantes num filme
                            int idActorParticipante = idsParticipantesNoFilme.get(l); // Obtenho o ID do actor/participante no filme
                            if (idsActores.contains(idActorParticipante) == false) { // Se já existir o ID do actor nesse ano, não contar mais
                                idsActores.add(idActorParticipante);
                            }
                        }
                    }
                }
                insertAnoGetIDsActoresParticipantesEmFilmes.put(parts[i],idsActores);
            }
            for (int i=0;i<insertAnoGetIDsActoresParticipantesEmFilmes.get(parts[1]).size();i++) {
                int idActor = (int) insertAnoGetIDsActoresParticipantesEmFilmes.get(parts[1]).get(i);
                if (insertAnoGetIDsActoresParticipantesEmFilmes.get(parts[2]).contains(idActor)) {
                    if (insertAnoGetIDsActoresParticipantesEmFilmes.get(parts[3]).contains(idActor)) {
                        count++;
                    }
                }
            }
            long finalTime = System.currentTimeMillis();
            query = new QueryResult(Integer.toString(count),finalTime-initialTime);
        } else if (pergunta.contains("TOP_MOVIES_WITH_GENDER_BIAS")) {  // Feito
            long initialTime = System.currentTimeMillis();
            String[] partes = pergunta.split(" ");
            String nFilmes = partes[1].trim();
            String ano = partes[2].trim();
            String output = "";
            /*
            //static HashMap<String,ArrayList> actoresDiferentes = new HashMap<>(); //Key Ano de Filme | Devolve Arraylist com IDs dos filmes
            //static HashMap<Integer,ArrayList> actoresDeUmFilme = new HashMap<>(); // Key IDFilme | Devolve Arraylist com IDs dos participantes no filme
            //static HashMap<Integer,String> insertIDActorGetGenero = new HashMap<>(); //Key ID Actor | Devolve Genero Actor
            static HashMap<Integer,ArrayList> apenasActoresDeUmFilme = new HashMap<>(); // Key IDFilme | Devolve Arraylist com IDs dos actores no filme
             */
            ArrayList<Integer> listIDsFilmesDoAnoInserido = actoresDiferentes.get(ano); // Key Ano | Devolve arraylist com IDs dos filmes desse ano

            HashMap<Integer,ArrayList> insertIDFilmeGetListGeneros = new HashMap<>(); //Key IDFilme | Recebe arraylist com os generos dos actores desse filme
            ArrayList<String> outputFinal = new ArrayList<>();
                for (int k=0;k<listIDsFilmesDoAnoInserido.size();k++) { // Estou a ver o tamanho do Arraylist com IDs dos Filmes para depois percorre-lo
                    int idFilme = listIDsFilmesDoAnoInserido.get(k); // ID de um filme numa certa posição
                    ArrayList<Integer> listIDsParticipantesFilme = apenasActoresDeUmFilme.get(idFilme); // listIDsParticipantesFilme = Arraylist com IDs dos participantes do filme
                    if (listIDsParticipantesFilme != null) { // Estou a verificar se o filme é válido (tem actores)
                        if (listIDsParticipantesFilme.size() > 10) { // Estou a verificar se existem mais que 10 participantes no filme
                            int countF = 0;
                            int countM = 0;
                            for (int l=0;l<listIDsParticipantesFilme.size(); l++) { // Vou percorrer o Arraylist com os IDs dos participantes do filme
                                int idActor = listIDsParticipantesFilme.get(l); // Recebo o ID de cada participante do Filme
                                String generoActor = insertIDActorGetGenero.get(idActor); // Recebo o genero do Ator/Participante através do seu ID pessoal
                                if (generoActor.equals("F")) {
                                    countF++;
                                } else if (generoActor.equals("M")) {
                                    countM++;
                                }
                                if (insertIDFilmeGetListGeneros.get(idFilme) == null) {
                                    ArrayList<String> arrayList = new ArrayList<>();
                                    arrayList.add(generoActor);
                                    insertIDFilmeGetListGeneros.put(idFilme, arrayList);
                                } else {
                                    ArrayList<String> arrayList = insertIDFilmeGetListGeneros.get(idFilme);
                                    arrayList.add(generoActor);
                                    insertIDFilmeGetListGeneros.put(idFilme, arrayList);
                                }
                            }
                            if (insertIDFilmeGetListGeneros.get(idFilme) != null)  {
                                if (countF>countM) {
                                    int percentagem = (int) Math.round(countF*100.0/(countF+countM));
                                    outputFinal.add(tituloDeUmFilme.get(idFilme) + ":F:" + percentagem);
                                } else {
                                    int percentagem = (int) Math.round(countM*100.0/(countF+countM));
                                    outputFinal.add(tituloDeUmFilme.get(idFilme) + ":M:" + percentagem);
                                }
                            }
                        }
                    }
                }
            for (int i=0;i<Integer.parseInt(nFilmes);i++) {
                ArrayList<String> last = quickSort(outputFinal);
                output = output + last.get(i) + "\n";
            }
            long finalTime = System.currentTimeMillis();
            query = new QueryResult(output,finalTime-initialTime);
        } else if (pergunta.contains("GET_RECENT_TITLES_SAME_AVG_VOTES_ONE_SHARED_ACTOR")) { // FEITO
            long initialTime = System.currentTimeMillis();
            String[] partes = pergunta.split("GET_RECENT_TITLES_SAME_AVG_VOTES_ONE_SHARED_ACTOR");
            String idFilme = partes[1].trim();
            String output = "";
            if (idFilmeMediaVotos.get(Integer.parseInt(idFilme)) != null) {
                double votacaoMediaFilmeX = idFilmeMediaVotos.get(Integer.parseInt(idFilme));
                ArrayList<Integer> idActoresFilmeX = actoresDeUmFilme.get(Integer.parseInt(idFilme)); //Devolve arraylist com IDs dos actores presentes no filme
                ArrayList<Integer> IDFilmesComMesmaMediaDoFilmeX = votacaoMediaFilme.get(votacaoMediaFilmeX); //Devolve arraylist com IDs dos filmes que têm a mesma média
                for (int i = 0; i < IDFilmesComMesmaMediaDoFilmeX.size(); i++) {
                    if (dataDeUmFilme.get(IDFilmesComMesmaMediaDoFilmeX.get(i)).compareTo(dataDeUmFilme.get(Integer.parseInt(idFilme))) > 0) {
                        if (actoresDeUmFilme.get(IDFilmesComMesmaMediaDoFilmeX.get(i)) != null) {
                            for (int k = 0; k < actoresDeUmFilme.get(IDFilmesComMesmaMediaDoFilmeX.get(i)).size(); k++) {
                                if (idActoresFilmeX.contains(actoresDeUmFilme.get(IDFilmesComMesmaMediaDoFilmeX.get(i)).get(k))) {
                                    if (output.equals("")) {
                                        output = tituloDeUmFilme.get(IDFilmesComMesmaMediaDoFilmeX.get(i));
                                    } else {
                                        output = output + "||" + tituloDeUmFilme.get(IDFilmesComMesmaMediaDoFilmeX.get(i));
                                    }
                                }
                            }
                        }
                    }
                }
            }
            long finalTime = System.currentTimeMillis();
            query = new QueryResult(output,finalTime-initialTime);
        } else if (pergunta.contains("GET_TOP_N_YEARS_BEST_AVG_VOTES")) {  // FALTA VER ONDE ESTÁ O ERRO DESTA + info Discord
            long initialTime = System.currentTimeMillis();
            String[] partes = pergunta.split("GET_TOP_N_YEARS_BEST_AVG_VOTES");
            String topValue = partes[1].trim();

            /*
            static HashSet<String> anoFilmes = new HashSet<>(); // Inseria todos os anos existentes aqui
            static HashMap<String,ArrayList> mediaVotosFilmes = new HashMap<>(); // Key Ano Filme | Devolve Arraylist com os filmes desse ano
            static HashMap<Integer,Float> idFilmeMediaVotos = new HashMap<>(); // Key IDFilme | Devolve Float MediaFilmes
            static HashMap<String,Float> anoPontuacao = new HashMap<>(); // Key Ano | Devolve a Media do Ano
             */

            String[] anoFilmesArray = anoFilmes.toArray(new String[anoFilmes.size()]); // Transformo o HashSet com todos os anos existentes num array para percorrer
            for (int i=0; i<anoFilmesArray.length;i++) { // Percorro o array com os vários anos de filmes
                String ano = anoFilmesArray[i]; // Obtenho um ano
                ArrayList<Integer> idsFilmes = actoresDiferentes.get(ano); // Obtenho um arraylist com os IDs dos filmes desse ano
                double somaVotosAno = 0.0f;
                for (int k=0;k<idsFilmes.size();k++) { // Percorro o arraylist com os IDs dos filmes para um certo ano
                    int idFilme = idsFilmes.get(k); // Obtenho o ID do filme de um determinado ano
                    double mediaVotos = idFilmeMediaVotos.get(idFilme); // Obtenho a média de votos do filme de um certo ano com base no id do filme
                    somaVotosAno = somaVotosAno + mediaVotos; // Faço soma total dos votos dos filmes todos de um certo ano
                }
                double mediaAno = somaVotosAno/idsFilmes.size(); // Obtenho a media de votos de um certo ano
                //System.out.println("ANO " + ano + " - Media: " + mediaAno);
                anoPontuacao.put(anoFilmesArray[i], mediaAno); // Guardo os dados num HashMap com Key Ano e devolve a media do ano
            }

            String stringQuery = "";
            String ano = anoFilmesArray[0];
            for (int i=0; i<Integer.parseInt(topValue);i++) {
                double maior = anoPontuacao.get(anoFilmesArray[0]);
                for (int k=1; k<anoFilmesArray.length;k++) {
                    if (anoPontuacao.get(anoFilmesArray[k]) != null) {
                        if (anoPontuacao.get(anoFilmesArray[k]) > maior) {
                            maior = anoPontuacao.get(anoFilmesArray[k]);
                            ano = anoFilmesArray[k];
                        }
                    }
                }
                if (i+1 != Integer.parseInt(topValue)) {
                    stringQuery = stringQuery + ano + ":" + Math.round(anoPontuacao.get(ano) * 100)/100.0 + "\n";
                } else {
                    stringQuery = stringQuery + ano + ":" + Math.round(anoPontuacao.get(ano) * 100)/100.00 + "\n";
                }
                anoPontuacao.remove(ano);
            }
            long finalTime = System.currentTimeMillis();
            query = new QueryResult(stringQuery,finalTime-initialTime);
        } else if (pergunta.contains("DISTANCE_BETWEEN_ACTORS")) {
            long initialTime = System.currentTimeMillis();
            long finalTime = System.currentTimeMillis();
            query = new QueryResult("s",finalTime-initialTime);
        } else if (pergunta.contains("GET_TOP_N_MOVIES_RATIO")) { //Descrobrir o ERRO. Print no discord de como estava antes

            long initialTime = System.currentTimeMillis();
            String[] partes = pergunta.split(" ");
            String topNFilmes = partes[1].trim();
            String ano = partes[2].trim();
            String output = "";

            ArrayList<Integer> idFilmes = actoresDiferentes.get(ano); // Insiro o Ano e recebo um arraylist com os IDs dos filmes desse ano
            HashMap<Integer,Double> insertIDFilmeGetRacio = new HashMap<>(); // Key IDFilme | Devolve o seu racio
            ArrayList<RacioFilme> dados = new ArrayList<>();

            for (int i=0;i<idFilmes.size();i++) { // Percorro o arraylist com os IDs dos filmes do ano escolhido
                int idFilme = idFilmes.get(i); // Obtenho o ID do filme
                ArrayList<Integer> listActoresFilme = apenasActoresDeUmFilme.get(idFilme); // Devolve Arraylist com IDs dos actores no filme
                if (listActoresFilme != null) { // Verifico se o filme tem actores, se é válido
                    int numeroActoresFilme = listActoresFilme.size(); // Obtenho o numero de actores do filme
                    double mediaFilme = idFilmeMediaVotos.get(idFilme); // Obtenho a media do filme
                    double racioFilme = mediaFilme / numeroActoresFilme; // Calculo o seu racio
                    String tituloFilme = tituloDeUmFilme.get(idFilme);
                    insertIDFilmeGetRacio.put(idFilme, racioFilme); // Guardo no IDFilme o seu racio
                    dados.add(new RacioFilme(tituloFilme, racioFilme));
                }
            }

            quickSortPorRacio(dados);
            boolean primeiroOutput=true;

            // Como o ArrayList 'dados' está ordenado por ordem crescente, começamos a dar output ao valores pelo fim
            for (int i=0, posicao = dados.size() - 1;i<Integer.parseInt(topNFilmes) && i < dados.size();i++, posicao--) {
                if (primeiroOutput == false) {
                    output = output + "\n";
                }
                primeiroOutput = false;
                output = output + dados.get(posicao).titulo + ":" + dados.get(posicao).racio;
            }
            if (output.equals("")) {
                output = "zerop";
            }
            long finalTime = System.currentTimeMillis();
            query = new QueryResult(output,finalTime-initialTime);
        } else if (pergunta.contains("TOP_6_DIRECTORS_WITHIN_FAMILY")) {
            long initialTime = System.currentTimeMillis();
            long finalTime = System.currentTimeMillis();
            query = new QueryResult("s",finalTime-initialTime);
        } else if (pergunta.contains("GET_TOP_ACTOR_YEAR")) { // FEITO
            long initialTime = System.currentTimeMillis();
            String[] partes = pergunta.split("GET_TOP_ACTOR_YEAR");
            String ano = partes[1].trim();
            ArrayList<Integer> arrayList = topActorYear.get(ano); //Devolve o ID do Filme. Com base no ID do Filme, tentar obter o nome da pessoa ou ID da pessoa
            ArrayList<Integer> arrayListIDsPessoa = new ArrayList<>();
            for (int i=0;i<arrayList.size();i++) {
                if (actoresDiferentes2.get(arrayList.get(i)) != null) {
                    for (int k = 0; k < actoresDiferentes2.get(arrayList.get(i)).size(); k++) {
                        arrayListIDsPessoa.add((Integer) actoresDiferentes2.get(arrayList.get(i)).get(k));
                    }
                }
                actoresDiferentes2.get(arrayList.get(i)); // Arraylist
            }
            int maior = Collections.frequency(arrayListIDsPessoa,arrayListIDsPessoa.get(0));
            int id = arrayListIDsPessoa.get(0);
            for (int i=1;i<arrayListIDsPessoa.size();i++) {
                if (Collections.frequency(arrayListIDsPessoa,arrayListIDsPessoa.get(i)) > maior) {
                    id = arrayListIDsPessoa.get(i);
                    maior = Collections.frequency(arrayListIDsPessoa,arrayListIDsPessoa.get(i));
                }
            }
            String nome = getPersonNameById.get(id);
            long finalTime = System.currentTimeMillis();
            query = new QueryResult(nome + ";" + Integer.toString(maior),finalTime-initialTime);
        } else if (pergunta.contains("INSERT_ACTOR")) {
            long initialTime = System.currentTimeMillis();
            String[] partes = pergunta.split("INSERT_ACTOR");
            String[] parts = partes[1].trim().split(";");
            String IDActor = parts[0].trim();
            String NomeActor = parts[1].trim();
            String GeneroActor = parts[2].trim();
            String IDFilme = partes[3].trim();
            new DeisiPeople("ACTOR", Integer.parseInt(IDActor), NomeActor, GeneroActor, Integer.parseInt(IDFilme));
            long finalTime = System.currentTimeMillis();
            query = new QueryResult("s",initialTime-finalTime);
        } else if (pergunta.contains("REMOVE_ACTOR")) {
            long initialTime = System.currentTimeMillis();
            String[] partes = pergunta.split(" ");
            String IDActor = partes[1];
            long finalTime = System.currentTimeMillis();
            query = new QueryResult("s",finalTime-initialTime);
        } else if (pergunta.contains("GET_DUPLICATE_LINES_YEAR")) {
            long initialTime = System.currentTimeMillis();
            long finalTime = System.currentTimeMillis();
            query = new QueryResult("s",initialTime-finalTime);
        }
        return query;
    }
    public static String getVideoURL() { return null; }
    public static String getCreativeQuery() { return null; }

    static boolean readFiles = false;

    static void lerFicheiros() throws IOException {
        if (readFiles == false) {
            MoviesValid moviesValid = readerDeisiMovies();
            linhasVálidas = moviesValid.listaFilmes;
            linhasIgnoradas = moviesValid.linhasIgnoradas;
            deisiPeople = readerDeisiPeople();
            deisiGenres = readerDeisiGenres();
            deisiMovieVotes = readerDeisiMovieVotes(linhasVálidas);
            readFiles = true;
        }
    }
    static ArrayList<Filme> getFilmes() { return linhasVálidas; }

    static ArrayList<String> getLinhasIgnoradas(String fileName) {
        if (fileName == "deisi_people.txt") {
            return deisiPeople;
        } else if (fileName == "deisi_genres.txt") {
            return deisiGenres;
        } else if (fileName == "deisi_movie_votes.txt") {
            return deisiMovieVotes;
        } else if (fileName == "deisi_movies.txt") {
            return linhasIgnoradas;
        } else {
            return null;
        }
    }

    public static void main(String[] args) throws IOException {
        lerFicheiros();
        Scanner in = new Scanner(System.in);
        String line = in.nextLine();
        while (line != null && !line.equals("QUIT")) {
            QueryResult resultado = perguntar(line);
            System.out.println(resultado.valor);
            System.out.println("(demorou " + resultado.tempo + " ms)");
            line = in.nextLine();
        }
    }

    public static ArrayList<String> readerDeisiPeople() throws IOException {
        int countNomePessoa = 1;
        FileReader fr = new FileReader("deisi_people.txt");
        BufferedReader reader = new BufferedReader(fr);
        String linha = null;
        ArrayList<String> linhasIgnoradas = new ArrayList<String>();
        while((linha = reader.readLine()) != null) {
            String dados[] = linha.split(",");
            if (dados.length == 5) {
                String TipoPessoa = dados[0].trim();
                int ID = Integer.parseInt(dados[1].trim());
                String NomePessoa = dados[2].trim();
                String Género = dados[3].trim();
                int IDFilme = Integer.parseInt(dados[4].trim());
                /*System.out.println("TipoPessoa: " + TipoPessoa);
                System.out.println("ID: " + ID);
                System.out.println("Nome: " + NomePessoa);
                System.out.println("Género: " + Género);
                System.out.println("IDFilme: " + IDFilme);*/
                DeisiPeople DeisiPeople = new DeisiPeople(TipoPessoa, ID, NomePessoa, Género, IDFilme);
                if (TipoPessoa.equals("ACTOR")) {
                    if (dicionario.get(NomePessoa) != null ) {
                        countNomePessoa = dicionario.get(NomePessoa)+1;
                    }
                    dicionario.put(NomePessoa,countNomePessoa);
                    countNomePessoa = 1;
                }
                if (filmesAno.get(NomePessoa) == null) {
                    ArrayList<Integer> IDFilmesPessoa = new ArrayList<Integer>();
                    IDFilmesPessoa.add(IDFilme);
                    filmesAno.put(NomePessoa,IDFilmesPessoa);
                } else {
                    ArrayList<Integer> IDFilmesPessoa = filmesAno.get(NomePessoa);
                    IDFilmesPessoa.add(IDFilme);
                    filmesAno.put(NomePessoa,IDFilmesPessoa);
                }


                // EM OBRAS
                if (TipoPessoa.equals("ACTOR")) {
                    if (actoresDiferentes2.get(IDFilme) == null) {
                        ArrayList<Integer> IDsActores = new ArrayList<>();
                        IDsActores.add(ID);
                        actoresDiferentes2.put(IDFilme, IDsActores);
                    } else {
                        ArrayList<Integer> IDsActores = actoresDiferentes2.get(IDFilme);
                        IDsActores.add(ID);
                        actoresDiferentes2.put(IDFilme, IDsActores);
                    }
                    //TOP_MOVIES_WITH_GENDER_BIAS
                    if (apenasActoresDeUmFilme.get(IDFilme) == null) {
                        ArrayList<Integer> arrayList = new ArrayList<>();
                        arrayList.add(ID);
                        apenasActoresDeUmFilme.put(IDFilme,arrayList);
                    } else {
                        ArrayList<Integer> arrayList = apenasActoresDeUmFilme.get(IDFilme);
                        arrayList.add(ID);
                        apenasActoresDeUmFilme.put(IDFilme,arrayList);
                    }
                    insertIDActorGetGenero.put(ID, Género);
                }

                //GET_RECENT_TITLES_SAME_AVG_VOTES_ONE_SHARED_ACTOR
                if (actoresDeUmFilme.get(IDFilme) == null) {
                    ArrayList<Integer> arrayList = new ArrayList<>();
                    arrayList.add(ID);
                    actoresDeUmFilme.put(IDFilme,arrayList);
                } else {
                    ArrayList<Integer> arrayList = actoresDeUmFilme.get(IDFilme);
                    arrayList.add(ID);
                    actoresDeUmFilme.put(IDFilme,arrayList);
                }

                //GET_TOP_ACTOR_YEAR
                getPersonNameById.put(ID,NomePessoa);

                //TOP_6_DIRECTORS_WITHIN_FAMILY
                if (TipoPessoa.equals("DIRECTOR")) {
                    if (insertIDFilmeGetIDDirector.get(IDFilme) == null) {
                        ArrayList<Integer> arrayList = new ArrayList<>();
                        arrayList.add(ID);
                        insertIDFilmeGetIDDirector.put(IDFilme,arrayList);
                    } else {
                        ArrayList<Integer> arrayList = insertIDFilmeGetIDDirector.get(IDFilme);
                        arrayList.add(ID);
                        insertIDFilmeGetIDDirector.put(IDFilme,arrayList);
                    }
                    insertDirectorIDGetName.put(ID,NomePessoa);
                }

            } else {
                linhasIgnoradas.add(linha);
            }
        }
        reader.close();
        return linhasIgnoradas;
    }

    public static ArrayList<String> readerDeisiGenres() throws IOException {
        FileReader fr = new FileReader("deisi_genres.txt");
        BufferedReader reader = new BufferedReader(fr);
        String linha = null;
        ArrayList<String> linhasIgnoradas = new ArrayList<String>();
        while((linha = reader.readLine()) != null) {
            String dados[] = linha.split(",");
            if (dados.length == 2) {
                String Género = dados[0].trim();
                int ID = Integer.parseInt(dados[1].trim());
                /*System.out.println("Nome Género: " + Género);
                System.out.println("ID Filme: " + ID);*/
                Genres Genres = new Genres(Género, ID);
            } else {
                linhasIgnoradas.add(linha);
            }
        }
        reader.close();
        return linhasIgnoradas;
    }

    public static ArrayList<String> readerDeisiMovieVotes(ArrayList<Filme> Filme) throws IOException {
        FileReader fr = new FileReader("deisi_movie_votes.txt");
        BufferedReader reader = new BufferedReader(fr);
        String linha = null;
        ArrayList<String> linhasIgnoradas = new ArrayList<String>();
        while((linha = reader.readLine()) != null) {
            String dados[] = linha.split(",");
            if (dados.length == 3) {
                int ID = Integer.parseInt(dados[0].trim());
                double MédiaVotos = Double.parseDouble(dados[1].trim());
                int NrVotos = Integer.parseInt(dados[2].trim());
                /*System.out.println("ID Filme: " + ID);
                System.out.println("Média Votos: " + MédiaVotos);
                System.out.println("Nr Votos: " + NrVotos);*/
                /*for (int i = 0; i < Filme.size(); i++) {
                    if (Filme.get(i).id == ID) {
                        Filme.get(i).mediaDeVotos = MédiaVotos;
                        Filme.get(i).nrDeVotos = NrVotos;
                    }
                }*/
                MovieVotes MovieVotes = new MovieVotes(ID, MédiaVotos, NrVotos);
                idFilmeMediaVotos.put(ID,MédiaVotos);
                insertIDFilmeGetNumeroVotos.put(ID,NrVotos);

                //GET_RECENT_TITLES_SAME_AVG_VOTES_ONE_SHARED_ACTOR
                if (votacaoMediaFilme.get(MédiaVotos) == null) {
                    votacaoMediaFilme.put(MédiaVotos, new ArrayList(ID));
                } else {
                    ArrayList<Integer> arrayList = votacaoMediaFilme.get(MédiaVotos);
                    arrayList.add(ID);
                    votacaoMediaFilme.put(MédiaVotos,arrayList);
                }

            } else {
                linhasIgnoradas.add(linha);
            }
        }
        reader.close();
        return linhasIgnoradas;
    }

    public static MoviesValid readerDeisiMovies() throws IOException {
        FileReader fr = new FileReader("deisi_movies.txt");
        BufferedReader reader = new BufferedReader(fr);
        String linha = null;
        ArrayList<String> linhasIgnoradas = new ArrayList<String>();
        ArrayList<Filme> linhasVálidas = new ArrayList<Filme>();
        while((linha = reader.readLine()) != null) {
            String dados[] = linha.split(",");
            if (dados.length == 5) {
                int ID = Integer.parseInt(dados[0].trim());
                String Título = dados[1].trim();
                float Duração = Float.parseFloat(dados[2].trim());
                int Orçamento = Integer.parseInt(dados[3].trim());
                String Data = dados[4].trim();
                /*System.out.println("ID Filme: " + ID);
                System.out.println("Título: " + Título);
                System.out.println("Duração: " + Duração);
                System.out.println("Orçamento: " + Orçamento);
                System.out.println("Data: " + Data);*/
                Filme filme = new Filme();
                filme.id = ID;
                filme.titulo = Título;
                filme.orcamento = Orçamento;
                filme.dataDeLancamento = Data;
                linhasVálidas.add(filme);
                Movies Movies = new Movies(ID, Título, Duração, Orçamento, Data);
                String[] arrayInfoFilme = {Título,Data};
                pesquisaPorIDFilme.put(ID,arrayInfoFilme);

                // EM OBRAS
                String[] dateFormat = Data.split("-");
                if (actoresDiferentes.get(dateFormat[2]) == null) {
                    ArrayList<Integer> IDsFilmes = new ArrayList<>();
                    IDsFilmes.add(ID);
                    actoresDiferentes.put(dateFormat[2],IDsFilmes);
                } else {
                    ArrayList<Integer> IDsFilmes = actoresDiferentes.get(dateFormat[2]);
                    IDsFilmes.add(ID);
                    actoresDiferentes.put(dateFormat[2],IDsFilmes);
                }

                if (topActorYear.get(dateFormat[2]) == null) {
                    topActorYear.put(dateFormat[2], new ArrayList<Integer>(ID));
                } else {
                    ArrayList<Integer> arrayList = topActorYear.get(dateFormat[2]); //Adiciona ID do Filme
                    arrayList.add(ID);
                    topActorYear.put(dateFormat[2],arrayList);
                }

                //GET_TOP_N_YEARS_BEST_AVG_VOTES
                anoFilmes.add(dateFormat[2]);

                //GET_RECENT_TITLES_SAME_AVG_VOTES_ONE_SHARED_ACTOR
                tituloDeUmFilme.put(ID,Título);
                String dateFormatFinal = String.join("-", dateFormat[2], dateFormat[1], dateFormat[0]);
                dataDeUmFilme.put(ID,dateFormatFinal);

            } else {
                linhasIgnoradas.add(linha);
            }
        }
        reader.close();
        return new MoviesValid(linhasVálidas,linhasIgnoradas);
    }
}

class InfoMoviesActorYear {
    String tituloFilme;
    String data;

    InfoMoviesActorYear(String tituloFilme, String data) {
        this.tituloFilme = tituloFilme;
        this.data = data;
    }
    public String toString() {
        return tituloFilme + " | " + data + "\n";
    }
}

class MoviesValid {
    ArrayList<Filme> listaFilmes;
    ArrayList<String> linhasIgnoradas;

    MoviesValid() {}

    MoviesValid(ArrayList<Filme> listaFilmes, ArrayList<String> linhasIgnoradas) {
        this.listaFilmes = listaFilmes;
        this.linhasIgnoradas = linhasIgnoradas;
    }
}

class DeisiPeople {
    String tipoPessoa;
    int id;
    String nomePessoa;
    String genero;
    int idFilme;

    DeisiPeople(String tipoPessoa, int id, String nomePessoa, String genero, int idFilme) {
        this.tipoPessoa = tipoPessoa;
        this.id = id;
        this.nomePessoa = nomePessoa;
        this.genero = genero;
        this.idFilme = idFilme;
    }

    public String toString() {
        return tipoPessoa + " | " + id + " | " + nomePessoa + " | " + genero + " | " + idFilme + "\n";
    }
}

class Movies {
    int id;
    String titulo;
    float duracao;
    int orcamento;
    String data;

    Movies(int id, String titulo, float duracao, int orcamento, String data) {
        this.id = id;
        this.titulo = titulo;
        this.duracao = duracao;
        this.orcamento = orcamento;
        this.data = data;
    }
}

class MovieVotes {
    int id;
    double mediaVotos;
    int nrVotos;

    MovieVotes(int id, double mediaVotos, int nrVotos) {
        this.id = id;
        this.mediaVotos = mediaVotos;
        this.nrVotos = nrVotos;
    }
}

class Genres {
    String genero;
    int id;

    Genres(String genero, int id) {
        this.genero = genero;
        this.id = id;
    }
}
class Pessoa {
    int id;
    String nome;
    String genero;

    Pessoa(int id, String nome, String genero) {
        this.id = id;
        this.nome = nome;
        this.genero = genero;
    }
}
class Filme {
    int id;
    String titulo;
    Pessoa actores;
    Pessoa realizadores;
    Filme generos;
    String dataDeLancamento;
    int orcamento;
    float mediaDeVotos;
    int nrDeVotos;

    Filme(){}
    Filme(int id, String titulo, Pessoa actores, Pessoa realizadores,
          Filme genero, String dataDeLancamento, int orcamento, float mediaDeVotos, int nrDeVotos) {
        this.id = id;
        this.titulo = titulo;
        this.actores = actores;
        this.realizadores = realizadores;
        this.generos = genero;
        this.dataDeLancamento = dataDeLancamento;
        this.orcamento = orcamento;
        this.mediaDeVotos = mediaDeVotos;
        this.nrDeVotos = nrDeVotos;
    }

    public String toString() {
        String[] dateFormat = this.dataDeLancamento.split("-");
        String dateFormatFinal = String.join("-", dateFormat[2], dateFormat[1], dateFormat[0]);
        return id + " | " + titulo + " | " + dateFormatFinal + " | " + nrDeVotos + " | " + mediaDeVotos;
    }
}

class GeneroCinematografico {
    String genero;

    GeneroCinematografico(String genero) {
        this.genero = genero;
    }
}

// Adiciona classe a ser usada na query 'GET_TOP_N_MOVIES_RATIO' (guarda valores intermedios)
class RacioFilme {
    String titulo;  // Guarda o titulo do filme
    double racio;  // Guarda o rácio (VotaçãoMédia/NrAtores)

    RacioFilme(String titulo, double racio) {
        this.titulo = titulo;
        this.racio = racio;
    }
}