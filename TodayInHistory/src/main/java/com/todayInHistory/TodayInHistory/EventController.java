package com.todayInHistory.TodayInHistory;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


@Controller
public class EventController {
    @Autowired
    EventRepository eventRepository;

    @GetMapping("/")
    public String today(Model model) throws IOException, InterruptedException {
        Event event = saveTodaysEvent();
        model.addAttribute("event", event);
        return "index";
    }

    public String getTodaysEvent() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://today-in-history.p.rapidapi.com/thisday"))
                .header("X-RapidAPI-Key", "84f082a22bmsh3708c8699bf0e9cp1c0edajsnbaeb40464f2b")
                .header("X-RapidAPI-Host", "today-in-history.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        return response.body();
    }

    public Event saveTodaysEvent() throws IOException, InterruptedException {
        Gson gson = new Gson();
        Event event = new Event();

//        String response = "{\"article\":{\"title\":\"Mayflower docks at Plymouth Harbor\",\"date\":\"December 18\",\"synopsis\":\"On December 18, 1620, the English ship Mayflower docks at modern-day Plymouth, Massachusetts, and its passengers prepare to begin their new settlement, Plymouth Colony.\",\"url\":\"https://www.history.com/this-day-in-history/mayflower-docks-at-plymouth-harbor\"}}";
        String response = getTodaysEvent();

        event = gson.fromJson(response, Article.class).getEvent();
        eventRepository.save(event);
        return event;
    }

}

