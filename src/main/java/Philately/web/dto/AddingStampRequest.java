package Philately.web.dto;

import Philately.stamp.model.Paper;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddingStampRequest {

    @Size(min = 5, max = 20, message = "Name length must be between 5 and 20 symbols!")
    private String name;

    @Size(min = 5, max = 25, message = "Description length must be between 5 and 25 symbols!")
    private String description;

    @URL(message = "Please enter valid image url")
    private String imageUrl;

    @NotNull(message = "You must select a type of paper!")
    private Paper paper;
}
