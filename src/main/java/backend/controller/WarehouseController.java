package backend.controller;

import audit.api.annotation.Audit;
import audit.api.annotation.AuditParam;
import backend.dto.WarehouseDto;
import backend.service.WarehouseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/warehouse")
@RequiredArgsConstructor
@Tag(name = "Склады", description = "Операции со складами")
public class WarehouseController {

    private final WarehouseService warehouseService;

    @Audit("WAREHOUSE_GET_BY_ID")
    @GetMapping("/{id}")
    @Operation(summary = "Получить склад по ID")
    public ResponseEntity<WarehouseDto> getById(@AuditParam("warehouseId") @PathVariable Long id) {
        return ResponseEntity.ok(warehouseService.getById(id));
    }

    @Audit("WAREHOUSE_GET_ALL")
    @GetMapping("/all")
    @Operation(summary = "Получить все склады")
    public ResponseEntity<List<WarehouseDto>> getAll() {
        return ResponseEntity.ok(warehouseService.getAll());
    }

    @Audit("WAREHOUSE_DELETE_BY_ID")
    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить склад по ID")
    public ResponseEntity<Void> deleteById(@AuditParam("warehouseId") @PathVariable Long id) {
        warehouseService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Audit("WAREHOUSE_SET_IS_DELETED")
    @PatchMapping("/{id}/isDeleted")
    @Operation(summary = "Обновить статус удаления склада")
    public ResponseEntity<WarehouseDto> isDeleted(
            @AuditParam("warehouseId") @PathVariable Long id,
            @AuditParam("isDeleted") @RequestParam boolean isDeleted
    ) {
        return ResponseEntity.ok(warehouseService.isDeleted(id, isDeleted));
    }

    @Audit("WAREHOUSE_CHANGE_STATUS")
    @PatchMapping("/{id}/{status}")
    @Operation(summary = "Изменить статус склада")
    public ResponseEntity<WarehouseDto> changeStatus(
            @AuditParam("warehouseId") @PathVariable Long id,
            @AuditParam("status") @PathVariable String status
    ) {
        return ResponseEntity.ok(warehouseService.changeStatus(id, status));
    }

    @Audit("WAREHOUSE_SAVE")
    @PostMapping("/save")
    @Operation(summary = "Сохранить склад")
    public ResponseEntity<WarehouseDto> saveWarehouse(@AuditParam("warehouseDto") @RequestBody WarehouseDto warehouseDto) {
        return ResponseEntity.ok(warehouseService.saveWarehouse(warehouseDto));
    }

}
