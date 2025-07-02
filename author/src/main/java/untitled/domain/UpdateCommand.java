package untitled.domain;

import lombok.Data;

@Data
public class UpdateCommand {
    public String email;
    public String name;
    public String bio;
    public String majorWork;
    public File portfolio;
} 