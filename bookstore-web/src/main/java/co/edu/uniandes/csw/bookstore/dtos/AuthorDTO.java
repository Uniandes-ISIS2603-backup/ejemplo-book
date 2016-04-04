package co.edu.uniandes.csw.bookstore.dtos;

import co.edu.uniandes.csw.crud.api.podam.strategy.DateStrategy;
import java.util.Date;
import uk.co.jemos.podam.common.PodamStrategyValue;

public class AuthorDTO {

    private Long id;

    private String name;

    @PodamStrategyValue(DateStrategy.class)
    private Date birthDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }
}
