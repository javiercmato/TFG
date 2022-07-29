package es.udc.fic.tfg.backendtfg.common.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Block<T> {
    /** Elementos almacenados */
    private List<T> items;
    
    /** Indica si hay más elementos */
    private boolean existMoreItems;
    
    /** Cantidad de elementos contenidos */
    private int itemsCount;
}
