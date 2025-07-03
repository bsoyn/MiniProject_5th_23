package untitled.dto;

import lombok.Data;
import java.util.*; 

@Data
public class TempSaveManuscriptDTO{
    private Long authorId; 
    private Long manuscriptId; 
    private String title; 
    private String contents; 
}