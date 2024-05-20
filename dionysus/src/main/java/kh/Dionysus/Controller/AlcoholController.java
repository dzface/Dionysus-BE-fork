package kh.Dionysus.Controller;

import kh.Dionysus.Dao.AlcoholDao;
import kh.Dionysus.Dto.AlcoholTotalDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/alcohol")
public class AlcoholController {
    //처음에 category로 들어가면 화면에 띄워주는 리스트
    @GetMapping("/selectalcohol")
    public ResponseEntity<List<AlcoholTotalDto>> selectAlcohol(@RequestParam String category) throws SQLException {
        AlcoholDao dao = new AlcoholDao();
        List<AlcoholTotalDto> AlcoholList = dao.alcoholSelect(category);
        return new ResponseEntity<>(AlcoholList, HttpStatus.OK);
    }

}
