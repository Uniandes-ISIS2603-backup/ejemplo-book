package co.edu.uniandes.csw.bookstore.converters;

import co.edu.uniandes.csw.bookstore.dtos.PrizeDTO;
import co.edu.uniandes.csw.bookstore.entities.PrizeEntity;
import java.util.ArrayList;
import java.util.List;

public abstract class PrizeConverter {

    public PrizeConverter() {
    }
    
    /**
     * Realiza la conversión de PrizeEntity a PrizeDTO. Se invoca cuando
     * otra entidad tiene una referencia a PrizeEntity. Entrega únicamente
     * los atributos proprios de la entidad.
     *
     * @param entity instancia de PrizeEntity a convertir
     * @return instancia de PrizeDTO con los datos recibidos por parámetro
     * @generated
     */
    public static PrizeDTO refEntity2DTO(PrizeEntity entity) {
        if (entity != null) {
            PrizeDTO dto = new PrizeDTO();
            dto.setId(entity.getId());
            dto.setName(entity.getName());

            return dto;
        } else {
            return null;
        }
    }

    /**
     * Realiza la conversión de PrizeDTO a PrizeEntity Se invoca cuando
     * otro DTO tiene una referencia a PrizeDTO Convierte únicamente el ID
     * ya que es el único atributo necesario para guardar la relación en la base
     * de datos
     *
     * @param dto instancia de PrizeDTO a convertir
     * @return instancia de PrizeEntity con los datos recibidos por
     * parámetro
     * @generated
     */
    public static PrizeEntity refDTO2Entity(PrizeDTO dto) {
        if (dto != null) {
            PrizeEntity entity = new PrizeEntity();
            entity.setId(dto.getId());

            return entity;
        } else {
            return null;
        }
    }

    /**
     * Convierte una instancia de PrizeEntity a PrizeDTO Se invoca
     * cuando se desea consultar la entidad y sus relaciones muchos a uno o uno
     * a uno
     *
     * @param entity instancia de PrizeEntity a convertir
     * @return Instancia de PrizeDTO con los datos recibidos por parámetro
     * @generated
     */
    private static PrizeDTO basicEntity2DTO(PrizeEntity entity) {
        if (entity != null) {
            PrizeDTO dto = new PrizeDTO();
            dto.setId(entity.getId());
            dto.setName(entity.getName());
            dto.setOrganization(entity.getOrganization());
            dto.setDate(entity.getDate());
            return dto;
        } else {
            return null;
        }
    }

    /**
     * Convierte una instancia de PrizeDTO a PrizeEntity Se invoca
     * cuando se necesita convertir una instancia de PrizeDTO con los
     * atributos propios de la entidad y con las relaciones uno a uno o muchos a
     * uno
     *
     * @param dto instancia de PrizeDTO a convertir
     * @return Instancia de PrizeEntity creada a partir de los datos de dto
     * @generated
     */
    private static PrizeEntity basicDTO2Entity(PrizeDTO dto) {
        if (dto != null) {
            PrizeEntity entity = new PrizeEntity();
            entity.setId(dto.getId());
            entity.setName(dto.getName());
            entity.setOrganization(dto.getOrganization());
            entity.setDate(dto.getDate());

            return entity;
        } else {
            return null;
        }
    }

    /**
     * Convierte instancias de PrizeEntity a PrizeDTO incluyendo sus
     * relaciones Uno a muchos y Muchos a muchos
     *
     * @param entity Instancia de PrizeEntity a convertir
     * @return Instancia de PrizeDTO con los datos recibidos por parámetro
     * @generated
     */
    public static PrizeDTO fullEntity2DTO(PrizeEntity entity) {
        if (entity != null) {
            PrizeDTO dto = basicEntity2DTO(entity);
            return dto;
        } else {
            return null;
        }
    }

    /**
     * Convierte una instancia de PrizeDTO a PrizeEntity. Incluye todos
     * los atributos de PrizeEntity.
     *
     * @param dto Instancia de PrizeDTO a convertir
     * @return Instancia de PrizeEntity con los datos recibidos por
     * parámetro
     * @generated
     */
    public static PrizeEntity fullDTO2Entity(PrizeDTO dto) {
        if (dto != null) {
            PrizeEntity entity = basicDTO2Entity(dto);
            return entity;
        } else {
            return null;
        }
    }

    /**
     * Convierte una colección de instancias de PrizeEntity a PrizeDTO.
     * Para cada instancia de PrizeEntity en la lista, invoca
     * basicEntity2DTO y añade el nuevo PrizeDTO a una nueva lista
     *
     * @param entities Colección de entidades a convertir
     * @return Collección de instancias de PrizeDTO
     * @generated
     */
    public static List<PrizeDTO> listEntity2DTO(List<PrizeEntity> entities) {
        List<PrizeDTO> dtos = new ArrayList<PrizeDTO>();
        if (entities != null) {
            for (PrizeEntity entity : entities) {
                dtos.add(basicEntity2DTO(entity));
            }
        }
        return dtos;
    }

    /**
     * Convierte una colección de instancias de PrizeDTO a instancias de
     * PrizeEntity Para cada instancia se invoca el método basicDTO2Entity
     *
     * @param dtos entities Colección de PrizeDTO a convertir
     * @return Collección de instancias de PrizeEntity
     * @generated
     */
    public static List<PrizeEntity> listDTO2Entity(List<PrizeDTO> dtos) {
        List<PrizeEntity> entities = new ArrayList<PrizeEntity>();
        if (dtos != null) {
            for (PrizeDTO dto : dtos) {
                entities.add(basicDTO2Entity(dto));
            }
        }
        return entities;
    }
}
