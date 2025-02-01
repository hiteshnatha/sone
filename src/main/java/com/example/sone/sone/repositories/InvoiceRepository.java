package com.example.sone.sone.repositories;

import com.example.sone.sone.entity.Invoice;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface InvoiceRepository extends MongoRepository<Invoice, ObjectId> {
    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);
}
