package co.latelier.TennisApplication;

import co.latelier.TennisApplication.models.Player;
import co.latelier.TennisApplication.utils.JsonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class JsonServiceTest {
    @Mock
    private ClassPathResource resource;

    private JsonService jsonService;

    @BeforeEach
    void setUp() {
        resource = mock(ClassPathResource.class);
        jsonService = new JsonService(resource);
    }

    @Test
    void readPlayersFromJsonTest() throws IOException {
        String json = "{\"players\": [" +
                "{" +
                "\"id\": 52, " +
                "\"firstname\": \"Novak\", " +
                "\"lastname\": \"Djokovic\", " +
                "\"shortname\": \"N.DJO\", " +
                "\"sex\": \"M\", " +
                "\"country\": {\"picture\": \"https://data.latelier.co/training/tennis_stats/resources/Serbie.png\", \"code\": \"SRB\"}, " +
                "\"picture\": \"https://data.latelier.co/training/tennis_stats/resources/Djokovic.png\", " +
                "\"data\": {" +
                "\"rank\": 2, " +
                "\"points\": 2542, " +
                "\"weight\": 80000, " +
                "\"height\": 188, " +
                "\"age\": 31, " +
                "\"last\": [1, 1, 1, 1, 1]" +
                "}" +
                "}" +
                "]}";
        InputStream inputStream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));

        when(resource.getInputStream()).thenReturn(inputStream);

        List<Player> players = jsonService.readPlayersFromJson();

        assertNotNull(players);
        assertFalse(players.isEmpty());
        assertEquals(1, players.size());
        assertEquals(52, players.get(0).getId());
        assertEquals("Novak", players.get(0).getFirstname());
        assertEquals("Djokovic", players.get(0).getLastname());
        assertEquals("N.DJO", players.get(0).getShortname());
        assertEquals("M", players.get(0).getSex());
        assertEquals("SRB", players.get(0).getCountry().getCode());
        assertEquals("https://data.latelier.co/training/tennis_stats/resources/Serbie.png", players.get(0).getCountry().getPicture());
        assertEquals(2, players.get(0).getData().getRank());
        assertEquals(2542, players.get(0).getData().getPoints());
        assertEquals(80000, players.get(0).getData().getWeight());
        assertEquals(188, players.get(0).getData().getHeight());
        assertEquals(31, players.get(0).getData().getAge());
        assertArrayEquals(new int[]{1, 1, 1, 1, 1}, players.get(0).getData().getLast().stream().mapToInt(i -> i).toArray());
        assertEquals("https://data.latelier.co/training/tennis_stats/resources/Djokovic.png", players.get(0).getPicture());

        verify(resource, times(1)).getInputStream();
    }

    @Test
    void getPlayerByIdTest() throws IOException {
        String json = "{\"players\": [{\"id\": 52, \"firstname\": \"Novak\", \"lastname\": \"Djokovic\"}]}";
        InputStream inputStream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        when(resource.getInputStream()).thenReturn(inputStream);

        Player player = jsonService.getPlayerById(52L);

        assertNotNull(player);
        assertEquals("Novak", player.getFirstname());
        assertEquals("Djokovic", player.getLastname());
    }

    @Test
    void countryWithHighestWinRatioTest() throws IOException {
        String json = "{\"players\": [{\"country\": {\"code\": \"SRB\"}, \"data\": {\"last\": [1, 1, 1]}}]}";
        InputStream inputStream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        when(resource.getInputStream()).thenReturn(inputStream);

        String country = jsonService.countryWithHighestWinRatio();

        assertEquals("SRB", country);
    }

    @Test
    void calculateAverageIMCTest() throws IOException {
        String json = "{\"players\": [{\"data\": {\"weight\": 80000, \"height\": 188}}]}";
        InputStream inputStream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        when(resource.getInputStream()).thenReturn(inputStream);

        double averageIMC = jsonService.calculateAverageIMC();

        assertTrue(averageIMC > 0);
    }

    @Test
    void calculateMedianHeightTest() throws IOException {
        String json = "{\"players\": [{\"data\": {\"height\": 188}}, {\"data\": {\"height\": 190}}]}";
        InputStream inputStream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        when(resource.getInputStream()).thenReturn(inputStream);

        Integer medianHeight = jsonService.calculateMedianHeight();

        assertNotNull(medianHeight);
        assertEquals(189, medianHeight.intValue());
    }


}
