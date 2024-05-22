package kh.Dionysus.Controller;

import kh.Dionysus.Dao.ReviewDao;
import kh.Dionysus.Dto.ReviewDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/review")
public class ReviewController {
    @GetMapping("/selectreview")
    public ResponseEntity<List<ReviewDto>> selectReview() throws SQLException {
        ReviewDao dao = new ReviewDao();
        List<ReviewDto> reviewList = dao.reviewSelect2();
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

    @PutMapping("/updatereview")
    public ResponseEntity<String> updateReview(@RequestBody ReviewDto dto) throws SQLException {
        ReviewDao dao = new ReviewDao();
        boolean result = dao.reviewUpdate(dto);
        if (result) {
            return new ResponseEntity<>("Review updated successfully.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed to update review.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deletereview")
    public ResponseEntity<String> deleteReview(@RequestParam String user_id, @RequestParam String alcohol_name) throws SQLException {
        ReviewDao dao = new ReviewDao();
        boolean result = dao.reviewDelete(user_id, alcohol_name);
        if (result) {
            return new ResponseEntity<>("Review deleted successfully.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed to delete review.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}