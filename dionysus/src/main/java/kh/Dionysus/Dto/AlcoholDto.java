package kh.Dionysus.Dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlcoholDto{
    private String alcohol_name;
    private String category;
    private String country_of_origin;
    private String com;
    private int abv;
    private int volume;
    private int price;
    private String tag;
}
