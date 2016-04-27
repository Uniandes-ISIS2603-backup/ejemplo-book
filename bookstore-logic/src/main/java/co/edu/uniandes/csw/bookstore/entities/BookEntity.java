package co.edu.uniandes.csw.bookstore.entities;

import co.edu.uniandes.csw.crud.spi.entity.BaseEntity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import uk.co.jemos.podam.common.PodamExclude;

@Entity
public class BookEntity extends BaseEntity implements Serializable {

    private String isbn;
    private String image;
    @Temporal(TemporalType.DATE)
    private Date publishDate;
    private String description;

    @ManyToMany
    @PodamExclude
    private List<AuthorEntity> authors = new ArrayList<>();

    @ManyToOne
    @PodamExclude
    private EditorialEntity editorial;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    @PodamExclude
    private List<ReviewEntity> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    @PodamExclude
    private List<PrizeEntity> prizes = new ArrayList<>();

    /**
     * @return the isbn
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * @param isbn the isbn to set
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    /**
     * @return the image
     */
    public String getImage() {
        return image;
    }

    /**
     * @param image the image to set
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * @return the publishDate
     */
    public Date getPublishDate() {
        return publishDate;
    }

    /**
     * @param publishDate the publishDate to set
     */
    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    public List<AuthorEntity> getAuthors() {
        return authors;
    }

    public void setAuthors(List<AuthorEntity> authors) {
        this.authors = authors;
    }

    public EditorialEntity getEditorial() {
        return editorial;
    }

    public void setEditorial(EditorialEntity editorial) {
        this.editorial = editorial;
    }

    public List<ReviewEntity> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewEntity> reviews) {
        this.reviews = reviews;
    }

    /**
     * @return the prizes
     */
    public List<PrizeEntity> getPrizes() {
        return prizes;
    }

    /**
     * @param prizes the prizes to set
     */
    public void setPrizes(List<PrizeEntity> prizes) {
        this.prizes = prizes;
    }

    /**
     * @return the prizes
     */
    public PrizeEntity getPrize(Long prizeId) {
        List<PrizeEntity> prizes = getPrizes();
        Iterator<PrizeEntity> i = prizes.iterator();
        boolean find = false;
        while (i.hasNext() && !find) {
            if (i.next().getId() == prizeId) {
                find = true;
            } else {
                find = false;
            }
        }
        if (find) {
            return i.next();
        } else {
            return null;
        }
    }
}

