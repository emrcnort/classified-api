package com.sahibinden.controller;

import com.sahibinden.dto.ClassifiedDto;
import com.sahibinden.dto.ClassifiedStateDto;
import com.sahibinden.dto.StatusCountDto;
import com.sahibinden.enums.Status;
import com.sahibinden.service.ClassifiedService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/dashboard/classifieds")
@Tag(name = "Classified Controller", description = "İlan API")
@RequiredArgsConstructor
public class ClassifiedController {
    private final ClassifiedService classifiedService;
    @PostMapping
    @Operation(summary = "Create a new classified", description = "Creates a new classified with the provided details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Classified created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public void create(@Valid @RequestBody ClassifiedDto classifiedDto) {
        classifiedService.createClassified(classifiedDto);
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Change the status of a classified", description = "Changes the status of the classified with the given ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status changed successfully"),
            @ApiResponse(responseCode = "404", description = "Classified not found")
    })
    public void changeStatus(@PathVariable Long id, @RequestParam Status newStatus) {
        classifiedService.changeClassifiedStatus(id, newStatus);
    }

    @GetMapping("/statistics")
    @Operation(summary = "Get classified ad statistics", description = "Retrieves statistics for classifieds.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully")
    })
    public ResponseEntity<List<StatusCountDto>>  getClassifiedStatistics() {
        return ResponseEntity.ok(classifiedService.getClassifiedStatistics());
    }

    @GetMapping("/{id}/status-changes")
    @Operation(summary = "Get status changes of a classified", description = "Retrieves all status changes for the classified with the given ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status changes retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Classified not found")
    })
    public ResponseEntity<List<ClassifiedStateDto>> statusChanges(@PathVariable Long id) {
        return ResponseEntity.ok(classifiedService.getAllClassifiedStatusChanges(id));
    }
}
