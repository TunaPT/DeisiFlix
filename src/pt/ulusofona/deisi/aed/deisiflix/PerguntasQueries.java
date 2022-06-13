package pt.ulusofona.deisi.aed.deisiflix;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

public class PerguntasQueries {
    //FUNÇÕES PERGUNTAR - QUERIES
    public static QueryResult countMoviesActorFunction(String pergunta, QueryResult query) {
        long initialTime = System.currentTimeMillis();
        String[] parts = pergunta.split("COUNT_MOVIES_ACTOR");
        String nomeAtor = parts[1].trim(); // Nome completo do autor
        if (Main.dicionario.get(nomeAtor) == null) {
            long finalTime = System.currentTimeMillis();
            query = new QueryResult("0",finalTime-initialTime);
        } else {
            long finalTime = System.currentTimeMillis();
            query = new QueryResult(Integer.toString(Main.dicionario.get(nomeAtor)),finalTime-initialTime);
        }
        return query;
    }

    public static QueryResult getMoviesActorYearFunction(String pergunta, QueryResult query) {
        long initialTime = System.currentTimeMillis();
        String[] parts = pergunta.split(" ");
        String part1 = parts[0].trim();
        String firstName = parts[1].trim();
        String secondName = parts[2].trim();
        String year = parts[3].trim();
        String outputFinal = "";
        if (Main.filmesAno.get(firstName + " " + secondName) != null) {
            ArrayList<Integer> arrayListIDsFilmes = Main.filmesAno.get(firstName + " " + secondName);
            ArrayList<InfoMoviesActorYear> TitulosAndDatas = new ArrayList<>();
            for (int i=0;i<arrayListIDsFilmes.size();i++) {
                String[] dateFormat = Main.pesquisaPorIDFilme.get(arrayListIDsFilmes.get(i))[1].split("-");
                if (year.equals(dateFormat[2])) {
                    TitulosAndDatas.add(new InfoMoviesActorYear(Main.pesquisaPorIDFilme.get(
                            arrayListIDsFilmes.get(i))[0],Main.pesquisaPorIDFilme.get(arrayListIDsFilmes.get(i))[1]));
                }
            }
            ArrayList<String> arrayListDatas = new ArrayList<>();
            HashMap<String,String> dadosFilmes = new HashMap<>();
            for (int i=0;i<TitulosAndDatas.size();i++) {
                String[] dateFormat = TitulosAndDatas.get(i).data.split("-");
                String dateFormatFinal = String.join("-", dateFormat[2], dateFormat[1], dateFormat[0]);
                dadosFilmes.put(dateFormatFinal,TitulosAndDatas.get(i).tituloFilme);
                arrayListDatas.add(dateFormatFinal);
            }
            Collections.sort(arrayListDatas,Collections.reverseOrder()); //ordena por data decrescente
            for (int i=0;i<TitulosAndDatas.size();i++) {
                if (i==TitulosAndDatas.size()-1) {
                    outputFinal = outputFinal +
                            dadosFilmes.get(arrayListDatas.get(i)) + " (" + arrayListDatas.get(i) + ")";
                } else {
                    outputFinal = outputFinal +
                            dadosFilmes.get(arrayListDatas.get(i)) + " (" + arrayListDatas.get(i) + ")\n";
                }
            }
        }
        long finalTime = System.currentTimeMillis();
        query = new QueryResult(outputFinal,finalTime-initialTime);
        return query;
    }

    public static QueryResult countMoviesWithActorsFunction(String pergunta, QueryResult query) {
        long initialTime = System.currentTimeMillis();
        String[] partes = pergunta.split("COUNT_MOVIES_WITH_ACTORS");
        String[] parts = partes[1].trim().split(";");
        int count = 0;
        if (parts.length > 1) {
            for (int k=0; k<Main.filmesAno.get(parts[0]).size(); k++) {
                if (Main.filmesAno.get(parts[1]).contains(Main.filmesAno.get(parts[0]).get(k))) {
                    if (Main.functionMoviesWithActors(parts.length, k, parts) == true) {
                        count++;
                    }
                }
            }
        }
        long finalTime = System.currentTimeMillis();
        query = new QueryResult(Integer.toString(count),finalTime-initialTime);
        return query;
    }

    public static QueryResult countActores3YearsFunction(String pergunta, QueryResult query) {
        long initialTime = System.currentTimeMillis();
        String[] parts = pergunta.split(" ");
        HashMap<String,ArrayList> insertAnoGetIDsActoresParticipantesEmFilmes = new HashMap<>();
        int count = 0;
        for (int i=1;i<parts.length;i++) { // Para o cumprimento 4
            ArrayList<Integer> idsFilmes = Main.actoresDiferentes.get(parts[i]);
            ArrayList<Integer> idsActores = new ArrayList<>();
            for (int k=0;k<idsFilmes.size();k++) {
                int idFilme = idsFilmes.get(k);
                ArrayList<Integer> idsParticipantesNoFilme = Main.actoresDiferentes2.get(idFilme);
                if (idsParticipantesNoFilme != null) {
                    for (int l=0;l<idsParticipantesNoFilme.size();l++) {
                        int idActorParticipante = idsParticipantesNoFilme.get(l);
                        if (idsActores.contains(idActorParticipante) == false) {
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
        return query;
    }

    public static QueryResult topMoviesWithGenderBiasFunction(String pergunta, QueryResult query) {
        long initialTime = System.currentTimeMillis();
        String[] partes = pergunta.split(" ");
        String nFilmes = partes[1].trim();
        String ano = partes[2].trim();
        String output = "";
        ArrayList<Integer> listIDsFilmesDoAnoInserido = Main.actoresDiferentes.get(ano);

        ArrayList<String> outputFinal = new ArrayList<>();
        for (int k=0;k<listIDsFilmesDoAnoInserido.size();k++) {
            int idFilme = listIDsFilmesDoAnoInserido.get(k);
            ArrayList<Integer> listIDsParticipantesFilme = Main.apenasActoresDeUmFilme.get(idFilme);
            if (listIDsParticipantesFilme != null) {
                if (listIDsParticipantesFilme.size() > 10) {
                    int countF = 0;
                    int countM = 0;
                    for (int l=0;l<listIDsParticipantesFilme.size(); l++) {
                        int idActor = listIDsParticipantesFilme.get(l);
                        String generoActor = Main.insertIDActorGetGenero.get(idActor);
                        if (generoActor.equals("F")) {
                            countF++;
                        } else if (generoActor.equals("M")) {
                            countM++;
                        }
                        if (Main.insertIDFilmeGetListGeneros.get(idFilme) == null) {
                            ArrayList<String> arrayList = new ArrayList<>();
                            arrayList.add(generoActor);
                            Main.insertIDFilmeGetListGeneros.put(idFilme, arrayList);
                        } else {
                            ArrayList<String> arrayList = Main.insertIDFilmeGetListGeneros.get(idFilme);
                            arrayList.add(generoActor);
                            Main.insertIDFilmeGetListGeneros.put(idFilme, arrayList);
                        }
                    }
                    if (Main.insertIDFilmeGetListGeneros.get(idFilme) != null)  {
                        if (countF>countM) {
                            int percentagem = (int) Math.round(countF*100.0/(countF+countM));
                            outputFinal.add(Main.tituloDeUmFilme.get(idFilme) + ":F:" + percentagem);
                        } else {
                            int percentagem = (int) Math.round(countM*100.0/(countF+countM));
                            outputFinal.add(Main.tituloDeUmFilme.get(idFilme) + ":M:" + percentagem);
                        }
                    }
                }
            }
        }
        for (int i=0;i<Integer.parseInt(nFilmes);i++) {
            ArrayList<String> last = Main.quickSort(outputFinal);
            output = output + last.get(i) + "\n";
        }
        long finalTime = System.currentTimeMillis();
        query = new QueryResult(output,finalTime-initialTime);
        return query;
    }

    public static QueryResult getRecentTitlesSameAvgVotesOneSharedActorFunction(String pergunta, QueryResult query) {
        long initialTime = System.currentTimeMillis();
        String[] partes = pergunta.split("GET_RECENT_TITLES_SAME_AVG_VOTES_ONE_SHARED_ACTOR");
        String idFilme = partes[1].trim();
        String output = "";
        if (Main.idFilmeMediaVotos.get(Integer.parseInt(idFilme)) != null) {
            double votacaoMediaFilmeX = Main.idFilmeMediaVotos.get(Integer.parseInt(idFilme));
            ArrayList<Integer> idActoresFilmeX = Main.actoresDeUmFilme.get(Integer.parseInt(idFilme));
            ArrayList<Integer> IDFilmesComMesmaMediaDoFilmeX = Main.votacaoMediaFilme.get(votacaoMediaFilmeX);
            for (int i = 0; i < IDFilmesComMesmaMediaDoFilmeX.size(); i++) {
                if (Main.dataDeUmFilme.get(IDFilmesComMesmaMediaDoFilmeX.get(i)).compareTo
                        (Main.dataDeUmFilme.get(Integer.parseInt(idFilme))) > 0) {
                    if (Main.actoresDeUmFilme.get(IDFilmesComMesmaMediaDoFilmeX.get(i)) != null) {
                        for (int k=0; k<Main.actoresDeUmFilme.get(IDFilmesComMesmaMediaDoFilmeX.get(i)).size(); k++) {
                            if (idActoresFilmeX.contains
                                    (Main.actoresDeUmFilme.get(IDFilmesComMesmaMediaDoFilmeX.get(i)).get(k))) {
                                if (output.equals("")) {
                                    output = Main.tituloDeUmFilme.get(IDFilmesComMesmaMediaDoFilmeX.get(i));
                                } else {
                                    output = output + "||" +
                                            Main.tituloDeUmFilme.get(IDFilmesComMesmaMediaDoFilmeX.get(i));
                                }
                            }
                        }
                    }
                }
            }
        }
        long finalTime = System.currentTimeMillis();
        query = new QueryResult(output,finalTime-initialTime);
        return query;
    }

    public static QueryResult getTopNYearsBestAvgVotesFunction(String pergunta, QueryResult query) {
        long initialTime = System.currentTimeMillis();
        String[] partes = pergunta.split("GET_TOP_N_YEARS_BEST_AVG_VOTES");
        String topValue = partes[1].trim();

        String[] anoFilmesArray = Main.anoFilmes.toArray(new String[Main.anoFilmes.size()]);
        for (int i=0; i<anoFilmesArray.length;i++) {
            String ano = anoFilmesArray[i];
            ArrayList<Integer> idsFilmes = Main.actoresDiferentes.get(ano);
            double somaVotosAno = 0.0f;
            for (int k=0;k<idsFilmes.size();k++) {
                int idFilme = idsFilmes.get(k);
                double mediaVotos = Main.idFilmeMediaVotos.get(idFilme);
                somaVotosAno = somaVotosAno + mediaVotos;
            }
            double mediaAno = somaVotosAno/idsFilmes.size();
            Main.anoPontuacao.put(anoFilmesArray[i], mediaAno);
        }

        String stringQuery = "";
        String ano = anoFilmesArray[0];
        for (int i=0; i<Integer.parseInt(topValue);i++) {
            double maior = Main.anoPontuacao.get(anoFilmesArray[0]);
            for (int k=1; k<anoFilmesArray.length;k++) {
                if (Main.anoPontuacao.get(anoFilmesArray[k]) != null) {
                    if (Main.anoPontuacao.get(anoFilmesArray[k]) > maior) {
                        maior = Main.anoPontuacao.get(anoFilmesArray[k]);
                        ano = anoFilmesArray[k];
                    }
                }
            }
            if (i+1 != Integer.parseInt(topValue)) {
                stringQuery = stringQuery + ano + ":" + Math.round(Main.anoPontuacao.get(ano) * 100)/100.0 + "\n";
            } else {
                stringQuery = stringQuery + ano + ":" + Math.round(Main.anoPontuacao.get(ano) * 100)/100.00 + "\n";
            }
            Main.anoPontuacao.remove(ano);
        }
        long finalTime = System.currentTimeMillis();
        query = new QueryResult(stringQuery,finalTime-initialTime);
        return query;
    }

    public static QueryResult distanceBetweenActorsFunction(String pergunta, QueryResult query) {
        long initialTime = System.currentTimeMillis();
        long finalTime = System.currentTimeMillis();
        query = new QueryResult("s",finalTime-initialTime);
        return query;
    }

    public static QueryResult getTopNMoviesRatioFunction(String pergunta, QueryResult query) {
        long initialTime = System.currentTimeMillis();
        String[] partes = pergunta.split(" ");
        String topNFilmes = partes[1].trim();
        String ano = partes[2].trim();
        String output = "";

        ArrayList<Integer> idFilmes = Main.actoresDiferentes.get(ano);
        HashMap<Integer,Double> insertIDFilmeGetRacio = new HashMap<>();
        ArrayList<RacioFilme> dados = new ArrayList<>();

        for (int i=0;i<idFilmes.size();i++) {
            int idFilme = idFilmes.get(i);
            ArrayList<Integer> listActoresFilme = Main.apenasActoresDeUmFilme.get(idFilme);
            if (listActoresFilme != null) {
                int numeroActoresFilme = listActoresFilme.size();
                double mediaFilme = Main.idFilmeMediaVotos.get(idFilme);
                double racioFilme = mediaFilme / numeroActoresFilme;
                String tituloFilme = Main.tituloDeUmFilme.get(idFilme);
                insertIDFilmeGetRacio.put(idFilme, racioFilme);
                dados.add(new RacioFilme(tituloFilme, racioFilme));
            }
        }

        Main.quickSortPorRacio(dados);
        boolean primeiroOutput=true;

        for (int i=0, posicao = dados.size() - 1;i<Integer.parseInt(topNFilmes) && i<dados.size();i++,posicao--) {
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
        return query;
    }

    public static QueryResult top6DirectorsWithinFamilyFunction(String pergunta, QueryResult query) {
        long initialTime = System.currentTimeMillis();
        long finalTime = System.currentTimeMillis();
        query = new QueryResult("s",finalTime-initialTime);
        return query;
    }

    public static QueryResult getTopActorYearFunction(String pergunta, QueryResult query) {
        long initialTime = System.currentTimeMillis();
        String[] partes = pergunta.split("GET_TOP_ACTOR_YEAR");
        String ano = partes[1].trim();
        ArrayList<Integer> arrayList = Main.topActorYear.get(ano);
        ArrayList<Integer> arrayListIDsPessoa = new ArrayList<>();
        for (int i=0;i<arrayList.size();i++) {
            if (Main.actoresDiferentes2.get(arrayList.get(i)) != null) {
                for (int k = 0; k < Main.actoresDiferentes2.get(arrayList.get(i)).size(); k++) {
                    arrayListIDsPessoa.add((Integer) Main.actoresDiferentes2.get(arrayList.get(i)).get(k));
                }
            }
            Main.actoresDiferentes2.get(arrayList.get(i)); // Arraylist
        }
        int maior = Collections.frequency(arrayListIDsPessoa,arrayListIDsPessoa.get(0));
        int id = arrayListIDsPessoa.get(0);
        for (int i=1;i<arrayListIDsPessoa.size();i++) {
            if (Collections.frequency(arrayListIDsPessoa,arrayListIDsPessoa.get(i)) > maior) {
                id = arrayListIDsPessoa.get(i);
                maior = Collections.frequency(arrayListIDsPessoa,arrayListIDsPessoa.get(i));
            }
        }
        String nome = Main.getPersonNameById.get(id);
        long finalTime = System.currentTimeMillis();
        query = new QueryResult(nome + ";" + Integer.toString(maior),finalTime-initialTime);
        return query;
    }

    public static QueryResult insertActorFunction(String pergunta, QueryResult query) {
        long initialTime = System.currentTimeMillis();
        String[] partes = pergunta.split("INSERT_ACTOR");
        String[] parts = partes[1].trim().split(";");
        int IDActor = Integer.parseInt(parts[0].trim());
        String NomeActor = parts[1].trim();
        String GeneroActor = parts[2].trim();
        int IDFilme = Integer.parseInt(parts[3].trim());
        String output = "";
        if (Main.todosFilmes.get(IDFilme) == null || Main.insertIDActorGetGenero.get(IDActor) != null) {
            output = "Erro";
        } else {
            if (GeneroActor.equals("M")) {
                Main.todosFilmes.get(IDFilme).numActoresMasculinos =
                        Main.todosFilmes.get(IDFilme).numActoresMasculinos + 1;
            } else if (GeneroActor.equals("F")) {
                Main.todosFilmes.get(IDFilme).numActoresFemininos =
                        Main.todosFilmes.get(IDFilme).numActoresFemininos + 1;
            }
            ArrayList<Integer> idFilmesPessoa = new ArrayList<>();
            idFilmesPessoa.add(IDFilme);
            if (Main.filmesAno.get(NomeActor) == null) {
                Main.filmesAno.put(NomeActor, idFilmesPessoa);
            } else {
                ArrayList<Integer> idsFilmes = Main.filmesAno.get(NomeActor);
                idsFilmes.add(IDFilme);
                Main.filmesAno.put(NomeActor, idsFilmes);
            }
            output = "OK";
        }
        long finalTime = System.currentTimeMillis();
        query = new QueryResult(output,finalTime-initialTime);
        return query;
    }

    public static QueryResult removeActorFunction(String pergunta, QueryResult query) {
        long initialTime = System.currentTimeMillis();
        String[] partes = pergunta.split(" ");
        int IDActor = Integer.parseInt(partes[1]);
        String output = "";
        if (Main.filmesActor.get(IDActor) != null) {
            output = "OK";
            ArrayList<Integer> listIDsFilmes = Main.filmesActor.get(IDActor); // Devolve lista de filmes do actor
            Main.filmesActor.remove(IDActor); //ativei esta
            String GeneroActor = Main.insertIDActorGetGenero.get(IDActor);
            Main.insertIDActorGetGenero.remove(IDActor);
            ArrayList<Integer> idsFilmesJaInseridos = new ArrayList<>();
            for (int i=0;i<listIDsFilmes.size();i++) {
                int idFilme = listIDsFilmes.get(i);
                if (idsFilmesJaInseridos.contains(idFilme) == false) {
                    if (GeneroActor.equals("M")) {
                        Main.todosFilmes.get(idFilme).numActoresMasculinos =
                                Main.todosFilmes.get(idFilme).numActoresMasculinos - 1;
                    }
                    if (GeneroActor.equals("F")) {
                        Main.todosFilmes.get(idFilme).numActoresFemininos =
                                Main.todosFilmes.get(idFilme).numActoresFemininos - 1;
                    }
                    idsFilmesJaInseridos.add(idFilme);
                }
            }
            String nomeActor = Main.getPersonNameById.get(IDActor);
            Main.dicionario.remove(nomeActor); //necessária
            Main.filmesAno.remove(nomeActor);
            Main.getPersonNameById.remove(IDActor);
        } else {
            output = "Erro";
        }
        long finalTime = System.currentTimeMillis();
        query = new QueryResult(output,finalTime-initialTime);
        return query;
    }

    public static QueryResult getDuplicateLinesYearFunction(String pergunta, QueryResult query) {
        long initialTime = System.currentTimeMillis();
        long finalTime = System.currentTimeMillis();
        query = new QueryResult("s",finalTime-initialTime);
        return query;
    }

    public static QueryResult getTop3MoviesGenreYearFunction(String pergunta, QueryResult query) {
        long initialTime = System.currentTimeMillis();
        String[] partes = pergunta.split(" ");
        String ano = partes[1];
        HashMap<String,Integer> listGenresAno = new HashMap<>();
        ArrayList<Integer> idFilmes = Main.actoresDiferentes.get(ano);
        HashSet<String> generosSemRepetidos = new HashSet<>();
        String output = "";
        if (idFilmes != null) {
            for (int i=0; i<idFilmes.size(); i++) {
                int idFilme = idFilmes.get(i);
                ArrayList<String> listGenreFilme = Main.insertIDFilmeGetGenerosFilme.get(idFilme);
                if (listGenreFilme != null) {
                    for (int k = 0; k < listGenreFilme.size(); k++) {
                        String genreFilme = listGenreFilme.get(k);
                        generosSemRepetidos.add(genreFilme);
                        if (listGenresAno.get(genreFilme) == null) {
                            listGenresAno.put(genreFilme, 1);
                        } else {
                            listGenresAno.put(genreFilme, listGenresAno.get(genreFilme) + 1);
                        }
                    }
                }
            }
            int index = 3;
            if (generosSemRepetidos.size() < 3) {
                index = generosSemRepetidos.size();
            }
            for (int i = 0; i < index; i++) {
                int maiorNumVezes = 0;
                int posicao = 0;
                if (generosSemRepetidos.size() > 0) {
                    for (int k = 0; k < generosSemRepetidos.size(); k++) {
                        int numVezes = listGenresAno.get(generosSemRepetidos.toArray()[k]);
                        if (numVezes > maiorNumVezes) {
                            maiorNumVezes = numVezes;
                            posicao = k;
                        }
                    }
                    output = output + generosSemRepetidos.toArray()[posicao] +
                            " está presente " + maiorNumVezes + " vez";
                    if (maiorNumVezes != 1) {
                        output = output + "es";
                    }
                    if (i + 1 != index) {
                        output = output + "\n";
                    }
                    generosSemRepetidos.remove(generosSemRepetidos.toArray()[posicao]);
                }
            }
        } else {
            output = "Não existem filmes neste ano";
        }
        long finalTime = System.currentTimeMillis();
        query = new QueryResult(output,finalTime-initialTime);
        return query;
    }
}
