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

    //GET_MOST_WATCHED_GENRE_YEAR
    //Key IDFilme | Devolve o numero de generos do filme (ação etc...)
    static HashMap<Integer,ArrayList> insertIDFilmeGetGenerosFilme = new HashMap<>();

    //COUNT_ACTORS_3_YEARS
    //Key Ano de Filme | Devolve Arraylist com IDs dos filmes
    static HashMap<String,ArrayList> actoresDiferentes = new HashMap<>();
    //Key ID Filme | Devolve Arraylist com IDs dos participantes no filme
    static HashMap<Integer,ArrayList> actoresDiferentes2 = new HashMap<>();


    //TOP_MOVIES_WITH_GENDER_BIAS
    // Key IDFilme | Devolve Arraylist com IDs dos actores no filme
    static HashMap<Integer,ArrayList> apenasActoresDeUmFilme = new HashMap<>();
    //Key ID Actor | Devolve Genero Actor
    static HashMap<Integer,String> insertIDActorGetGenero = new HashMap<>();

    //GET_RECENT_TITLES_SAME_AVG_VOTES_ONE_SHARED_ACTOR
    // Key IDFilme | Devolve Arraylist com IDs dos participantes no filme
    static HashMap<Integer,ArrayList> actoresDeUmFilme = new HashMap<>();
    // Key IDFilme | Devolve o titulo do filme
    static HashMap<Integer,String> tituloDeUmFilme = new HashMap<>();
    // Key IDFilme | Devolve data do filme
    static HashMap<Integer,String> dataDeUmFilme = new HashMap<>();
    // Key Media Filme | Devolve Arraylist com IDs do filme com essa média
    static HashMap<Double,ArrayList> votacaoMediaFilme = new HashMap<>();

    //GET_TOP_N_YEARS_BEST_AVG_VOTES
    static HashSet<String> anoFilmes = new HashSet<>();
    // Key IDFilme | Devolve Double MediaFilmes
    static HashMap<Integer,Double> idFilmeMediaVotos = new HashMap<>();
    static HashMap<String,Double> anoPontuacao = new HashMap<>();

    //GET_TOP_N_MOVIES_RATIO
    static HashMap<Integer,Integer> insertIDFilmeGetNumeroVotos = new HashMap<>();

    //GET_TOP_ACTOR_YEAR
    static HashMap<String,ArrayList> topActorYear = new HashMap<>();
    static HashMap<Integer,String> getPersonNameById = new HashMap<>();

    //TOP_6_DIRECTORS_WITHIN_FAMILY
    // Key ID Filme | Devolve Arraylist com IDs dos realizadores/directors
    static HashMap<Integer,ArrayList> insertIDFilmeGetIDDirector = new HashMap<>();
    // Key ID Realizador/Director | Devolve o seu nome
    static HashMap<Integer,String> insertDirectorIDGetName = new HashMap<>();

    //Key IDFilme | Recebe arraylist com os generos dos actores desse filme
    static HashMap<Integer,ArrayList> insertIDFilmeGetListGeneros = new HashMap<>();

    //Insert Actor
    // Key IDFilme | Devolve arraylist em string com os dados do filme
    static HashMap<Integer,Filme> todosFilmes = new HashMap<>();
    // Key IDActor | Devolve arraylist com os ids dos filmes em que participou
    static HashMap<Integer,ArrayList> filmesActor = new HashMap<>();

    static ArrayList<Filme> linhasVálidas = new ArrayList<Filme>();
    static ArrayList<String> linhasIgnoradas = new ArrayList<String>();
    static ArrayList<String> deisiPeople = new ArrayList<String>();
    static ArrayList<String> deisiGenres = new ArrayList<String>();
    static ArrayList<String> deisiMovieVotes = new ArrayList<String>();



    public static ArrayList<String> quickSort(ArrayList<String> list) {

        if (list.isEmpty()) {
            return list;
        }
        ArrayList<String> sorted;
        ArrayList<String> smaller = new ArrayList<String>();
        ArrayList<String> greater = new ArrayList<String>();
        String[] parts = list.get(0).split(":");
        int pivot = Integer.parseInt(parts[parts.length-1]);
        int i;
        int j;
        for (i=1;i<list.size();i++)
        {
            String[] parts2 = list.get(i).split(":");
            j=Integer.parseInt(parts2[parts2.length-1]);
            if (j>pivot) {
                smaller.add(list.get(i));
            } else {
                greater.add(list.get(i));
            }
        }
        smaller=quickSort(smaller);
        greater=quickSort(greater);
        smaller.add(list.get(0));
        smaller.addAll(greater);
        sorted = smaller;

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
        if (pergunta.contains("COUNT_MOVIES_ACTOR")) {
            query = PerguntasQueries.countMoviesActorFunction(pergunta,query);
        } else if (pergunta.contains("GET_MOVIES_ACTOR_YEAR")) {
            query = PerguntasQueries.getMoviesActorYearFunction(pergunta,query);
        } else if (pergunta.contains("COUNT_MOVIES_WITH_ACTORS")) {
            query = PerguntasQueries.countMoviesWithActorsFunction(pergunta,query);
        } else if (pergunta.contains("COUNT_ACTORS_3_YEARS")) {
            query = PerguntasQueries.countActores3YearsFunction(pergunta,query);
        } else if (pergunta.contains("TOP_MOVIES_WITH_GENDER_BIAS")) {
            query = PerguntasQueries.topMoviesWithGenderBiasFunction(pergunta,query);
        } else if (pergunta.contains("GET_RECENT_TITLES_SAME_AVG_VOTES_ONE_SHARED_ACTOR")) {
            query = PerguntasQueries.getRecentTitlesSameAvgVotesOneSharedActorFunction(pergunta,query);
        } else if (pergunta.contains("GET_TOP_N_YEARS_BEST_AVG_VOTES")) {
            query = PerguntasQueries.getTopNYearsBestAvgVotesFunction(pergunta,query);
        } else if (pergunta.contains("DISTANCE_BETWEEN_ACTORS")) {
            query = PerguntasQueries.distanceBetweenActorsFunction(pergunta,query);
        } else if (pergunta.contains("GET_TOP_N_MOVIES_RATIO")) {
            query = PerguntasQueries.getTopNMoviesRatioFunction(pergunta,query);
        } else if (pergunta.contains("TOP_6_DIRECTORS_WITHIN_FAMILY")) {
            query = PerguntasQueries.topMoviesWithGenderBiasFunction(pergunta,query);
        } else if (pergunta.contains("GET_TOP_ACTOR_YEAR")) {
            query = PerguntasQueries.getTopActorYearFunction(pergunta,query);
        } else if (pergunta.contains("INSERT_ACTOR")) {
            query = PerguntasQueries.insertActorFunction(pergunta,query);
        } else if (pergunta.contains("REMOVE_ACTOR")) {
            query = PerguntasQueries.removeActorFunction(pergunta,query);
        } else if (pergunta.contains("GET_DUPLICATE_LINES_YEAR")) {
            query = PerguntasQueries.getDuplicateLinesYearFunction(pergunta,query);
        } else if (pergunta.contains("GET_TOP3_MOVIES_GENRE_YEAR")) {
            query = PerguntasQueries.getTop3MoviesGenreYearFunction(pergunta,query);
        } else {
            query = null;
        }
        return query;
    }
    public static String getVideoURL() { return null; }
    public static String getCreativeQuery() { return "GET_TOP3_MOVIES_GENRE_YEAR"; }

    static boolean readFiles = false;

    static void lerFicheiros() throws IOException {
        todosFilmes = new HashMap<>();
        dicionario = new HashMap<>();
        filmesAno = new HashMap<>();
        pesquisaPorIDFilme = new HashMap<>();
        insertIDFilmeGetGenerosFilme = new HashMap<>();
        actoresDiferentes = new HashMap<>();
        actoresDiferentes2 = new HashMap<>();
        apenasActoresDeUmFilme = new HashMap<>();
        insertIDActorGetGenero = new HashMap<>();
        actoresDeUmFilme = new HashMap<>();
        tituloDeUmFilme = new HashMap<>();
        dataDeUmFilme = new HashMap<>();
        votacaoMediaFilme = new HashMap<>();
        anoFilmes = new HashSet<>();
        idFilmeMediaVotos = new HashMap<>();
        anoPontuacao = new HashMap<>();
        insertIDFilmeGetNumeroVotos = new HashMap<>();
        topActorYear = new HashMap<>();
        getPersonNameById = new HashMap<>();
        insertIDFilmeGetIDDirector = new HashMap<>();
        insertDirectorIDGetName = new HashMap<>();
        insertIDFilmeGetListGeneros = new HashMap<>();
        //if (readFiles == false) {
            deisiPeople = readerDeisiPeople();
            deisiGenres = readerDeisiGenres();
            deisiMovieVotes = readerDeisiMovieVotes(linhasVálidas);
            MoviesValid moviesValid = readerDeisiMovies();
            linhasVálidas = moviesValid.listaFilmes;
            linhasIgnoradas = moviesValid.linhasIgnoradas;
            readFiles = true;
            //System.out.println(getFilmes());
            //System.out.println(getLinhasIgnoradas("deisi_movies.txt").size());
        //}
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
            if (resultado != null) {
                System.out.println(resultado.valor);
                System.out.println("(demorou " + resultado.tempo + " ms)");
            } else {
                System.out.println("Pergunta desconhecida. Tente novamente.");
            }
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

                if (filmesActor.get(ID) == null) {
                    ArrayList<Integer> arrayList = new ArrayList<>();
                    arrayList.add(IDFilme);
                    filmesActor.put(ID,arrayList);
                } else {
                    ArrayList<Integer> arrayList = filmesActor.get(ID);
                    arrayList.add(IDFilme);
                    filmesActor.put(ID,arrayList);
                }

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
                if (insertIDFilmeGetGenerosFilme.get(ID) == null) {
                    ArrayList<String> arrayList = new ArrayList<>();
                    arrayList.add(Género);
                    insertIDFilmeGetGenerosFilme.put(ID,arrayList);
                } else {
                    ArrayList<String> arrayList = insertIDFilmeGetGenerosFilme.get(ID);
                    arrayList.add(Género);
                    insertIDFilmeGetGenerosFilme.put(ID,arrayList);
                }
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
                ArrayList<Integer> idsActores = apenasActoresDeUmFilme.get(ID);
                int masculino = 0;
                int feminino = 0;
                if (idsActores != null) {
                    for (int i = 0; i < idsActores.size(); i++) {
                        if (insertIDActorGetGenero.get(idsActores.get(i)).equals("M")) {
                            masculino++;
                        }
                        if (insertIDActorGetGenero.get(idsActores.get(i)).equals("F")) {
                            feminino++;
                        }
                    }
                }

                int numRealizadores = 0;
                if (insertIDFilmeGetIDDirector.get(ID) != null) {
                    numRealizadores = insertIDFilmeGetIDDirector.get(ID).size();
                }
                int numGeneros = 0;
                if (insertIDFilmeGetGenerosFilme.get(ID) != null) {
                    numGeneros = insertIDFilmeGetGenerosFilme.get(ID).size();
                }
                int numVotos = 0;
                if (insertIDFilmeGetNumeroVotos.get(ID) != null) {
                    numVotos = insertIDFilmeGetNumeroVotos.get(ID);
                }
                double mediaVotos = 0;
                if (idFilmeMediaVotos.get(ID) != null) {
                    mediaVotos = idFilmeMediaVotos.get(ID);
                }

                Filme filme = new Filme();
                filme.id = ID;
                filme.titulo = Título;
                filme.orcamento = Orçamento;
                filme.dataDeLancamento = Data;
                filme.nrDeVotos = numVotos;
                filme.mediaDeVotos = mediaVotos;
                filme.generos = numGeneros;
                filme.realizadores = numRealizadores;
                filme.numActoresMasculinos = masculino;
                filme.numActoresFemininos = feminino;

                todosFilmes.put(ID,filme);

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
    int actores;
    int realizadores;
    int generos;
    String dataDeLancamento;
    int orcamento;
    double mediaDeVotos;
    int nrDeVotos;
    int numActoresMasculinos;
    int numActoresFemininos;

    Filme(){}
    Filme(int id, String titulo, int realizadores,
          int genero, String dataDeLancamento, double mediaDeVotos, int nrDeVotos, int numActoresMasculinos,
          int numActoresFemininos) {
        this.id = id;
        this.titulo = titulo;
        this.realizadores = realizadores;
        this.generos = genero;
        this.dataDeLancamento = dataDeLancamento;
        this.mediaDeVotos = mediaDeVotos;
        this.nrDeVotos = nrDeVotos;
        this.numActoresMasculinos = numActoresMasculinos;
        this.numActoresFemininos = numActoresFemininos;
    }

    public String toString() {
        String[] dateFormat = this.dataDeLancamento.split("-");
        String dateFormatFinal = String.join("-", dateFormat[2], dateFormat[1], dateFormat[0]);
        return id + " | " + titulo + " | " + dateFormatFinal + " | " + nrDeVotos + " | " + mediaDeVotos +
                " | " + generos + " | " + realizadores + " | " + numActoresMasculinos + " | " + numActoresFemininos;
    }
}

class GeneroCinematografico {
    String genero;

    GeneroCinematografico(String genero) {
        this.genero = genero;
    }
}

class RacioFilme {
    String titulo;
    double racio;

    RacioFilme(String titulo, double racio) {
        this.titulo = titulo;
        this.racio = racio;
    }
}