package pt.ulusofona.deisi.aed.deisiflix;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Readers {
    public static ArrayList<String> readerDeisiPeople() throws IOException {
        FileReader fr = new FileReader("deisi_people.txt");
        BufferedReader reader = new BufferedReader(fr);
        String linha = null;
        ArrayList<String> linhasIgnoradas = new ArrayList<String>();
        int countLinha = 0;
        while((linha = reader.readLine()) != null) {
            countLinha++;
            String dados[] = linha.split(",");
            if (dados.length == 5) {
                String TipoPessoa = dados[0].trim();
                int ID = Integer.parseInt(dados[1].trim());
                String NomePessoa = dados[2].trim();
                String Género = dados[3].trim();
                int IDFilme = Integer.parseInt(dados[4].trim());
                ajuda(TipoPessoa,ID,NomePessoa,Género,IDFilme,countLinha);
            } else {
                linhasIgnoradas.add(linha);
            }
        }
        reader.close();
        return linhasIgnoradas;
    }

    public static void ajuda(String TipoPessoa,int ID,String NomePessoa,String Género,int IDFilme,int countLinha) {
        int countNomePessoa = 1;
        if (TipoPessoa.equals("ACTOR")) {
            if (Main.filmesActor.get(ID) != null && Main.filmesActor.get(ID).contains(IDFilme)) {
                Main.infoDuplicatedLines.add(countLinha + ":" + ID + ":" + IDFilme);
            }
        }
        if (Main.filmesActor.get(ID) == null) {
            ArrayList<Integer> arrayList = new ArrayList<>();
            arrayList.add(IDFilme);
            Main.filmesActor.put(ID,arrayList);
        } else {
            ArrayList<Integer> arrayList = Main.filmesActor.get(ID);
            arrayList.add(IDFilme);
            Main.filmesActor.put(ID,arrayList);
        }
        if (Main.filmesDiretor.get(ID) == null) {
            ArrayList<Integer> arrayList = new ArrayList<>();
            arrayList.add(IDFilme);
            Main.filmesDiretor.put(ID,arrayList);
        } else {
            ArrayList<Integer> arrayList = Main.filmesDiretor.get(ID);
            if (!arrayList.contains(IDFilme)){
                arrayList.add(IDFilme);
                Main.filmesDiretor.put(ID,arrayList);
            }
        }
        if (TipoPessoa.equals("ACTOR")) {
            if (Main.dicionario.get(NomePessoa) != null ) {
                countNomePessoa = Main.dicionario.get(NomePessoa)+1;
            }
            Main.dicionario.put(NomePessoa,countNomePessoa);
            countNomePessoa = 1;
        }
        if (Main.filmesAno.get(NomePessoa) == null) {
            ArrayList<Integer> IDFilmesPessoa = new ArrayList<Integer>();
            IDFilmesPessoa.add(IDFilme);
            Main.filmesAno.put(NomePessoa,IDFilmesPessoa);
        } else {
            ArrayList<Integer> IDFilmesPessoa = Main.filmesAno.get(NomePessoa);
            IDFilmesPessoa.add(IDFilme);
            Main.filmesAno.put(NomePessoa,IDFilmesPessoa);
        }
        if (TipoPessoa.equals("ACTOR")) {
            if (Main.actoresDiferentes2.get(IDFilme) == null) {
                ArrayList<Integer> IDsActores = new ArrayList<>();
                IDsActores.add(ID);
                Main.actoresDiferentes2.put(IDFilme, IDsActores);
            } else {
                ArrayList<Integer> IDsActores = Main.actoresDiferentes2.get(IDFilme);
                IDsActores.add(ID);
                Main.actoresDiferentes2.put(IDFilme, IDsActores);
            }
            if (Main.apenasActoresDeUmFilme.get(IDFilme) == null) {
                ArrayList<Integer> arrayList = new ArrayList<>();
                arrayList.add(ID);
                Main.apenasActoresDeUmFilme.put(IDFilme,arrayList);
            } else {
                ArrayList<Integer> arrayList = Main.apenasActoresDeUmFilme.get(IDFilme);
                arrayList.add(ID);
                Main.apenasActoresDeUmFilme.put(IDFilme,arrayList);
            }
            Main.insertIDActorGetGenero.put(ID, Género);
        }
        if (Main.actoresDeUmFilme.get(IDFilme) == null) {
            ArrayList<Integer> arrayList = new ArrayList<>();
            arrayList.add(ID);
            Main.actoresDeUmFilme.put(IDFilme,arrayList);
        } else {
            ArrayList<Integer> arrayList = Main.actoresDeUmFilme.get(IDFilme);
            arrayList.add(ID);
            Main.actoresDeUmFilme.put(IDFilme,arrayList);
        }
        Main.getPersonNameById.put(ID,NomePessoa);
        if (TipoPessoa.equals("DIRECTOR")) {
            if (Main.insertIDFilmeGetIDDirector.get(IDFilme) == null) {
                ArrayList<Integer> arrayList = new ArrayList<>();
                arrayList.add(ID);
                Main.insertIDFilmeGetIDDirector.put(IDFilme,arrayList);
            } else {
                ArrayList<Integer> arrayList = Main.insertIDFilmeGetIDDirector.get(IDFilme);
                arrayList.add(ID);
                Main.insertIDFilmeGetIDDirector.put(IDFilme,arrayList);
            }
            Main.insertDirectorIDGetName.put(ID,NomePessoa);
            Main.insertNomeDiretorGetID.put(NomePessoa,ID);
        }
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
                if (Main.insertIDFilmeGetGenerosFilme.get(ID) == null) {
                    ArrayList<String> arrayList = new ArrayList<>();
                    arrayList.add(Género);
                    Main.insertIDFilmeGetGenerosFilme.put(ID,arrayList);
                } else {
                    ArrayList<String> arrayList = Main.insertIDFilmeGetGenerosFilme.get(ID);
                    arrayList.add(Género);
                    Main.insertIDFilmeGetGenerosFilme.put(ID,arrayList);
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
                Main.idFilmeMediaVotos.put(ID,MédiaVotos);
                Main.insertIDFilmeGetNumeroVotos.put(ID,NrVotos);

                //GET_RECENT_TITLES_SAME_AVG_VOTES_ONE_SHARED_ACTOR
                if (Main.votacaoMediaFilme.get(MédiaVotos) == null) {
                    Main.votacaoMediaFilme.put(MédiaVotos, new ArrayList(ID));
                } else {
                    ArrayList<Integer> arrayList = Main.votacaoMediaFilme.get(MédiaVotos);
                    arrayList.add(ID);
                    Main.votacaoMediaFilme.put(MédiaVotos,arrayList);
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
                ArrayList<Integer> idsActores = Main.apenasActoresDeUmFilme.get(ID);
                int masculino = 0;
                int feminino = 0;
                if (idsActores != null) {
                    for (int i = 0; i < idsActores.size(); i++) {
                        if (Main.insertIDActorGetGenero.get(idsActores.get(i)).equals("M")) {
                            masculino++;
                        }
                        if (Main.insertIDActorGetGenero.get(idsActores.get(i)).equals("F")) {
                            feminino++;
                        }
                    }
                }

                int numRealizadores = 0;
                if (Main.insertIDFilmeGetIDDirector.get(ID) != null) {
                    numRealizadores = Main.insertIDFilmeGetIDDirector.get(ID).size();
                }
                int numGeneros = 0;
                if (Main.insertIDFilmeGetGenerosFilme.get(ID) != null) {
                    numGeneros = Main.insertIDFilmeGetGenerosFilme.get(ID).size();
                }
                int numVotos = 0;
                if (Main.insertIDFilmeGetNumeroVotos.get(ID) != null) {
                    numVotos = Main.insertIDFilmeGetNumeroVotos.get(ID);
                }
                double mediaVotos = 0;
                if (Main.idFilmeMediaVotos.get(ID) != null) {
                    mediaVotos = Main.idFilmeMediaVotos.get(ID);
                }
                Main.orcamentoFilme.put(ID,Orçamento);

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

                Main.todosFilmes.put(ID,filme);

                linhasVálidas.add(filme);
                Movies Movies = new Movies(ID, Título, Duração, Orçamento, Data);
                String[] arrayInfoFilme = {Título,Data};
                Main.pesquisaPorIDFilme.put(ID,arrayInfoFilme);

                // EM OBRAS
                String[] dateFormat = Data.split("-");
                if (Main.actoresDiferentes.get(dateFormat[2]) == null) {
                    ArrayList<Integer> IDsFilmes = new ArrayList<>();
                    IDsFilmes.add(ID);
                    Main.actoresDiferentes.put(dateFormat[2],IDsFilmes);
                } else {
                    ArrayList<Integer> IDsFilmes = Main.actoresDiferentes.get(dateFormat[2]);
                    IDsFilmes.add(ID);
                    Main.actoresDiferentes.put(dateFormat[2],IDsFilmes);
                }

                if (Main.topActorYear.get(dateFormat[2]) == null) {
                    Main.topActorYear.put(dateFormat[2], new ArrayList<Integer>(ID));
                } else {
                    ArrayList<Integer> arrayList = Main.topActorYear.get(dateFormat[2]); //Adiciona ID do Filme
                    arrayList.add(ID);
                    Main.topActorYear.put(dateFormat[2],arrayList);
                }

                //GET_TOP_N_YEARS_BEST_AVG_VOTES
                Main.anoFilmes.add(dateFormat[2]);

                //GET_RECENT_TITLES_SAME_AVG_VOTES_ONE_SHARED_ACTOR
                Main.tituloDeUmFilme.put(ID,Título);
                String dateFormatFinal = String.join("-", dateFormat[2], dateFormat[1], dateFormat[0]);
                Main.dataDeUmFilme.put(ID,dateFormatFinal);
            } else {
                linhasIgnoradas.add(linha);
            }
        }
        reader.close();
        return new MoviesValid(linhasVálidas,linhasIgnoradas);
    }
}
