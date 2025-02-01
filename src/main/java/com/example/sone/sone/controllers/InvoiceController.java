package com.example.sone.sone.controllers;

import com.example.sone.sone.entity.Invoice;
import com.example.sone.sone.services.InvoiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Tag(name = "Invoices", description = "APIs for managing invoices")
@RequestMapping("/api/invoices")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @Operation(
            summary = "Get all invoices",
            description = "Retrieves a list of all invoices in the system"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved all invoices")
    @GetMapping
    public List<Invoice> getAllInvoices() {
        return invoiceService.getAllInvoices();
    }

    @Operation(
            summary = "Get invoice by ID",
            description = "Retrieves a specific invoice using its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Invoice found"),
            @ApiResponse(responseCode = "404", description = "Invoice not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Invoice> getInvoiceById(@PathVariable ObjectId id) {
        Optional<Invoice> invoice = invoiceService.getInvoiceById(id);
        return invoice.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Create new invoice",
            description = "Creates a new invoice in the system"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Invoice created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid invoice data provided")
    })
    @PostMapping
    public Optional<Invoice> createInvoice(@RequestBody Invoice invoice) {
        return invoiceService.createInvoice(invoice);
    }

    @Operation(
            summary = "Update existing invoice",
            description = "Updates an existing invoice with new information"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Invoice updated successfully"),
            @ApiResponse(responseCode = "404", description = "Invoice not found"),
            @ApiResponse(responseCode = "400", description = "Invalid invoice data provided")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Invoice> updateInvoice(@PathVariable ObjectId id, @RequestBody Invoice invoice) {
        Invoice updatedInvoice = invoiceService.updateInvoice(id, invoice);
        if (updatedInvoice != null) {
            return ResponseEntity.ok(updatedInvoice);
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(
            summary = "Delete invoice",
            description = "Deletes an invoice from the system"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Invoice deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Invoice not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvoice(@PathVariable ObjectId id) {
        invoiceService.deleteInvoice(id);
        return ResponseEntity.ok().build();
    }
}

