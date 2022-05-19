package pt.ulusofona.deisi.aed.deisiflix;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestFilme {
    @Test
    public void testString() {
        Filme Filme = new Filme(603, "The Matrix", null, null, null, "30-03-1999", 63000000, 7.9f, 9079);
        String resultadoInserido = Filme.toString();
        String resultadoEsperado = "603 | The Matrix | 1999-03-30 | 9079 | 7.9";
        assertEquals(resultadoInserido, resultadoEsperado);
    }

    /*@Test
    public void testCreativeQuery() {
        String result = Main.perguntar("GET_STUFF 2000");
        assertEquals("34 blabla\n56 bleble", result);
        result = Main.perguntar("GET_STUFF 1995");
        assertEquals("32 olaola\n21 oleole\n89 olioli", result);
    }*/
}
