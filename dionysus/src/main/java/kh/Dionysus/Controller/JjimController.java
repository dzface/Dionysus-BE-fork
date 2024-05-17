package kh.Dionysus.Controller;

import kh.Dionysus.Dao.JjimDao;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/jjim")
public class JjimController {
    private final JjimDao jjimDao = new JjimDao();

    @PostMapping("/toggle")
    public ResponseEntity<String> toggleJjim(@RequestParam String userId,
                                             @RequestParam String alcoholName,
                                             @RequestParam boolean jjim) {
        try {
            jjimDao.toggleJjim(userId, alcoholName, jjim);
            return ResponseEntity.ok("Success");
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error occurred");
        }
    }
}
