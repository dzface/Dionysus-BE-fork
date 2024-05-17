package kh.Dionysus.Controller;

import kh.Dionysus.Dao.SearchDao;
import kh.Dionysus.Dto.AlcoholTotalDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/search")
public class SearchController {



    @GetMapping("/selectsearch")
    public ResponseEntity<List<AlcoholTotalDto>> selectSearch(@RequestParam String search) throws SQLException {
            SearchDao dao = new SearchDao();
            List<AlcoholTotalDto> SearchAlcohol = dao.alcoholSearch(search);
            return ResponseEntity.ok(SearchAlcohol);


    }
}