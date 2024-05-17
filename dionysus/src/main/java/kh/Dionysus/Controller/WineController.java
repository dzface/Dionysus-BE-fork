package kh.Dionysus.Controller;

import kh.Dionysus.Dao.AlcoholDao;
import kh.Dionysus.Dto.AlcoholTotalDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

public class WineController {
    @CrossOrigin(origins = "http://localhost:3000")
    @RestController
    @RequestMapping("/wine")
    public class BeerController {
        @GetMapping("/selectwine")
        public ResponseEntity<List<AlcoholTotalDto>> selectBeer(@RequestParam String name) throws SQLException {
            AlcoholDao dao = new AlcoholDao();
            List<AlcoholTotalDto> BeerList = dao.alcoholSelect(name);
            return ResponseEntity.ok(BeerList);
        }
    }
}
