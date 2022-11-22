package pt.ulusofona.deisi.aed.deisiflix;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.HashMap;

class QueryResult {
    String valor;
    long tempo;

    QueryResult() {}
    QueryResult(String valor, long tempo) {
        this.valor = valor;
        this.tempo = tempo;
    }
}

public class Main {

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

    //GET_DUPLICATE_LINES_YEAR
    static ArrayList<String> infoDuplicatedLines = new ArrayList<>();

    static ArrayList<Filme> linhasVálidas = new ArrayList<Filme>();
    static ArrayList<String> linhasIgnoradas = new ArrayList<String>();
    static ArrayList<String> deisiPeople = new ArrayList<String>();
    static ArrayList<String> deisiGenres = new ArrayList<String>();
    static ArrayList<String> deisiMovieVotes = new ArrayList<String>();

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
            query = PerguntasQueries.top6DirectorsWithinFamilyFunction(pergunta,query);
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

    public static String getVideoURL() { return "https://youtu.be/Gp-v9aHpac8"; }
    public static String getCreativeQuery() { return "GET_TOP3_MOVIES_GENRE_YEAR"; }

    static void lerFicheiros() throws IOException {
        //Reset nos HashMaps
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
        infoDuplicatedLines = new ArrayList<>();

        //Leitura Readers
        deisiPeople = Readers.readerDeisiPeople();
        deisiGenres = Readers.readerDeisiGenres();
        deisiMovieVotes = Readers.readerDeisiMovieVotes(linhasVálidas);
        MoviesValid moviesValid = Readers.readerDeisiMovies();
        linhasVálidas = moviesValid.listaFilmes;
        linhasIgnoradas = moviesValid.linhasIgnoradas;
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
}

// Classes

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