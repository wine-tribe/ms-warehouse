package backend.controller;

import audit.api.annotation.Audit;
import audit.api.annotation.AuditParam;
import backend.dto.OrderDto;
import backend.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
@Tag(name = "Заказы", description = "Операции с заказами")
public class OrderController {

    private final OrderService service;

    @Audit("WAREHOUSE_ORDER_GET_BY_ID")
    @GetMapping("/{id}")
    @Operation(summary = "Получить заказ по ID")
    public ResponseEntity<OrderDto> getById(@AuditParam("orderId") @PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @Audit("WAREHOUSE_ORDER_GET_ALL")
    @GetMapping("/all")
    @Operation(summary = "Получить все заказы")
    public ResponseEntity<List<OrderDto>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @Audit("WAREHOUSE_ORDER_DELETE_BY_ID")
    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить заказ по ID")
    public ResponseEntity<Void> deleteById(@AuditParam("orderId") @PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Audit("WAREHOUSE_ORDER_SET_IS_DELETED")
    @PatchMapping("/{id}/isDeleted")
    @Operation(summary = "Изменить флаг isDeleted для заказа")
    public ResponseEntity<OrderDto> isDeleted(
            @AuditParam("orderId") @PathVariable Long id,
            @AuditParam("isDeleted") @RequestParam boolean isDeleted
    ) {
        return ResponseEntity.ok(service.isDeleted(id, isDeleted));
    }

    @Audit("WAREHOUSE_ORDER_CHANGE_STATUS")
    @PatchMapping("/{id}/{status}")
    @Operation(summary = "Изменить статус заказа")
    public ResponseEntity<OrderDto> changeStatus(
            @AuditParam("orderId") @PathVariable Long id,
            @AuditParam("status") @PathVariable String status
    ) {
        return ResponseEntity.ok(service.changeStatus(id, status));
    }

    @Audit("WAREHOUSE_ORDER_UPDATE_DETAILS")
    @PatchMapping("/{id}/update")
    @Operation(summary = "Обновить детали заказа (notProcess, warehouseId, price)")
    public ResponseEntity<OrderDto> updateOrderDetails(
            @AuditParam("orderId") @PathVariable Long id,
            @AuditParam("notProcess") @RequestParam(required = false) Boolean notProcess,
            @AuditParam("warehouseId") @RequestParam(required = false) Long warehouseId,
            @AuditParam("price") @RequestParam(required = false) BigDecimal price
    ) {
        return ResponseEntity.ok(service.updateOrderDetails(id, notProcess, warehouseId, price));
    }

    @Audit("WAREHOUSE_ORDER_SAVE")
    @PostMapping("/save")
    @Operation(summary = "Сохранить новый заказ")
    public ResponseEntity<OrderDto> saveOrder(@AuditParam("orderDto") @RequestBody OrderDto orderDto) {
        return ResponseEntity.ok(service.saveOrder(orderDto));
    }

    @Audit("WAREHOUSE_ORDER_GET_IDS_BY_WAREHOUSE")
    @GetMapping("/warehouse/{warehouseId}/order")
    @Operation(summary = "Получить ID заказов по складу")
    public ResponseEntity<List<Long>> getOrderIdsByWarehouse(@AuditParam("warehouseId") @PathVariable Long warehouseId) {
        return ResponseEntity.ok(service.getOrderIdsByWarehouse(warehouseId));
    }

    @Audit("WAREHOUSE_ORDER_TRANSFER")
    @PatchMapping("/{id}/transfer/{newWarehouseId}")
    @Operation(summary = "Перенести заказ на другой склад")
    public ResponseEntity<OrderDto> transferOrderToAnotherWarehouse(
            @AuditParam("orderId") @PathVariable Long id,
            @AuditParam("newWarehouseId") @PathVariable Long newWarehouseId
    ) {
        return ResponseEntity.ok(service.transferOrderToAnotherWarehouse(id, newWarehouseId));
    }

    @Audit("WAREHOUSE_ORDER_TRANSFER_BULK")
    @PatchMapping("/transfer")
    @Operation(summary = "Перенести несколько заказов на другой склад")
    public ResponseEntity<List<OrderDto>> transferOrdersToAnotherWarehouse(
            @AuditParam("orderIds") @RequestParam List<Long> orderIds,
            @AuditParam("newWarehouseId") @RequestParam Long newWarehouseId
    ) {
        return ResponseEntity.ok(service.transferOrdersToAnotherWarehouse(orderIds, newWarehouseId));
    }
}
