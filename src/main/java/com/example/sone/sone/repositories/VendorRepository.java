package com.example.sone.sone.repositories;

import com.example.sone.sone.entity.Invoice;
import com.example.sone.sone.entity.Vendor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface VendorRepository extends MongoRepository<Vendor, ObjectId> {
    Optional<Vendor> findByEmail(String email);
}
