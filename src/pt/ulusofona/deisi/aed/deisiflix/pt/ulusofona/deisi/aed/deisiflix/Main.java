package pt.ulusofona.deisi.aed.deisiflix.pt.ulusofona.deisi.aed.deisiflix;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
public class Main {
    static void lerFicheiros() throws IOException {}
    static ArrayList<Filme> getFilmes() { return null; }
    static ArrayList<String> getLinhasIgnoradas(String fileName) { return null; }

    public static void main(String[] args) throws IOException {
        scan0();
        scan1();
        scan2();
        scan3();
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
    public static ArrayList<String> scan3() throws IOException {
        String nomeFicheiro = "deisi_movies.txt";
        File ficheiro = new File(nomeFicheiro);
        FileInputStream fis = new FileInputStream(ficheiro);
        Scanner leitorFicheiro = new Scanner(fis);
        ArrayList<String> linhasIgnoradas = new ArrayList<String>();
        while(leitorFicheiro.hasNextLine()) {
            String linha = leitorFicheiro.nextLine();
            String dados[] = linha.split(",");
            if (dados.length == 5) {
                int ID = Integer.parseInt(dados[0].trim());
                String Título = dados[1].trim();
                float Duração = Float.parseFloat(dados[2].trim());
                int Orçamento = Integer.parseInt(dados[3].trim());
                String Data = dados[4];
                /*System.out.println("ID Filme: " + ID);
                System.out.println("Título: " + Título);
                System.out.println("Duração: " + Duração);
                System.out.println("Orçamento: " + Orçamento);
                System.out.println("Data: " + Data);*/
                Movies Movies = new Movies(ID, Título, Duração, Orçamento, Data);
            } else {
                linhasIgnoradas.add(linha);
            }
        }
        leitorFicheiro.close();
        return linhasIgnoradas;
    }
}

class DeisiPeople {
    String TipoPessoa;
    int ID;
    String NomePessoa;
    String Género;
    int IDFilme;

    DeisiPeople(String TipoPessoa, int ID, String NomePessoa, String Género, int IDFilme) {
        this.TipoPessoa = TipoPessoa;
        this.ID = ID;
        this.NomePessoa = NomePessoa;
        this.Género = Género;
        this.IDFilme = IDFilme;
    }
}

class Movies {
    int ID;
    String Título;
    float Duração;
    int Orçamento;
    String Data;

    Movies(int ID, String Título, float Duração, int Orçamento, String Data) {
        this.ID = ID;
        this.Título = Título;
        this.Duração = Duração;
        this.Orçamento = Orçamento;
        this.Data = Data;
    }
}

class MovieVotes {
    int ID;
    float MédiaVotos;
    int NrVotos;

    MovieVotes(int ID, float MédiaVotos, int NrVotos) {
        this.ID = ID;
        this.MédiaVotos = MédiaVotos;
        this.NrVotos = NrVotos;
    }
}

class Genres {
    String Género;
    int ID;

    Genres(String Género, int ID) {
        this.Género = Género;
        this.ID = ID;
    }
}
class Pessoa {
    int ID;
    String Nome;
    String Género;

    Pessoa(int ID, String Nome, String género) {
        this.ID = ID;
        this.Nome = Nome;
        this.Género = Género;
    }
}
class Filme {
    int ID;
    String Título;
    Pessoa Actores;
    Pessoa Realizadores;
    Filme Géneros;
    String DataDeLançamento;
    int Orçamento;
    float MédiaDeVotos;
    int NrDeVotos;

    Filme(int ID, String Título, Pessoa Actores, Pessoa Realizaores,
          Filme Género, String DataDeLançamento, int Orçamento, float MédiaDeVotos, int NrDeVotos) {
        this.ID = ID;
        this.Título = Título;
        this.Actores = Actores;
        this.Realizadores = Realizaores;
        this.Géneros = Género;
        this.DataDeLançamento = DataDeLançamento;
        this.Orçamento = Orçamento;
        this.MédiaDeVotos = MédiaDeVotos;
        this.NrDeVotos = NrDeVotos;
    }

    public String toString() {
        return "ID | Título | Data Lançamento (formato AAAA-MM-DD) | Nr. Votos | Média Votos";
    }
}

class GéneroCinematográfico {
    String Género;

    GéneroCinematográfico(String Género) {
        this.Género = Género;
    }
}