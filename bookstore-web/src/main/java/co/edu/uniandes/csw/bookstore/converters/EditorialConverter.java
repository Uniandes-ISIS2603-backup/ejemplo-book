package co.edu.uniandes.csw.bookstore.converters;

import co.edu.uniandes.csw.bookstore.dtos.EditorialDTO;
import co.edu.uniandes.csw.bookstore.entities.EditorialEntity;
import java.util.ArrayList;
import java.util.List;

public abstract class EditorialConverter {

    /**
     * Constructor privado para evitar la creación del constructor implícito de
     * Java
     *
     * @generated
     */
    private EditorialConverter() {
    }

    /**
     * Realiza la conversión de EditorialEntity a EditorialDTO. Se invoca cuando
     * otra entidad tiene una referencia a EditorialEntity. Entrega únicamente
     * los atributos proprios de la entidad.
     *
     * @param entity instancia de EditorialEntity a convertir
     * @return instancia de EditorialDTO con los datos recibidos por parámetro
     * @generated
     */
    public static EditorialDTO refEntity2DTO(EditorialEntity entity) {
        if (entity != null) {
            EditorialDTO dto = new EditorialDTO();
            dto.setId(entity.getId());
            dto.setName(entity.getName());

            return dto;
        } else {
            return null;
        }
    }

    /**
     * Realiza la conversión de EditorialDTO a EditorialEntity Se invoca cuando
     * otro DTO tiene una referencia a EditorialDTO Convierte únicamente el ID
     * ya que es el único atributo necesario para guardar la relación en la base
     * de datos
     *
     * @param dto instancia de EditorialDTO a convertir
     * @return instancia de EditorialEntity con los datos recibidos por
     * parámetro
     * @generated
     */
    public static EditorialEntity refDTO2Entity(EditorialDTO dto) {
        if (dto != null) {
            EditorialEntity entity = new EditorialEntity();
            entity.setId(dto.getId());

            return entity;
        } else {
            return null;
        }
    }

    /**
     * Convierte una instancia de EditorialEntity a EditorialDTO Se invoca
     * cuando se desea consultar la entidad y sus relaciones muchos a uno o uno
     * a uno
     *
     * @param entity instancia de EditorialEntity a convertir
     * @return Instancia de EditorialDTO con los datos recibidos por parámetro
     * @generated
     */
    private static EditorialDTO basicEntity2DTO(EditorialEntity entity) {
        if (entity != null) {
            EditorialDTO dto = new EditorialDTO();
            dto.setId(entity.getId());
            dto.setName(entity.getName());

            return dto;
        } else {
            return null;
        }
    }

    /**
     * Convierte una instancia de EditorialDTO a EditorialEntity Se invoca
     * cuando se necesita convertir una instancia de EditorialDTO con los
     * atributos propios de la entidad y con las relaciones uno a uno o muchos a
     * uno
     *
     * @param dto instancia de EditorialDTO a convertir
     * @return Instancia de EditorialEntity creada a partir de los datos de dto
     * @generated
     */
    private static EditorialEntity basicDTO2Entity(EditorialDTO dto) {
        if (dto != null) {
            EditorialEntity entity = new EditorialEntity();
            entity.setId(dto.getId());
            entity.setName(dto.getName());

            return entity;
        } else {
            return null;
        }
    }

    /**
     * Convierte instancias de EditorialEntity a EditorialDTO incluyendo sus
     * relaciones Uno a muchos y Muchos a muchos
     *
     * @param entity Instancia de EditorialEntity a convertir
     * @return Instancia de EditorialDTO con los datos recibidos por parámetro
     * @generated
     */
    public static EditorialDTO fullEntity2DTO(EditorialEntity entity) {
        if (entity != null) {
            EditorialDTO dto = basicEntity2DTO(entity);
            return dto;
        } else {
            return null;
        }
    }

    /**
     * Convierte una instancia de EditorialDTO a EditorialEntity. Incluye todos
     * los atributos de EditorialEntity.
     *
     * @param dto Instancia de EditorialDTO a convertir
     * @return Instancia de EditorialEntity con los datos recibidos por
     * parámetro
     * @generated
     */
    public static EditorialEntity fullDTO2Entity(EditorialDTO dto) {
        if (dto != null) {
            EditorialEntity entity = basicDTO2Entity(dto);
            return entity;
        } else {
            return null;
        }
    }

    /**
     * Convierte una colección de instancias de EditorialEntity a EditorialDTO.
     * Para cada instancia de EditorialEntity en la lista, invoca
     * basicEntity2DTO y añade el nuevo EditorialDTO a una nueva lista
     *
     * @param entities Colección de entidades a convertir
     * @return Collección de instancias de EditorialDTO
     * @generated
     */
    public static List<EditorialDTO> listEntity2DTO(List<EditorialEntity> entities) {
        List<EditorialDTO> dtos = new ArrayList<EditorialDTO>();
        if (entities != null) {
            for (EditorialEntity entity : entities) {
                dtos.add(basicEntity2DTO(entity));
            }
        }
        return dtos;
    }

    /**
     * Convierte una colección de instancias de EditorialDTO a instancias de
     * EditorialEntity Para cada instancia se invoca el método basicDTO2Entity
     *
     * @param dtos entities Colección de EditorialDTO a convertir
     * @return Collección de instancias de EditorialEntity
     * @generated
     */
    public static List<EditorialEntity> listDTO2Entity(List<EditorialDTO> dtos) {
        List<EditorialEntity> entities = new ArrayList<EditorialEntity>();
        if (dtos != null) {
            for (EditorialDTO dto : dtos) {
                entities.add(basicDTO2Entity(dto));
            }
        }
        return entities;
    }
}
