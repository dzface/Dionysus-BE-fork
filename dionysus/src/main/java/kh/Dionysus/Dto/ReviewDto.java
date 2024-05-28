package kh.Dionysus.Dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewDto {
    private String user_id;
    private String alcohol_name;
    private String review;
    private String user_nick;
}
