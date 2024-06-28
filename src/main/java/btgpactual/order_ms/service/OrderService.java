package btgpactual.order_ms.service;

import btgpactual.order_ms.controller.dto.OrderResponse;
import btgpactual.order_ms.dto.OrderCreatedEvent;
import btgpactual.order_ms.entity.OrderEntity;
import btgpactual.order_ms.entity.OrderItem;
import btgpactual.order_ms.repository.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void save(OrderCreatedEvent orderCreatedEvent){

        var entity = new OrderEntity();
        entity.setOrderId(orderCreatedEvent.codigoPedido());
        entity.setCustomerId(orderCreatedEvent.codigoCliente());
        entity.setItems(orderCreatedEvent.itens().stream()
                .map(i -> new OrderItem(i.produto(), i.quantidade(), i.preco()))
                .toList());
        entity.setTotal(getTotal(orderCreatedEvent));

        orderRepository.save(entity);

    }

    public Page<OrderResponse> findAllByCustomerId(Long customerId, PageRequest pageRequest){

        var orders = orderRepository.findAllByCustomerId(customerId, pageRequest);

        return orders.map(OrderResponse::fromEntity);
    }
    private BigDecimal getTotal(OrderCreatedEvent orderCreatedEvent) {
        return orderCreatedEvent.itens().stream().map(i -> i.preco().multiply(BigDecimal.valueOf(i.quantidade()))).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
    }
}

