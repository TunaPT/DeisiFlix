package pt.ulusofona.deisi.aed.deisiflix;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestFilme {
    @Test
    public void testString() {
        Filme Filme = new Filme(56429, "Up from the Depths", 1, 1, "01-06-1979", 3.2, 2, 4, 6);
        String resultadoInserido = Filme.toString();
        String resultadoEsperado = "56429 | Up from the Depths | 1979-06-01 | 2 | 3.2 | 1 | 1 | 4 | 6";
        assertEquals(resultadoInserido, resultadoEsperado);

        Filme = new Filme(151687, "Charlie Muffin", 3, 2, "11-12-1979", 5.3, 8, 2, 2);
        resultadoInserido = Filme.toString();
        resultadoEsperado = "151687 | Charlie Muffin | 1979-12-11 | 8 | 5.3 | 2 | 3 | 2 | 2";
        assertEquals(resultadoInserido, resultadoEsperado);
    }

    @Test
    public void testCountMoviesActorQuery() {
        QueryResult result = Main.perguntar("COUNT_MOVIES_ACTOR Tom Cruise");
        assertEquals("47",result.valor);
    }

    @Test
    public void testGetMoviesActorYearQuery() {
        QueryResult result = Main.perguntar("GET_MOVIES_ACTOR_YEAR Tom Cruise 2000");
        assertEquals("Mission: Impossible II (2000-05-24)",result.valor);
    }

    @Test
    public void testCountMoviesWithActorsQuery() {
        QueryResult result = Main.perguntar("COUNT_MOVIES_WITH_ACTORS");
        assertEquals("Mission: Impossible II (2000-05-24)",result.valor);
    }

    @Test
    public void testCountActors3YearsQuery() {
        QueryResult result = Main.perguntar("COUNT_ACTORS_3_YEARS");
        assertEquals("Mission: Impossible II (2000-05-24)",result.valor);
    }

    @Test
    public void testTopMoviesWithGenderBiasQuery() {
        QueryResult result = Main.perguntar("TOP_MOVIES_WITH_GENDER_BIAS");
        assertEquals("Mission: Impossible II (2000-05-24)",result.valor);
    }

    @Test
    public void testGetRecentTitleSameAvgVotesOneSharedActorQuery() {
        QueryResult result = Main.perguntar("GET_RECENT_TITLES_SAME_AVG_VOTES_ONE_SHARED_ACTOR");
        assertEquals("Mission: Impossible II (2000-05-24)",result.valor);
    }

    @Test
    public void testGetTopNYearsBestAvgVotesQuery() {
        QueryResult result = Main.perguntar("GET_TOP_N_YEARS_BEST_AVG_VOTES");
        assertEquals("Mission: Impossible II (2000-05-24)",result.valor);
    }

    @Test
    public void testGetTopNMoviesRatioQuery() {
        QueryResult result = Main.perguntar("GET_TOP_N_MOVIES_RATIO");
        assertEquals("Mission: Impossible II (2000-05-24)",result.valor);
    }

    @Test
    public void testGetTopActorYearQuery() {
        QueryResult result = Main.perguntar("GET_TOP_ACTOR_YEAR");
        assertEquals("Mission: Impossible II (2000-05-24)",result.valor);
    }

    @Test
    public void testInsertActorQuery() {
        QueryResult result = Main.perguntar("INSERT_ACTOR");
        assertEquals("Mission: Impossible II (2000-05-24)",result.valor);
    }

    @Test
    public void testRemoveActorQuery() {
        QueryResult result = Main.perguntar("REMOVE_ACTOR");
        assertEquals("Mission: Impossible II (2000-05-24)",result.valor);
    }

    @Test
    public void testCreativeQuery() {
        QueryResult result = Main.perguntar("GET_TOP3_MOVIES_GENRE_YEAR 2000");
        assertEquals("Drama está presente 388 vezes\n" +
                "Comedy está presente 250 vezes\n" +
                "Romance está presente 152 vezes", result.valor);

        result = Main.perguntar("GET_TOP3_MOVIES_GENRE_YEAR 1898");
        assertEquals("Fantasy está presente 7 vezes\n" +
                "Comedy está presente 3 vezes\n" +
                "Documentary está presente 1 vez", result.valor);
    }
}
