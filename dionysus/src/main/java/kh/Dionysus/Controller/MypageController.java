package kh.Dionysus.Controller;

import kh.Dionysus.Dao.JjimDao;
import kh.Dionysus.Dao.MemberDelDao;
import kh.Dionysus.Dao.MemberUpdateDao;
import kh.Dionysus.Dao.ReviewDao;
import kh.Dionysus.Dto.UserDto;
import kh.Dionysus.Dto.MypageDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/mypage")
public class MypageController {

    // 회원 정보 수정
    @PostMapping("/memberupdate")
    public ResponseEntity<Boolean> memberUpdate(@RequestBody UserDto Dto) {
        MemberUpdateDao dao = new MemberUpdateDao();
        Boolean isTrue= dao.memberUpdate(Dto);
        return new ResponseEntity<>(isTrue, HttpStatus.OK);
    }

    // 회원 탈퇴 전 이름과 주민번호 체크 true 값이면 정보가 있는 것
    @PostMapping("/memcheck")
    public ResponseEntity<Boolean> memcheck(@RequestBody UserDto Dto) {
        MemberDelDao dao = new MemberDelDao();
        boolean isTrue = dao.MemberCheck(Dto);
        return new ResponseEntity<>(isTrue, HttpStatus.OK);
    }
    // 회원 탈퇴
    @PostMapping("/memberdel")
    public ResponseEntity<Boolean> memberdel(@RequestBody UserDto Dto) {
        MemberDelDao dao = new MemberDelDao();
        boolean isTrue = dao.memberDelete(Dto.getUser_id());
        return new ResponseEntity<>(isTrue, HttpStatus.OK);
    }








    // 찜
    @PostMapping("/jjimalcohol")
    public ResponseEntity<List<MypageDto>> jjimalcohol(@RequestBody MypageDto Dto) throws SQLException {
        JjimDao dao = new JjimDao();
        List<MypageDto> jjimList = dao.jjimSelect(Dto.getUser_id());
        List<MypageDto> jjimAlcoholList = dao.jjimAlcoholSelect(jjimList);
        return new ResponseEntity<>(jjimAlcoholList, HttpStatus.OK);
    }


    // 리뷰
    @PostMapping("/reviewalcohol")
    public ResponseEntity<List<MypageDto>> reviewalcohol(@RequestBody MypageDto Dto) throws SQLException {
        ReviewDao dao = new ReviewDao();
        List<MypageDto> reviewList = dao.reviewSelect(Dto.getUser_id());
        List<MypageDto> jjimAlcoholList = dao.reviewAlcoholSelect(reviewList);
        return new ResponseEntity<>(jjimAlcoholList, HttpStatus.OK);
    }


}
