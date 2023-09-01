package example.integrated_test.dto;

import lombok.Data;

@Data
public class UpdateOrderDTO extends OrderDTO {
    private Long orderId;
}
