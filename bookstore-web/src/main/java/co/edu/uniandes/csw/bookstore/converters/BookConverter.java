package co.edu.uniandes.csw.bookstore.converters;

import co.edu.uniandes.csw.bookstore.dtos.BookDTO;
import co.edu.uniandes.csw.bookstore.entities.BookEntity;
import java.util.ArrayList;
import java.util.List;

public abstract class BookConverter {

    private BookConverter() {
    }

    /**
     * Realiza la conversión de BookEntity a BookDTO Se invoca cuando otra
     * entidad tiene una referencia a BookEntity Entrega únicamente los
     * atributos proprios de la entidad
     *
     * @param entity instancia de BookEntity a convertir
     * @return instancia de BookDTO con los datos recibidos por parámetro
     */
    public static BookDTO refEntity2DTO(BookEntity entity) {
        if (entity != null) {
            BookDTO dto = new BookDTO();
            dto.setId(entity.getId());
            dto.setName(entity.getName());
            dto.setPublishDate(entity.getPublishDate());
            dto.setIsbn(entity.getIsbn());
            dto.setImage(entity.getImage());
            dto.setDescription(entity.getDescription());

            return dto;
        } else {
            return null;
        }
    }

    /**
     * Realiza la conversión de BookDTO a BookEntity Se invoca cuando otro DTO
     * tiene una referencia a BookDTO Convierte únicamente el ID ya que es el
     * único atributo necesario para guardar la relación en la base de datos
     *
     * @param dto instancia de BookDTO a convertir
     * @return instancia de BookEntity con los datos recibidos por parámetro
     */
    public static BookEntity refDTO2Entity(BookDTO dto) {
        if (dto != null) {
            BookEntity entity = new BookEntity();
            entity.setId(dto.getId());

            return entity;
        } else {
            return null;
        }
    }

    /**
     * Convierte una instancia de BookEntity a BookDTO Se invoca cuando se desea
     * consultar la entidad y sus relaciones muchos a uno o uno a uno
     *
     * @param entity instancia de BookEntity a convertir
     * @return instancia de BookDTO con los datos recibidos por parámetro
     */
    public static BookDTO basicEntity2DTO(BookEntity entity) {
        if (entity != null) {
            BookDTO dto = refEntity2DTO(entity);

            return dto;
        } else {
            return null;
        }
    }

    /**
     * Convierte una instancia de BookDTO a BookEntity Se invoca cuando se
     * necesita convertir una instancia de BookDTO con los atributos propios de
     * la entidad y con las relaciones uno a uno o muchos a uno
     *
     * @param dto instancia de BookDTO a convertir
     * @return Instancia de BookEntity creada a partir de los datos de dto
     */
    public static BookEntity basicDTO2Entity(BookDTO dto) {
        if (dto != null) {
            BookEntity entity = new BookEntity();
            entity.setId(dto.getId());
            entity.setName(dto.getName());
            entity.setIsbn(dto.getIsbn());
            entity.setImage(dto.getImage());
            entity.setPublishDate(dto.getPublishDate());
            entity.setDescription(dto.getDescription());

            return entity;
        } else {
            return null;
        }
    }

    /**
     * Convierte instancias de BookEntity a BookDTO incluyendo sus relaciones
     * Uno a muchos y Muchos a muchos
     *
     * @param entity Instancia de BookEntity a convertir
     * @return
     */
    public static BookDTO fullEntity2DTO(BookEntity entity) {
        BookDTO dto = basicEntity2DTO(entity);
        return dto;
    }

    /**
     * Convierte una instancia de BookDTO a BookEntity Incluye todos los
     * atributos propios de Book, además de todas sus relaciones
     *
     * @param dto
     * @return
     */
    public static BookEntity fullDTO2Entity(BookDTO dto) {
        BookEntity entity = basicDTO2Entity(dto);
        return entity;
    }

    /**
     * Convierte una colección de instancias de BookEntity a BookDTO Para cada
     * instancia de BookEntity en la lista, invoca basicEntity2DTO y añade el
     * nuevo DTO a una nueva lista
     *
     * @param entities Colección de entidades a convertir
     * @return Collección de instancias de BookDTO
     */
    public static List<BookDTO> listEntity2DTO(List<BookEntity> entities) {
        List<BookDTO> dtos = new ArrayList<BookDTO>();
        if (entities != null) {
            for (BookEntity entity : entities) {
                dtos.add(fullEntity2DTO(entity));
            }
        }
        return dtos;
    }

    /**
     * Convierte una colección de instancias de BookDTO a instancias de
     * BookEntity Para cada instancia se invoca el método basicDTO2Entity
     *
     * @param dtos
     * @return
     */
    public static List<BookEntity> listDTO2Entity(List<BookDTO> dtos) {
        List<BookEntity> entities = new ArrayList<BookEntity>();
        if (dtos != null) {
            for (BookDTO dto : dtos) {
                entities.add(basicDTO2Entity(dto));
            }
        }
        return entities;
    }
}
