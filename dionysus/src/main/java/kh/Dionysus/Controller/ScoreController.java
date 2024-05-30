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


@CrossOrigin(origins = "http://gool-man.kro.kr:3000")
@RestController
@RequestMapping("/score")
public class ScoreController {
    //사용함.
    @PutMapping("/insertscore")
    public ResponseEntity<String> insertScore(@RequestBody ScoreDto dto) throws SQLException {
        ScoreDao dao = new ScoreDao();
        boolean result = dao.scoreInsert(dto);
        if (result) {
            return new ResponseEntity<>("Score inserted successfully.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed to insert score.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
