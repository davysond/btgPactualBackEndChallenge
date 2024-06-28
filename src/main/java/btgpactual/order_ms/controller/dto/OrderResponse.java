package btgpactual.order_ms.controller.dto;

import btgpactual.order_ms.entity.OrderEntity;
import org.springframework.core.annotation.Order;

import java.math.BigDecimal;

public record OrderResponse(Long orderId, Long customerId, BigDecimal total) {

    public static OrderResponse fromEntity(OrderEntity entity){
        return new OrderResponse(entity.getOrderId(), entity.getCustomerId(), entity.getTotal());
    }
}
