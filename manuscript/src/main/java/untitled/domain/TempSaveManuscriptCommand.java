package untitled.domain;

import lombok.Data;
import java.util.*; 

@Data
public class TempSaveManuscriptCommand{
    private Long authorId; 
    private Long manuscriptId; 
    private String title; 
    private String contents; 
}