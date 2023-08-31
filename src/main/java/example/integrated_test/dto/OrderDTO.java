package example.integrated_test.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import example.integrated_test.domain.Address;
import example.integrated_test.domain.OrderStatus;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.Data;

import java.util.Date;

@Data
public class OrderDTO {
    @Hidden
    private Long orderId;

//    private Customer customer;

//    private List<Product> products;

    private Address address;

//    private PaymentMethod paymentMethod;

    @Hidden
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss")
    private Date orderDate;

    @Hidden
    private OrderStatus orderStatus;

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }
}
