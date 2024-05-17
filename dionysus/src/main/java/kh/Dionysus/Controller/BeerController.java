package kh.Dionysus.Controller;

import kh.Dionysus.Dao.AlcoholDao;
import kh.Dionysus.Dto.AlcoholTotalDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/alcohol")
public class BeerController {
    @GetMapping("/selectalcohol")
    public ResponseEntity<List<AlcoholTotalDto>> selectAlcohol(@RequestParam String name) throws SQLException {
        AlcoholDao dao = new AlcoholDao();
        List<AlcoholTotalDto> AlcoholList = dao.alcoholSelect(name);
        return ResponseEntity.ok(AlcoholList);
    }

}
