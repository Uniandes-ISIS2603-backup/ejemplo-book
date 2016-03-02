package co.edu.uniandes.csw.bookstore.converters;

import co.edu.uniandes.csw.bookstore.dtos.ReviewDTO;
import co.edu.uniandes.csw.bookstore.entities.BookEntity;
import co.edu.uniandes.csw.bookstore.entities.ReviewEntity;
import java.util.ArrayList;
import java.util.List;

public abstract class ReviewConverter {

    /**
     * Constructor privado para evitar la creación del constructor implícito de Java
     * @generated
     */
    private ReviewConverter() {
    }

    /**
     * Realiza la conversión de ReviewEntity a ReviewDTO.
     * Se invoca cuando otra entidad tiene una referencia a ReviewEntity.
     * Entrega únicamente los atributos proprios de la entidad.
     *
     * @param entity instancia de ReviewEntity a convertir
     * @return instancia de ReviewDTO con los datos recibidos por parámetro
     * @generated
     */
    public static ReviewDTO refEntity2DTO(ReviewEntity entity) {
        if (entity != null) {
            ReviewDTO dto = new ReviewDTO();
            dto.setId(entity.getId());
            dto.setName(entity.getName());
            dto.setSource(entity.getSource());
            dto.setDescription(entity.getDescription());

            return dto;
        } else {
            return null;
        }
    }

    /**
     * Realiza la conversión de ReviewDTO a ReviewEntity Se invoca cuando otro DTO
     * tiene una referencia a ReviewDTO Convierte únicamente el ID ya que es el
     * único atributo necesario para guardar la relación en la base de datos
     *
     * @param dto instancia de ReviewDTO a convertir
     * @return instancia de ReviewEntity con los datos recibidos por parámetro
     * @generated
     */
    public static ReviewEntity refDTO2Entity(ReviewDTO dto) {
        if (dto != null) {
            ReviewEntity entity = new ReviewEntity();
            entity.setId(dto.getId());

            return entity;
        } else {
            return null;
        }
    }

    /**
     * Convierte una instancia de ReviewEntity a ReviewDTO Se invoca cuando se desea
     * consultar la entidad y sus relaciones muchos a uno o uno a uno
     *
     * @param entity instancia de ReviewEntity a convertir
     * @return Instancia de ReviewDTO con los datos recibidos por parámetro
     * @generated
     */
    private static ReviewDTO basicEntity2DTO(ReviewEntity entity) {
        if (entity != null) {
            ReviewDTO dto = new ReviewDTO();
            dto.setId(entity.getId());
            dto.setName(entity.getName());
            dto.setSource(entity.getSource());
            dto.setDescription(entity.getDescription());
            dto.setBook(BookConverter.refEntity2DTO(entity.getBook()));

            return dto;
        } else {
            return null;
        }
    }

    /**
     * Convierte una instancia de ReviewDTO a ReviewEntity Se invoca cuando se
     * necesita convertir una instancia de ReviewDTO con los atributos propios de
     * la entidad y con las relaciones uno a uno o muchos a uno
     *
     * @param dto instancia de ReviewDTO a convertir
     * @return Instancia de ReviewEntity creada a partir de los datos de dto
     * @generated
     */
    private static ReviewEntity basicDTO2Entity(ReviewDTO dto) {
        if (dto != null) {
            ReviewEntity entity = new ReviewEntity();
            entity.setId(dto.getId());
            entity.setName(dto.getName());
            entity.setSource(dto.getSource());
            entity.setDescription(dto.getDescription());
            entity.setBook(BookConverter.refDTO2Entity(dto.getBook()));

            return entity;
        } else {
            return null;
        }
    }

    /**
     * Convierte instancias de ReviewEntity a ReviewDTO incluyendo sus relaciones
     * Uno a muchos y Muchos a muchos
     *
     * @param entity Instancia de ReviewEntity a convertir
     * @return Instancia de ReviewDTO con los datos recibidos por parámetro
     * @generated
     */
    public static ReviewDTO fullEntity2DTO(ReviewEntity entity) {
        if (entity != null) {
            ReviewDTO dto = basicEntity2DTO(entity);
            return dto;
        } else {
            return null;
        }
    }

    /**
     * Convierte una instancia de ReviewDTO a ReviewEntity.
     * Incluye todos los atributos de ReviewEntity.
     *
     * @param dto Instancia de ReviewDTO a convertir
     * @return Instancia de ReviewEntity con los datos recibidos por parámetro
     * @generated
     */
    public static ReviewEntity fullDTO2Entity(ReviewDTO dto) {
        if (dto != null) {
            ReviewEntity entity = basicDTO2Entity(dto);
            return entity;
        } else {
            return null;
        }
    }

    /**
     * Convierte una colección de instancias de ReviewEntity a ReviewDTO. Para cada
     * instancia de ReviewEntity en la lista, invoca basicEntity2DTO y añade el
     * nuevo ReviewDTO a una nueva lista
     *
     * @param entities Colección de entidades a convertir
     * @return Collección de instancias de ReviewDTO
     * @generated
     */
    public static List<ReviewDTO> listEntity2DTO(List<ReviewEntity> entities) {
        List<ReviewDTO> dtos = new ArrayList<ReviewDTO>();
        if (entities != null) {
            for (ReviewEntity entity : entities) {
                dtos.add(basicEntity2DTO(entity));
            }
        }
        return dtos;
    }

    /**
     * Convierte una colección de instancias de ReviewDTO a instancias de
     * ReviewEntity Para cada instancia se invoca el método basicDTO2Entity
     *
     * @param dtos entities Colección de ReviewDTO a convertir
     * @return Collección de instancias de ReviewEntity
     * @generated
     */
    public static List<ReviewEntity> listDTO2Entity(List<ReviewDTO> dtos) {
        List<ReviewEntity> entities = new ArrayList<ReviewEntity>();
        if (dtos != null) {
            for (ReviewDTO dto : dtos) {
                entities.add(basicDTO2Entity(dto));
            }
        }
        return entities;
    }

    /**
     * Convierte una instancia de ReviewDTO a ReviewEntity asignando un valor
     * al atributo org.eclipse.uml2.uml.internal.impl.PropertyImpl@394af115 (name: book, visibility: <unset>) (isLeaf: false) (isStatic: false) (isOrdered: false, isUnique: true, isReadOnly: false) (aggregation: none, isDerived: false, isDerivedUnion: false, isID: false) de ReviewEntity. Se usa cuando se necesita convertir
     * un ReviewDTO asignando el libro asociado
     * @param dto Instancia de ReviewDTO
     * @param parent Instancia de BookEntity
     * @return Instancia de ReviewEntity con BookEntity asociado
     * @generated
     */
    public static ReviewEntity childDTO2Entity(ReviewDTO dto, BookEntity parent){
        ReviewEntity entity = basicDTO2Entity(dto);
        entity.setBook(parent);
        return entity;
    }

    /**
     * Convierte una colección de instancias de ReviewDTO a ReviewEntity
     * asignando el mismo padre para todos. Se usa cuando se necesita crear o
     * actualizar varios ReviewEntity con el mismo org.eclipse.uml2.uml.internal.impl.PropertyImpl@394af115 (name: book, visibility: <unset>) (isLeaf: false) (isStatic: false) (isOrdered: false, isUnique: true, isReadOnly: false) (aggregation: none, isDerived: false, isDerivedUnion: false, isID: false)
     * @param dtos Colección de instancias de ReviewDTO
     * @param parent Instancia de BookEntity
     * @return Colección de ReviewEntity con el atributo org.eclipse.uml2.uml.internal.impl.PropertyImpl@394af115 (name: book, visibility: <unset>) (isLeaf: false) (isStatic: false) (isOrdered: false, isUnique: true, isReadOnly: false) (aggregation: none, isDerived: false, isDerivedUnion: false, isID: false) asignado
     * @generated
     */
    public static List<ReviewEntity> childListDTO2Entity(List<ReviewDTO> dtos, BookEntity parent) {
        List<ReviewEntity> entities = new ArrayList<ReviewEntity>();
        if (dtos != null) {
            for (ReviewDTO dto : dtos) {
                entities.add(childDTO2Entity(dto, parent));
            }
        }
        return entities;
    }
}
