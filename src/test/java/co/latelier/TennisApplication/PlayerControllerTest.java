package co.latelier.TennisApplication;

import co.latelier.TennisApplication.controllers.PlayerController;
import co.latelier.TennisApplication.models.Country;
import co.latelier.TennisApplication.models.Data;
import co.latelier.TennisApplication.models.Player;
import co.latelier.TennisApplication.utils.JsonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PlayerController.class)
public class PlayerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JsonService jsonService;

    @Test
    public void shouldReturnPlayersList() throws Exception {
        List<Player> players = new ArrayList<>();

        Country country1 = new Country();
        country1.setPicture("https://data.latelier.co/training/tennis_stats/resources/Serbie.png");
        country1.setCode("SRB");

        Data data1 = new Data();
        data1.setRank(2);
        data1.setPoints(2542);
        data1.setWeight(80000);
        data1.setHeight(188);
        data1.setAge(31);
        data1.setLast(Arrays.asList(1, 1, 1, 1, 1));

        Player player1 = new Player();
        player1.setId(52L);
        player1.setFirstname("Novak");
        player1.setLastname("Djokovic");
        player1.setShortname("N.DJO");
        player1.setSex("M");
        player1.setCountry(country1);
        player1.setPicture("https://data.latelier.co/training/tennis_stats/resources/Djokovic.png");
        player1.setData(data1);

        Country country2 = new Country();
        country2.setPicture("https://data.latelier.co/training/tennis_stats/resources/Espagne.pn");
        country2.setCode("ESP");

        Data data2 = new Data();
        data2.setRank(1);
        data2.setPoints(1982);
        data2.setWeight(85000);
        data2.setHeight(185);
        data2.setAge(33);
        data2.setLast(Arrays.asList(1, 0, 0, 0, 1));

        Player player2 = new Player();
        player2.setId(17L);
        player2.setFirstname("Rafael");
        player2.setLastname("Nadal");
        player2.setShortname("R.NAD");
        player2.setSex("M");
        player2.setCountry(country2);
        player2.setPicture("https://data.latelier.co/training/tennis_stats/resources/Nadal.pn");
        player2.setData(data2);

        players.add(player1);
        players.add(player2);

        given(jsonService.readPlayersFromJson()).willReturn(players);

        mockMvc.perform(get("/api/players"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(17)))
                .andExpect(jsonPath("$[0].firstname", is("Rafael")))
                .andExpect(jsonPath("$[0].lastname", is("Nadal")))
                .andExpect(jsonPath("$[1].id", is(52)))
                .andExpect(jsonPath("$[1].firstname", is("Novak")))
                .andExpect(jsonPath("$[1].lastname", is("Djokovic")))
                .andDo(print());
    }

    @Test
    public void shouldReturnValidContentTypeAndStructure() throws Exception {
        List<Player> players = new ArrayList<>();
        Country country = new Country();
        country.setPicture("https://data.latelier.co/training/tennis_stats/resources/Serbie.png");
        country.setCode("SRB");

        Data data = new Data();
        data.setRank(2);
        data.setPoints(2542);
        data.setWeight(80000);
        data.setHeight(188);
        data.setAge(31);
        data.setLast(Arrays.asList(1, 1, 1, 1, 1));

        Player player = new Player();
        player.setId(52L);
        player.setFirstname("Novak");
        player.setLastname("Djokovic");
        player.setShortname("N.DJO");
        player.setSex("M");
        player.setCountry(country);
        player.setPicture("https://data.latelier.co/training/tennis_stats/resources/Djokovic.png");
        player.setData(data);
        players.add(player);

        given(jsonService.readPlayersFromJson()).willReturn(players);

        mockMvc.perform(get("/api/players"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]").isNotEmpty())
                .andExpect(jsonPath("$[0].id").value(player.getId()))
                .andExpect(jsonPath("$[0].firstname").value(player.getFirstname()))
                .andExpect(jsonPath("$[0].lastname").value(player.getLastname()))
                .andDo(print());
    }

    @Test
    public void shouldReturnInternalServerErrorWhenJsonReadFails() throws Exception {
        given(jsonService.readPlayersFromJson()).willThrow(new IOException());

        mockMvc.perform(get("/api/players"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error reading players data"))
                .andDo(print());
    }

    @Test
    public void shouldReturnEmptyListWhenNoPlayersFound() throws Exception {
        given(jsonService.readPlayersFromJson()).willReturn(new ArrayList<>());

        mockMvc.perform(get("/api/players"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)))
                .andDo(print());
    }

    @Test
    public void shouldReturnPlayerForValidId() throws Exception {
        Country country = new Country();
        country.setPicture("https://data.latelier.co/training/tennis_stats/resources/Serbie.png");
        country.setCode("SRB");

        Data data = new Data();
        data.setRank(2);
        data.setPoints(2542);
        data.setWeight(80000);
        data.setHeight(188);
        data.setAge(31);
        data.setLast(Arrays.asList(1, 1, 1, 1, 1));

        Player player = new Player();
        player.setId(52L);
        player.setFirstname("Novak");
        player.setLastname("Djokovic");
        player.setShortname("N.DJO");
        player.setSex("M");
        player.setCountry(country);
        player.setPicture("https://data.latelier.co/training/tennis_stats/resources/Djokovic.png");
        player.setData(data);

        given(jsonService.getPlayerById(52L)).willReturn(player);

        mockMvc.perform(get("/api/players/{id}", 52))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(52))
                .andDo(print());
    }

    @Test
    public void shouldReturnNotFoundForInvalidId() throws Exception {
        given(jsonService.getPlayerById(anyLong())).willReturn(null);

        mockMvc.perform(get("/api/players/{id}", 999L))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void shouldReturnInternalServerErrorOnException() throws Exception {
        given(jsonService.getPlayerById(anyLong())).willThrow(IOException.class);

        mockMvc.perform(get("/api/players/{id}", 1L))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error reading players data"))
                .andDo(print());
    }

    @Test
    public void shouldReturnStatistics() throws Exception {
        double averageIMC = 23.5;
        String countryWithHighestWinRatio = "SRB";
        Integer medianHeight = 180;

        given(jsonService.calculateAverageIMC()).willReturn(averageIMC);
        given(jsonService.countryWithHighestWinRatio()).willReturn(countryWithHighestWinRatio);
        given(jsonService.calculateMedianHeight()).willReturn(medianHeight);

        mockMvc.perform(get("/api/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.averageIMC").value(averageIMC))
                .andExpect(jsonPath("$.countryWithHighestWinRatio").value(countryWithHighestWinRatio))
                .andExpect(jsonPath("$.medianHeight").value(medianHeight))
                .andDo(print());
    }


}
