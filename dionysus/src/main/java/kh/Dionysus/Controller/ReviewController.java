package kh.Dionysus.Controller;

import kh.Dionysus.Dao.ReviewDao;
import kh.Dionysus.Dto.ReviewDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@CrossOrigin(origins = "http://gool-man.kro.kr:3000")
@RestController
@RequestMapping("/review")
public class ReviewController {
    @GetMapping("/selectreview")
    public ResponseEntity<List<ReviewDto>> selectReview(@RequestParam String alcohol_name) throws SQLException {
        ReviewDao dao = new ReviewDao();
        List<ReviewDto> reviewList = dao.reviewSelect(alcohol_name);
         return new ResponseEntity<>(reviewList, HttpStatus.OK);
    }

    @PostMapping("/insertreview")
    public ResponseEntity<String> insertReview(@RequestBody ReviewDto dto) throws SQLException {
        ReviewDao dao = new ReviewDao();
        boolean result = dao.reviewInsert(dto);
        if (result) {
            return new ResponseEntity<>("Review inserted successfully.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed to insert review.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}