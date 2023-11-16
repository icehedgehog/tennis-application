package co.latelier.TennisApplication.controllers;

import co.latelier.TennisApplication.models.Player;
import co.latelier.TennisApplication.utils.JsonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PlayerController {
    private final JsonService jsonService;

    public PlayerController(JsonService jsonService) {
        this.jsonService = jsonService;
    }

    @GetMapping("/players")
    public ResponseEntity<?> getPlayers() {
        try {
            List<Player> players = jsonService.readPlayersFromJson();
            players.sort(Comparator.comparingInt(p -> p.getData().getRank()));
            return ResponseEntity.ok(players);
        } catch (IOException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error reading players data");
        }
    }

    @GetMapping("/players/{id}")
    public ResponseEntity<?> getPlayerById(@PathVariable Long id) {
        try {
            Player player = jsonService.getPlayerById(id);
            if (player == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(player);
        } catch (IOException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error reading players data");
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<?> getStatistics() {
        try {
            double averageIMC = jsonService.calculateAverageIMC();
            String countryWithHighestWinRatio = jsonService.countryWithHighestWinRatio();
            Integer medianHeight = jsonService.calculateMedianHeight();

            Statistics stats = new Statistics(countryWithHighestWinRatio, averageIMC, medianHeight);
            return ResponseEntity.ok(stats);
        } catch (IOException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error calculating statistics");
        }
    }

    private static class Statistics {
        private double averageIMC;
        private String countryWithHighestWinRatio;
        private Integer medianHeight;

        public Statistics(String countryWithHighestWinRatio, double averageIMC, Integer medianHeight) {
            this.countryWithHighestWinRatio = countryWithHighestWinRatio;
            this.averageIMC = averageIMC;
            this.medianHeight = medianHeight;
        }

        public String getCountryWithHighestWinRatio() {
            return countryWithHighestWinRatio;
        }

        public double getAverageIMC() {
            return averageIMC;
        }

        public Integer getMedianHeight() {
            return medianHeight;
        }

        public void setCountryWithHighestWinRatio(String countryWithHighestWinRatio) {
            this.countryWithHighestWinRatio = countryWithHighestWinRatio;
        }

        public void setAverageIMC(double averageIMC) {
            this.averageIMC = averageIMC;
        }

        public void setMedianHeight(Integer medianHeight) {
            this.medianHeight = medianHeight;
        }
    }
}
