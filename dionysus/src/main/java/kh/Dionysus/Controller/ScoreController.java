package kh.Dionysus.Controller;

import kh.Dionysus.Dao.AlcoholDao;
import kh.Dionysus.Dto.AlcoholTotalDto;
import kh.Dionysus.Dto.ScoreDto;
import kh.Dionysus.Dao.ScoreDao;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

public class ScoreController {
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
    @GetMapping("/selectscore")
    public ResponseEntity<List<ScoreDto>> selectScore(@RequestParam String alcohol_name) throws SQLException {
        ScoreDao dao = new ScoreDao();
        List<ScoreDto> scoreList = dao.scoreSelect(alcohol_name);
        return new ResponseEntity<>(scoreList, HttpStatus.OK);
    }

    @PostMapping("/insertscore")
    public ResponseEntity<String> insertScore(@RequestBody ScoreDto dto) throws SQLException {
        ScoreDao dao = new ScoreDao();
        boolean result = dao.scoreInsert(dto);
        if (result) {
            return new ResponseEntity<>("Score inserted successfully.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed to insert score.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updatescore")
    public ResponseEntity<String> updateScore(@RequestBody ScoreDto dto) throws SQLException {
        ScoreDao dao = new ScoreDao();
        boolean result = dao.scoreUpdate(dto);
        if (result) {
            return new ResponseEntity<>("Score updated successfully.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed to update score.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
