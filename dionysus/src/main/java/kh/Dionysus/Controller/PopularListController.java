package kh.Dionysus.Controller;
import kh.Dionysus.Dao.PopularListDao;
import kh.Dionysus.Dto.AlcoholTotalDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@CrossOrigin(origins = "http://gool-man.kro.kr:3000")
@RestController
@RequestMapping("/popular")
public class PopularListController {
    @GetMapping("/selectpopular")
    public ResponseEntity<List<AlcoholTotalDto>> selectPopular(@RequestParam String tag) throws SQLException {
        PopularListDao dao = new PopularListDao();
        List<AlcoholTotalDto> PopularList = dao.popularSelect(tag);
        return new ResponseEntity<>(PopularList, HttpStatus.OK);
    }

}
