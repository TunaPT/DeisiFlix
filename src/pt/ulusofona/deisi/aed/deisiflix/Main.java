package pt.ulusofona.deisi.aed.deisiflix;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
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


    static HashMap<String,ArrayList> actoresDiferentes = new HashMap<>();
    static HashMap<Integer,ArrayList> actoresDiferentes2 = new HashMap<>();
    static HashMap<String,HashSet> pesquisaAnoActor = new HashMap<>();

    //GET_RECENT_TITLES_SAME_AVG_VOTES_ONE_SHARED_ACTOR
    static HashMap<Integer,ArrayList> actoresDeUmFilme = new HashMap<>(); // Key IDFilme | Devolve Arraylist com IDs dos participantes no filme
    static HashMap<Integer,String> tituloDeUmFilme = new HashMap<>(); // Key IDFilme | Devolve o titulo do filme
    static HashMap<Integer,String> dataDeUmFilme = new HashMap<>(); // Key IDFilme | Devolve data do filme
    static HashMap<Float,ArrayList> votacaoMediaFilme = new HashMap<>(); // Key Media Filme | Devolve Arraylist com IDs do filme com essa média

    //GET_TOP_N_YEARS_BEST_AVG_VOTES
    static HashSet<String> anoFilmes = new HashSet<>();
    static HashMap<String,ArrayList> mediaVotosFilmes = new HashMap<>();
    static HashMap<Integer,Float> idFilmeMediaVotos = new HashMap<>(); // Key IDFilme | Devolve Arraylist MediaFilmes
    static HashMap<String,Float> anoPontuacao = new HashMap<>();

    //GET_TOP_ACTOR_YEAR
    static HashMap<String,ArrayList> topActorYear = new HashMap<>();
    static HashMap<Integer,String> getPersonNameById = new HashMap<>();


    static ArrayList<Filme> linhasVálidas = new ArrayList<Filme>();
    static ArrayList<String> linhasIgnoradas = new ArrayList<String>();
    static ArrayList<String> deisiPeople = new ArrayList<String>();
    static ArrayList<String> deisiGenres = new ArrayList<String>();
    static ArrayList<String> deisiMovieVotes = new ArrayList<String>();


    public static QueryResult perguntar(String pergunta) {
        QueryResult query = new QueryResult();
        if (pergunta.contains("COUNT_MOVIES_ACTOR")) {
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
        } else if (pergunta.contains("GET_MOVIES_ACTOR_YEAR")) {
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
        } else if (pergunta.contains("COUNT_MOVIES_WITH_ACTORS")) {
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
        } else if (pergunta.contains("COUNT_ACTORS_3_YEARS")) {

            /*static HashMap<String,ArrayList> actoresDiferentes = new HashMap<>();  //Key ANO, devolve filmes desse ano
            static HashMap<Integer,ArrayList> actoresDiferentes2 = new HashMap<>(); //Key filme, devolve IDs das pessoas desse filme
            static HashMap<String,HashSet> pesquisaAnoActor = new HashMap<>();*/
            long initialTime = System.currentTimeMillis();
            String[] parts = pergunta.split(" ");
            HashSet<Integer> IDActor = new HashSet<>();
            int count=0;
            for (int i=1;i<parts.length;i++) {
                //System.out.println("IDs Filmes no ano " + parts[i] + ": " + actoresDiferentes.get(parts[i]) + "\n");
                for (int k=0;k<actoresDiferentes.get(parts[i]).size();k++) {
                    //System.out.println("IDs Actores participantes no Filme com ID " + actoresDiferentes.get(parts[i]).get(k) + ": " + actoresDiferentes2.get(actoresDiferentes.get(parts[i]).get(k)));
                    if (actoresDiferentes2.get(actoresDiferentes.get(parts[i]).get(k)) != null) {
                        for (int l = 0; l < actoresDiferentes2.get(actoresDiferentes.get(parts[i]).get(k)).size(); l++) {
                            //System.out.println("ID Pessoa: " + actoresDiferentes2.get(actoresDiferentes.get(parts[i]).get(k)).get(l));
                            //IDActor.add((Integer) actoresDiferentes2.get(actoresDiferentes.get(parts[i]).get(k)).get(l));
                            if (pesquisaAnoActor.get(parts[i]) == null) {
                                pesquisaAnoActor.put(parts[i], new HashSet<Integer>((Integer) actoresDiferentes2.get(actoresDiferentes.get(parts[i]).get(k)).get(l)));
                            } else {
                                IDActor = pesquisaAnoActor.get(parts[i]);
                                IDActor.add((Integer) actoresDiferentes2.get(actoresDiferentes.get(parts[i]).get(k)).get(l));
                                pesquisaAnoActor.put(parts[i],IDActor);
                            }
                            // Objetivo colocar o ano e um HashSet com os IDs todos dos actores desse ano //VERIFICAR ESTA PARTE AQUI - FALTA ADICIONAR OS IDS A UM UNICO HASHSET
                        }
                        //System.out.print("SIZE ArrayList: " + actoresDiferentes2.get(actoresDiferentes.get(parts[i]).get(k)).size() + "\n");
                        //System.out.println();
                    }
                }
                //System.out.println("No Ano " + parts[i] + " existem " + actoresDiferentes.get(parts[i]).size() + " actores");
                //System.out.println("Todos os Actores do Ano: " + parts[i] + ": " + pesquisaAnoActor.get(parts[i]));
                //System.out.println();
            }
            //System.out.println(actoresDiferentes.get(parts[1]).size());
            for (int i=0;i<actoresDiferentes.get(parts[1]).size();i++) {
                //System.out.println(actoresDiferentes.get(parts[2]).get(i));
                if (pesquisaAnoActor.get(parts[2]).contains(pesquisaAnoActor.get(parts[1]).contains(i))) {
                    System.out.println("zzz");
                    /*for (int k=0;k<actoresDiferentes.get(parts[1]).size();k++) {
                        System.out.println("xxx");
                        if (actoresDiferentes.get(parts[3]).contains(actoresDiferentes.get(parts[1]).get(k))) {
                            System.out.println("aw");
                            count++;
                        }
                    }*/
                }
            }
            long finalTime = System.currentTimeMillis();
            //filmesAnos a chave de procura é o nome de autor e devolve um arraylist com os IDs dos filmes
            //pesquisarPorIDFilme a chave de procura é o ID do filme e devolve um array com o titulo e data
            query = new QueryResult("s",finalTime-initialTime);
        } else if (pergunta.contains("TOP_MOVIES_WITH_GENDER_BIAS")) {
            long initialTime = System.currentTimeMillis();
            long finalTime = System.currentTimeMillis();
            query = new QueryResult("s",finalTime-initialTime);
        } else if (pergunta.contains("GET_RECENT_TITLES_SAME_AVG_VOTES_ONE_SHARED_ACTOR")) {
            long initialTime = System.currentTimeMillis();
            String[] partes = pergunta.split("GET_RECENT_TITLES_SAME_AVG_VOTES_ONE_SHARED_ACTOR");
            String idFilme = partes[1].trim();


            String output = "";
            if (idFilmeMediaVotos.get(Integer.parseInt(idFilme)) != null) {
                float votacaoMediaFilmeX = idFilmeMediaVotos.get(Integer.parseInt(idFilme));
                ArrayList<Integer> idActoresFilmeX = actoresDeUmFilme.get(Integer.parseInt(idFilme)); //Devolve arraylist com IDs dos actores presentes no filme
                ArrayList<Integer> IDFilmesComMesmaMediaDoFilmeX = votacaoMediaFilme.get(votacaoMediaFilmeX); //Devolve arraylist com IDs dos filmes que têm a mesma média
                for (int i = 0; i < IDFilmesComMesmaMediaDoFilmeX.size(); i++) {
                    if (dataDeUmFilme.get(IDFilmesComMesmaMediaDoFilmeX.get(i)).compareTo(dataDeUmFilme.get(Integer.parseInt(idFilme))) > 0) {
                        if (actoresDeUmFilme.get(IDFilmesComMesmaMediaDoFilmeX.get(i)) != null) {
                            for (int k = 0; k < actoresDeUmFilme.get(IDFilmesComMesmaMediaDoFilmeX.get(i)).size(); k++) {
                                if (idActoresFilmeX.contains(actoresDeUmFilme.get(IDFilmesComMesmaMediaDoFilmeX.get(i)).get(k))) {
                                    //System.out.println("Fixe, contem um ator em comum");
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
        } else if (pergunta.contains("GET_TOP_N_YEARS_BEST_AVG_VOTES")) {  // FALTA VER ONDE ESTÁ O ERRO DESTA
            long initialTime = System.currentTimeMillis();
            String[] partes = pergunta.split("GET_TOP_N_YEARS_BEST_AVG_VOTES");
            String topValue = partes[1].trim();
            //mediaVotosFilmes
            //idFilmeMediaVotos
            float mediaAno = 0.0f;
            String[] anoFilmesArray = anoFilmes.toArray(new String[anoFilmes.size()]);
            for (int i=0; i<anoFilmesArray.length;i++) { //anoFilmes dará o ano em questão
                ArrayList idsFilmes = mediaVotosFilmes.get(anoFilmesArray[i]);
                for (int k=0;k<idsFilmes.size();k++) {
                    int idFilme = (int) idsFilmes.get(k); //dá o id do filme
                    float mediaVotos = idFilmeMediaVotos.get(idFilme);  //media de votos do filme
                    mediaAno = mediaAno + mediaVotos;
                }
                anoPontuacao.put(anoFilmesArray[i], mediaAno/idsFilmes.size());
                mediaAno = 0.0f;
            }
            String stringQuery = "";
            String ano = anoFilmesArray[0];
            for (int i=0; i<Integer.parseInt(topValue);i++) {
                float maior = anoPontuacao.get(anoFilmesArray[0]);
                for (int k=1; k<anoFilmesArray.length;k++) {
                    if (anoPontuacao.get(anoFilmesArray[k]) != null) {
                        if (anoPontuacao.get(anoFilmesArray[k]) > maior) {
                            maior = anoPontuacao.get(anoFilmesArray[k]);
                            ano = anoFilmesArray[k];
                        }
                    }
                }
                //System.out.println(anoPontuacao.get("1902"));
                if (i+1 != Integer.parseInt(topValue)) {
                    stringQuery = stringQuery + ano + ":" + anoPontuacao.get(ano) + "\n";
                } else {
                    //String.format("%.2f",anoPontuacao.get(ano))
                    stringQuery = stringQuery + ano + ":" + anoPontuacao.get(ano) + "\n"; //aplicar com apenas duas casas decimais
                }
                anoPontuacao.remove(ano);
            }
            long finalTime = System.currentTimeMillis();
            query = new QueryResult(stringQuery,finalTime-initialTime);
        } else if (pergunta.contains("DISTANCE_BETWEEN_ACTORS")) {
            long initialTime = System.currentTimeMillis();
            long finalTime = System.currentTimeMillis();
            query = new QueryResult("s",finalTime-initialTime);
        } else if (pergunta.contains("GET_TOP_N_MOVIES_RATIO")) {
            long initialTime = System.currentTimeMillis();
            long finalTime = System.currentTimeMillis();
            query = new QueryResult("s",finalTime-initialTime);
        } else if (pergunta.contains("TOP_6_DIRECTORS_WITHIN_FAMILY")) {
            long initialTime = System.currentTimeMillis();
            long finalTime = System.currentTimeMillis();
            query = new QueryResult("s",finalTime-initialTime);
        } else if (pergunta.contains("GET_TOP_ACTOR_YEAR")) {
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
                float MédiaVotos = Float.parseFloat(dados[1].trim());
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
                if (mediaVotosFilmes.get(dateFormat[2]) == null) {
                    mediaVotosFilmes.put(dateFormat[2],new ArrayList<>(ID));
                } else {
                    ArrayList<Integer> arrayList = mediaVotosFilmes.get(dateFormat[2]);
                    arrayList.add(ID);
                    mediaVotosFilmes.put(dateFormat[2],arrayList);
                }

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
    float mediaVotos;
    int nrVotos;

    MovieVotes(int id, float mediaVotos, int nrVotos) {
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