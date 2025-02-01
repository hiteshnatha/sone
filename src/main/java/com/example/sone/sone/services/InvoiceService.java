package com.example.sone.sone.services;

import com.example.sone.sone.entity.Invoice;
import com.example.sone.sone.repositories.InvoiceRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    public Optional<Invoice> getInvoiceById(ObjectId id) {
        return invoiceRepository.findById(id);
    }

        public Optional<Invoice> createInvoice(Invoice invoice) {
            try {
                String invoiceNumber = invoice.getInvoiceNumber();
                if (invoiceRepository.findByInvoiceNumber(invoiceNumber).isPresent()) {
                    throw new IllegalArgumentException("Invoice number already exists");
                }
                return Optional.of(invoiceRepository.save(invoice));
            } catch (IllegalArgumentException e) {
                return Optional.empty();
            }
        }

    public Invoice updateInvoice(ObjectId id, Invoice invoiceDetails) {
        Optional<Invoice> invoice = invoiceRepository.findById(id);
        if (invoice.isPresent()) {
            Invoice existingInvoice = invoice.get();
        if (invoiceDetails.getCustomerName() != null && !invoiceDetails.getCustomerName().isEmpty()) {
             existingInvoice.setCustomerName(invoiceDetails.getCustomerName());
         }
         if (invoiceDetails.getInvoiceNumber() != null && !invoiceDetails.getInvoiceNumber().isEmpty()) {
             existingInvoice.setInvoiceNumber(invoiceDetails.getInvoiceNumber());
         }
         if (invoiceDetails.getInvoiceDate() != null) {
             existingInvoice.setInvoiceDate(invoiceDetails.getInvoiceDate());
         }
         if (invoiceDetails.getAmount() != null) {
             existingInvoice.setAmount(invoiceDetails.getAmount());
         }
         if (invoiceDetails.getStatus() != null && !invoiceDetails.getStatus().isEmpty()) {
             existingInvoice.setStatus(invoiceDetails.getStatus());
         }
            return invoiceRepository.save(existingInvoice);
        }
        return null;
    }

    public void deleteInvoice(ObjectId id) {
        invoiceRepository.deleteById(id);
    }
}
