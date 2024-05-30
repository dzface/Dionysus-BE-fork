package kh.Dionysus.Controller;

import kh.Dionysus.Dao.JjimDao;
import kh.Dionysus.Dto.JjimDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@CrossOrigin(origins = "http://kh-dionysus.kro.kr:3000")
@RestController
@RequestMapping("/jjim")
public class JjimController {
    //사용함.
    @PostMapping("/insertjjim")
    public ResponseEntity<String> insertjjim(@RequestBody JjimDto dto) {
        JjimDao jjimDao = new JjimDao();
        try {
            jjimDao.insertJjim(dto);
            return ResponseEntity.ok("Jjim insertd successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error occurred");
        }
    }
    //사용함.
    @PostMapping("/deletejjim")
    public ResponseEntity<String> deleteJjim(@RequestBody JjimDto dto){
        JjimDao dao = new JjimDao();
        try{
            dao.deleteJjim(dto);
            return ResponseEntity.ok("Jjim deleted successfully.");
        }
        catch (SQLException e){
            return ResponseEntity.ok("Failed to delete jjim");
        }
    }
    @GetMapping("/selectjjim")
    public ResponseEntity<List<JjimDto>> selectJjim(@RequestParam String user_id) throws SQLException {
        JjimDao dao = new JjimDao();
        List<JjimDto> jjimList = dao.jjimSelect2(user_id);
        return new ResponseEntity<>(jjimList, HttpStatus.OK);
    }
}
