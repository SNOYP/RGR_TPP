package hello.controller;

import hello.service.SecureCrudService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Controller
public class MainController {

    private final SecureCrudService crudService;

    public MainController(SecureCrudService crudService) {
        this.crudService = crudService;
    }

    @GetMapping("/")
    public String index(Model model) {
        try {
            model.addAttribute("genresList", crudService.getAllGenres());
            model.addAttribute("groupsList", crudService.getAllGroups()); // Це для повної таблиці

            model.addAttribute("currentGenres", crudService.readAllData("genres"));
            model.addAttribute("currentGroups", crudService.readAllData("groups"));
            model.addAttribute("currentSongs", crudService.readAllData("songs"));

        } catch (SQLException e) {
            model.addAttribute("error", "❌ Помилка БД: " + e.getMessage());
            model.addAttribute("genresList", new HashMap<>());
            model.addAttribute("groupsList", new HashMap<>());
            model.addAttribute("currentGenres", new ArrayList<>());
            model.addAttribute("currentGroups", new ArrayList<>());
            model.addAttribute("currentSongs", new ArrayList<>());
        }
        return "index";
    }

    // --- API ДЛЯ ФІЛЬТРАЦІЇ (AJAX) ---
    @GetMapping("/api/groups-by-genre")
    @ResponseBody
    public ResponseEntity<Map<Integer, String>> getGroupsByGenre(@RequestParam int genreId) {
        try {
            // Використовуємо метод, який вже є у вашому сервісі
            return ResponseEntity.ok(crudService.getGroupsByGenre(genreId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new HashMap<>());
        }
    }

    // --- ДОДАВАННЯ ---

    @PostMapping("/api/add-genre")
    @ResponseBody
    public ResponseEntity<String> addGenre(@RequestParam String name) {
        // ID тепер автоматичний, передаємо тільки ім'я
        try {
            Map<String, String> params = new HashMap<>();
            params.put("name", name);
            crudService.insert("genres", params);
            return ResponseEntity.ok("Жанр додано!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/api/add-group")
    @ResponseBody
    public ResponseEntity<String> addGroup(@RequestParam String name, @RequestParam Integer genre_id) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("name", name);
            params.put("genre_id", String.valueOf(genre_id));
            crudService.insert("groups", params);
            return ResponseEntity.ok("Групу додано!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/api/add-song")
    @ResponseBody
    public ResponseEntity<String> addSong(@RequestParam String title, @RequestParam Integer group_id, @RequestParam Integer duration_seconds) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("title", title);
            params.put("group_id", String.valueOf(group_id));
            params.put("duration_seconds", String.valueOf(duration_seconds));
            crudService.insert("songs", params);
            return ResponseEntity.ok("Пісню додано!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // --- ВИДАЛЕННЯ ---
    @PostMapping("/api/delete-item")
    @ResponseBody
    public ResponseEntity<String> deleteItem(@RequestParam String table, @RequestParam int id) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("id", String.valueOf(id));
            crudService.delete(table, params);
            return ResponseEntity.ok("Видалено успішно!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Помилка видалення: " + e.getMessage());
        }
    }
}