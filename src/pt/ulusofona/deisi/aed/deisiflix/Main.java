package pt.ulusofona.deisi.aed.deisiflix;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
public class Main {
    static ArrayList<Filme> linhasVálidas = new ArrayList<Filme>();
    static ArrayList<String> linhasIgnoradas = new ArrayList<String>();
    static ArrayList<String> deisiPeople = new ArrayList<String>();
    static ArrayList<String> deisiGenres = new ArrayList<String>();
    static ArrayList<String> deisiMovieVotes = new ArrayList<String>();
    static void lerFicheiros() throws IOException {
        MoviesValid moviesValid = scan3();
        linhasVálidas = moviesValid.listaFilmes;
        linhasIgnoradas = moviesValid.linhasIgnoradas;
        deisiPeople = scan0();
        deisiGenres = scan1();
        deisiMovieVotes = scan2();
    }
    static ArrayList<Filme> getFilmes() { return linhasVálidas; }

    static ArrayList<String> getLinhasIgnoradas(String fileName) throws IOException {
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
    }
    //deisi_people
    public static ArrayList<String> scan0() throws IOException {
        String nomeFicheiro = "deisi_people.txt";
        File ficheiro = new File(nomeFicheiro);
        FileInputStream fis = new FileInputStream(ficheiro);
        Scanner leitorFicheiro = new Scanner(fis);
        ArrayList<String> linhasIgnoradas = new ArrayList<String>();
        while(leitorFicheiro.hasNextLine()) {
            String linha = leitorFicheiro.nextLine();
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
            } else {
                linhasIgnoradas.add(linha);
            }
        }
        leitorFicheiro.close();
        return linhasIgnoradas;
    }

    //deisi_genres
    public static ArrayList<String> scan1() throws IOException {
        String nomeFicheiro = "deisi_genres.txt";
        File ficheiro = new File(nomeFicheiro);
        FileInputStream fis = new FileInputStream(ficheiro);
        Scanner leitorFicheiro = new Scanner(fis);
        ArrayList<String> linhasIgnoradas = new ArrayList<String>();
        while(leitorFicheiro.hasNextLine()) {
            String linha = leitorFicheiro.nextLine();
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
        leitorFicheiro.close();
        return linhasIgnoradas;
    }

    //deisi_movie_votes
    public static ArrayList<String> scan2() throws IOException {
        String nomeFicheiro = "deisi_movie_votes.txt";
        File ficheiro = new File(nomeFicheiro);
        FileInputStream fis = new FileInputStream(ficheiro);
        Scanner leitorFicheiro = new Scanner(fis);
        ArrayList<String> linhasIgnoradas = new ArrayList<String>();
        while(leitorFicheiro.hasNextLine()) {
            String linha = leitorFicheiro.nextLine();
            String dados[] = linha.split(",");
            if (dados.length == 3) {
                int ID = Integer.parseInt(dados[0].trim());
                float MédiaVotos = Float.parseFloat(dados[1].trim());
                int NrVotos = Integer.parseInt(dados[2].trim());
                /*System.out.println("ID Filme: " + ID);
                System.out.println("Média Votos: " + MédiaVotos);
                System.out.println("Nr Votos: " + NrVotos);*/

                MovieVotes MovieVotes = new MovieVotes(ID, MédiaVotos, NrVotos);
            } else {
                linhasIgnoradas.add(linha);
            }
        }
        leitorFicheiro.close();
        return linhasIgnoradas;
    }
    //deisi_movies
    public static MoviesValid scan3() throws IOException {
        String nomeFicheiro = "deisi_movies.txt";
        File ficheiro = new File(nomeFicheiro);
        FileInputStream fis = new FileInputStream(ficheiro);
        Scanner leitorFicheiro = new Scanner(fis);
        ArrayList<String> linhasIgnoradas = new ArrayList<String>();
        ArrayList<Filme> linhasVálidas = new ArrayList<Filme>();
        while(leitorFicheiro.hasNextLine()) {
            String linha = leitorFicheiro.nextLine();
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
            } else {
                linhasIgnoradas.add(linha);
            }
        }
        leitorFicheiro.close();
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