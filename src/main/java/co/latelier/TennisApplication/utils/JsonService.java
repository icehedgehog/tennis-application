package co.latelier.TennisApplication.utils;

import co.latelier.TennisApplication.models.Player;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class JsonService {
    private List<Player> cachedPlayers;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final ClassPathResource resource;

    public JsonService(ClassPathResource resource) {
        this.resource = resource;
    }

    public List<Player> readPlayersFromJson() throws IOException {
        if (cachedPlayers == null) {
            InputStream inputStream = resource.getInputStream();
            PlayersWrapper wrapper = objectMapper.readValue(inputStream, PlayersWrapper.class);
            cachedPlayers = Arrays.asList(wrapper.getPlayers());
        }
        return cachedPlayers;
    }

    public Player getPlayerById(Long id) throws IOException {
        List<Player> players = readPlayersFromJson();
        return players.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public String countryWithHighestWinRatio() throws IOException {
        List<Player> players = readPlayersFromJson();

        return players.stream()
                .collect(Collectors.groupingBy(
                        player -> player.getCountry().getCode(),
                        Collectors.averagingDouble(player -> calculateWinRatio(player.getData().getLast()))
                ))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    private double calculateWinRatio(List<Integer> lastGames) {
        if (lastGames == null || lastGames.isEmpty()) return 0.0;
        return lastGames.stream().mapToDouble(Integer::doubleValue).average().orElse(0.0);
    }

    public double calculateAverageIMC() throws IOException {
        List<Player> players = readPlayersFromJson();

        DoubleSummaryStatistics stats = players.stream()
                .filter(player -> player.getData().getWeight() != null && player.getData().getHeight() != null)
                .mapToDouble(player -> calculateIMC(player.getData().getWeight(), player.getData().getHeight()))
                .summaryStatistics();

        return stats.getAverage();
    }

    private double calculateIMC(Integer weight, Integer height) {
        if (height == null || height == 0) return 0;
        double heightInMeters = height / 100.0;
        double weightInKilograms = weight / 1000.0;
        return weightInKilograms / (heightInMeters * heightInMeters);
    }

    public Integer calculateMedianHeight() throws IOException {
        List<Player> players = readPlayersFromJson();
        List<Integer> heights = players.stream()
                .map(player -> player.getData().getHeight())
                .filter(Objects::nonNull)
                .sorted()
                .collect(Collectors.toList());

        if (heights.isEmpty()) return null;

        int middle = heights.size() / 2;
        if (heights.size() % 2 == 0) {
            return (heights.get(middle - 1) + heights.get(middle)) / 2;
        } else {
            return heights.get(middle);
        }
    }

    private static class PlayersWrapper {
        private Player[] players;

        public Player[] getPlayers() {
            return players;
        }
    }
}
