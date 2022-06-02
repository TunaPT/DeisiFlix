package pt.ulusofona.deisi.aed.deisiflix;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


import java.lang.reflect.Array;
import java.util.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
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

    //static ArrayList<String> dicionario = new ArrayList<String>(); //HASHMAP
    static HashMap<String,Integer> dicionario = new HashMap<String,Integer>(); //HASHMAP
    static HashMap<String,HashMap> filmesAno = new HashMap<String,HashMap>();
    static HashMap<Integer,String[]> pesquisaPorIDFilme = new HashMap<Integer,String[]>();
    static ArrayList<Filme> linhasVálidas = new ArrayList<Filme>();
    static ArrayList<String> linhasIgnoradas = new ArrayList<String>();
    static ArrayList<String> deisiPeople = new ArrayList<String>();
    static ArrayList<String> deisiGenres = new ArrayList<String>();
    static ArrayList<String> deisiMovieVotes = new ArrayList<String>();


    public static QueryResult perguntar(String pergunta) {
        QueryResult teste = new QueryResult();
        if (pergunta.contains("COUNT_MOVIES_ACTOR")) {
            long actualTime = System.currentTimeMillis();
            String[] parts = pergunta.split("COUNT_MOVIES_ACTOR");
            String nomeAtor = parts[1].trim(); // Nome completo do autor
            if (dicionario.get(nomeAtor) == null) {
                long finalTime = System.currentTimeMillis();
                teste = new QueryResult("0",finalTime-actualTime);
            } else {
                long finalTime = System.currentTimeMillis();
                teste = new QueryResult(Integer.toString(dicionario.get(nomeAtor)),finalTime-actualTime);
            }
        } else if (pergunta.contains("GET_MOVIES_ACTOR_YEAR")) {
            String[] parts = pergunta.split(" ");
            String part1 = parts[0].trim();
            String firstName = parts[1].trim();
            String secondName = parts[2].trim();
            String year = parts[3].trim();
            if (filmesAno.get(firstName + " " + secondName) != null) {
                // System.out.println(filmesAno.get(firstName + " " + secondName).get(firstName + " " + secondName)); //DEBUG
                //System.out.println(filmesAno.get(firstName + " " + secondName)); //DEBUG
                //for (int i = 0;)
                //Integer[] xd = filmesAno.get(firstName + " " + secondName).get(firstName + " " + secondName);
                System.out.println((filmesAno.get(firstName + " " + secondName).get(firstName + " " + secondName)));
                //System.out.println(pesquisaPorIDFilme.get(filmesAno.get(firstName + " " + secondName).get(firstName + " " + secondName)));
            }

            teste = new QueryResult("s",2);
        } else if (pergunta.contains("COUNT_MOVIES_WITH_ACTORS")) {

            String[] parts = pergunta.split(";");


            teste = new QueryResult("s",2);
        } else if (pergunta.contains("COUNT_ACTORS_3_YEARS")) {

            String[] parts = pergunta.split(" ");


            teste = new QueryResult("s",2);
        } else if (pergunta.contains("TOP_MOVIES_WITH_GENDER_BIAS")) {
            teste = new QueryResult("s",2);
        } else if (pergunta.contains("GET_RECENT_TITLES_SAME_AVG_VOTES_ONE_SHARED_ACTOR")) {
            teste = new QueryResult("s",2);
        } else if (pergunta.contains("GET_TOP_N_YEARS_BEST_AVG_VOTES")) {
            teste = new QueryResult("s",2);
        } else if (pergunta.contains("DISTANCE_BETWEEN_ACTORS")) {
            teste = new QueryResult("s",2);
        } else if (pergunta.contains("GET_TOP_N_MOVIES_RATIO")) {
            teste = new QueryResult("s",2);
        } else if (pergunta.contains("TOP_6_DIRECTORS_WITHIN_FAMILY")) {
            teste = new QueryResult("s",2);
        } else if (pergunta.contains("GET_TOP_ACTOR_YEAR")) {
            teste = new QueryResult("s",2);
        } else if (pergunta.contains("INSERT_ACTOR")) {
            teste = new QueryResult("s",2);
        } else if (pergunta.contains("REMOVE_ACTOR")) {
            teste = new QueryResult("s",2);
        } else if (pergunta.contains("GET_DUPLICATE_LINES_YEAR")) {
            teste = new QueryResult("s",2);
        }
        return teste;
    }
    public static String getVideoURL() { return null; }
    public static String getCreativeQuery() { return null; }

    static void lerFicheiros() throws IOException {
        MoviesValid moviesValid = readerDeisiMovies();
        linhasVálidas = moviesValid.listaFilmes;
        linhasIgnoradas = moviesValid.linhasIgnoradas;
        deisiPeople = readerDeisiPeople();
        deisiGenres = readerDeisiGenres();
        deisiMovieVotes = readerDeisiMovieVotes(linhasVálidas);
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
                    //dicionario.add(NomePessoa);
                    if (dicionario.get(NomePessoa) != null ) {
                        countNomePessoa = dicionario.get(NomePessoa)+1;
                    }
                    dicionario.put(NomePessoa,countNomePessoa);
                    countNomePessoa = 1;
                }
                if (filmesAno.get(NomePessoa) == null) {
                    HashMap<String,ArrayList> HashIDFilesPorPessoa = new HashMap<String,ArrayList>();
                    ArrayList<Integer> IDFilmesPesoa = new ArrayList<Integer>();
                    IDFilmesPesoa.add(IDFilme);
                    HashIDFilesPorPessoa.put(NomePessoa,IDFilmesPesoa);
                    filmesAno.put(NomePessoa,HashIDFilesPorPessoa);
                } else {
                    HashMap<String,ArrayList> HashIDFilesPorPessoa = new HashMap<String,ArrayList>();
                    HashIDFilesPorPessoa = filmesAno.get(NomePessoa);
                    ArrayList<Integer> IDFilmesPesoa = new ArrayList<Integer>();
                    IDFilmesPesoa = HashIDFilesPorPessoa.get(NomePessoa);
                    IDFilmesPesoa.add(IDFilme);
                    filmesAno.put(NomePessoa,HashIDFilesPorPessoa);
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
            } else {
                linhasIgnoradas.add(linha);
            }
        }
        reader.close();
        return new MoviesValid(linhasVálidas,linhasIgnoradas);
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