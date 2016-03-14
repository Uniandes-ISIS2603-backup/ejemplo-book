package co.edu.uniandes.csw.bookstore.converters;

import co.edu.uniandes.csw.bookstore.dtos.AuthorDTO;
import co.edu.uniandes.csw.bookstore.entities.AuthorEntity;
import java.util.ArrayList;
import java.util.List;

public abstract class AuthorConverter {

    /**
     * Constructor privado para evitar la creación del constructor implícito de Java
     * @generated
     */
    private AuthorConverter() {
    }

    /**
     * Realiza la conversión de AuthorEntity a AuthorDTO.
     * Se invoca cuando otra entidad tiene una referencia a AuthorEntity.
     * Entrega únicamente los atributos proprios de la entidad.
     *
     * @param entity instancia de AuthorEntity a convertir
     * @return instancia de AuthorDTO con los datos recibidos por parámetro
     * @generated
     */
    public static AuthorDTO refEntity2DTO(AuthorEntity entity) {
        if (entity != null) {
            AuthorDTO dto = new AuthorDTO();
            dto.setId(entity.getId());
            dto.setName(entity.getName());
            dto.setBirthDate(entity.getBirthDate());

            return dto;
        } else {
            return null;
        }
    }

    /**
     * Realiza la conversión de AuthorDTO a AuthorEntity Se invoca cuando otro DTO
     * tiene una referencia a AuthorDTO Convierte únicamente el ID ya que es el
     * único atributo necesario para guardar la relación en la base de datos
     *
     * @param dto instancia de AuthorDTO a convertir
     * @return instancia de AuthorEntity con los datos recibidos por parámetro
     * @generated
     */
    public static AuthorEntity refDTO2Entity(AuthorDTO dto) {
        if (dto != null) {
            AuthorEntity entity = new AuthorEntity();
            entity.setId(dto.getId());

            return entity;
        } else {
            return null;
        }
    }

    /**
     * Convierte una instancia de AuthorEntity a AuthorDTO Se invoca cuando se desea
     * consultar la entidad y sus relaciones muchos a uno o uno a uno
     *
     * @param entity instancia de AuthorEntity a convertir
     * @return Instancia de AuthorDTO con los datos recibidos por parámetro
     * @generated
     */
    private static AuthorDTO basicEntity2DTO(AuthorEntity entity) {
        if (entity != null) {
            AuthorDTO dto = new AuthorDTO();
            dto.setId(entity.getId());
            dto.setName(entity.getName());
            dto.setBirthDate(entity.getBirthDate());

            return dto;
        } else {
            return null;
        }
    }

    /**
     * Convierte una instancia de AuthorDTO a AuthorEntity Se invoca cuando se
     * necesita convertir una instancia de AuthorDTO con los atributos propios de
     * la entidad y con las relaciones uno a uno o muchos a uno
     *
     * @param dto instancia de AuthorDTO a convertir
     * @return Instancia de AuthorEntity creada a partir de los datos de dto
     * @generated
     */
    private static AuthorEntity basicDTO2Entity(AuthorDTO dto) {
        if (dto != null) {
            AuthorEntity entity = new AuthorEntity();
            entity.setId(dto.getId());
            entity.setName(dto.getName());
            entity.setBirthDate(dto.getBirthDate());

            return entity;
        } else {
            return null;
        }
    }

    /**
     * Convierte instancias de AuthorEntity a AuthorDTO incluyendo sus relaciones
     * Uno a muchos y Muchos a muchos
     *
     * @param entity Instancia de AuthorEntity a convertir
     * @return Instancia de AuthorDTO con los datos recibidos por parámetro
     * @generated
     */
    public static AuthorDTO fullEntity2DTO(AuthorEntity entity) {
        if (entity != null) {
            AuthorDTO dto = basicEntity2DTO(entity);
            return dto;
        } else {
            return null;
        }
    }

    /**
     * Convierte una instancia de AuthorDTO a AuthorEntity.
     * Incluye todos los atributos de AuthorEntity.
     *
     * @param dto Instancia de AuthorDTO a convertir
     * @return Instancia de AuthorEntity con los datos recibidos por parámetro
     * @generated
     */
    public static AuthorEntity fullDTO2Entity(AuthorDTO dto) {
        if (dto != null) {
            AuthorEntity entity = basicDTO2Entity(dto);
            return entity;
        } else {
            return null;
        }
    }

    /**
     * Convierte una colección de instancias de AuthorEntity a AuthorDTO. Para cada
     * instancia de AuthorEntity en la lista, invoca basicEntity2DTO y añade el
     * nuevo AuthorDTO a una nueva lista
     *
     * @param entities Colección de entidades a convertir
     * @return Collección de instancias de AuthorDTO
     * @generated
     */
    public static List<AuthorDTO> listEntity2DTO(List<AuthorEntity> entities) {
        List<AuthorDTO> dtos = new ArrayList<AuthorDTO>();
        if (entities != null) {
            for (AuthorEntity entity : entities) {
                dtos.add(basicEntity2DTO(entity));
            }
        }
        return dtos;
    }

    /**
     * Convierte una colección de instancias de AuthorDTO a instancias de
     * AuthorEntity Para cada instancia se invoca el método basicDTO2Entity
     *
     * @param dtos entities Colección de AuthorDTO a convertir
     * @return Collección de instancias de AuthorEntity
     * @generated
     */
    public static List<AuthorEntity> listDTO2Entity(List<AuthorDTO> dtos) {
        List<AuthorEntity> entities = new ArrayList<AuthorEntity>();
        if (dtos != null) {
            for (AuthorDTO dto : dtos) {
                entities.add(basicDTO2Entity(dto));
            }
        }
        return entities;
    }
}
