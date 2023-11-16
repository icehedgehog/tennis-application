package co.latelier.TennisApplication.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @GetMapping("/")
    public String home() {
        return "<h2>API Usage</h2>" +
                "<p>The application exposes several RESTful endpoints. Here are some examples of usage:</p>" +
                "<ul>" +
                "<li>Get all players: <code>/api/players</code></li>" +
                "<li>Get a player by ID: <code>/api/players/{id}</code></li>" +
                "<li>Get statistics: <code>/api/statistics</code></li>" +
                "</ul>" +
                "<p>The last endpoint returns statistics such as the country with the highest win ratio, the average BMI of players, and the median height of players.</p>";
    }
}
