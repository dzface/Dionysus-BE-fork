package kh.Dionysus.Controller;

import kh.Dionysus.Dao.SearchDao;
import kh.Dionysus.Dto.AlcoholTotalDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@CrossOrigin(origins = "http://192.168.10.26:3000")
@RestController
@RequestMapping("/search")
public class SearchController {
    // 사용함.
    @GetMapping("/selectsearch")
    public ResponseEntity<List<AlcoholTotalDto>> selectSearch(@RequestParam String category,@RequestParam String searchTerm) throws SQLException {
            SearchDao dao = new SearchDao();
            List<AlcoholTotalDto> SearchAlcohol = dao.alcoholSearch(category, searchTerm);
            return ResponseEntity.ok(SearchAlcohol);
    }
}