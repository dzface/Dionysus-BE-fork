package kh.Dionysus.Controller;

import kh.Dionysus.Dao.BeerDao;
import kh.Dionysus.Dto.AlcoholDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/beer")
public class BeerController {
    @GetMapping("/selectbeer")
    public ResponseEntity<List<AlcoholDto>> selectBeer(@RequestParam String name){
        BeerDao dao = new BeerDao();
        List<AlcoholDto> Alcohol = dao.beerSelect(name);
        return ResponseEntity.ok(Alcohol);
    }

}
