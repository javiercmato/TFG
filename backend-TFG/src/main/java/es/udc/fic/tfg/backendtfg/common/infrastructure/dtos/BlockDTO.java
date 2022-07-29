package es.udc.fic.tfg.backendtfg.common.infrastructure.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Data
@AllArgsConstructor
public class BlockDTO<T> {
    @NotNull
    private List<T> items;
    
    @NotNull
    private boolean hasMoreItems;
    
    @NotNull
    @PositiveOrZero
    private int itemsCount;
}
