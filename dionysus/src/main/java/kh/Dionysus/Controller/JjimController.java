package kh.Dionysus.Controller;

import kh.Dionysus.Dao.JjimDao;
import kh.Dionysus.Dto.JjimDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/jjim")
public class JjimController {
    //사용함.
    @PostMapping("/insertjjim")
    public ResponseEntity<String> insertjjim(@RequestBody JjimDto dto) {
        JjimDao jjimDao = new JjimDao();
        try {
            jjimDao.insertJjim(dto);
            return ResponseEntity.ok("Success");
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error occurred");
        }
    }
    //사용함.
    @PostMapping("/deletejjim")
    public ResponseEntity<String> deleteJjim(@RequestBody JjimDto dto) throws SQLException{
        JjimDao dao = new JjimDao();
        boolean result = dao.deleteJjim(dto);
        if(result){
            return new ResponseEntity<>("Jjim deleted successfully.",HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("Failed to delete jjim", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/selectjjim")
    public ResponseEntity<List<JjimDto>> selectJjim(@RequestParam String user_id) throws SQLException {
        JjimDao dao = new JjimDao();
        List<JjimDto> jjimList = dao.jjimSelect2(user_id);
        return new ResponseEntity<>(jjimList, HttpStatus.OK);
    }

    @PostMapping("/updatejjim")
    public ResponseEntity<String> updateJjim(@RequestBody JjimDto dto) throws SQLException {
        JjimDao dao = new JjimDao();
        boolean result = dao.jjimUpdate(dto);
        if (result) {
            return new ResponseEntity<>("Jjim updated successfully.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed to update jjim.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
