package com.example.sone.sone.services;

import com.example.sone.sone.dto.LoginResponseDTO;
import com.example.sone.sone.dto.VendorLoginDTO;
import com.example.sone.sone.entity.Vendor;
import com.example.sone.sone.repositories.VendorRepository;
import com.example.sone.sone.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Service
public class VendorService {

    private static final Logger logger = LoggerFactory.getLogger(VendorService.class);

    @Autowired
    private VendorRepository vendorRepository;

    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public VendorService(VendorRepository vendorRepository, BCryptPasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.vendorRepository = vendorRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public List<Vendor> getAllVendors() {
        return vendorRepository.findAll();
    }

    public Vendor createVendor(Vendor vendor) {
        try {
            // Validate required fields
            if (vendor.getPassword() == null || vendor.getEmail() == null || vendor.getName() == null ||
                    vendor.getCity() == null || vendor.getState() == null) {
                throw new IllegalArgumentException("Missing required fields");
            }

            // Check if the email already exists
            if (vendorRepository.findByEmail(vendor.getEmail()).isPresent()) {
                throw new IllegalArgumentException("Email already exists");
            }

            // Hash the password
            String hashedPassword = passwordEncoder.encode(vendor.getPassword());
            vendor.setPassword(hashedPassword);

            // Validate email format
            if (!isValidEmail(vendor.getEmail())) {
                throw new IllegalArgumentException("Invalid email format");
            }

            // Save the vendor to the database
            return vendorRepository.save(vendor);

        } catch (Exception e) {
            logger.error("Error creating vendor: " + e.getMessage(), e);
            throw new IllegalArgumentException("Error creating vendor: " + e.getMessage(), e);
        }
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    public LoginResponseDTO loginVendor(VendorLoginDTO vendor) {
        try {
            // Log the login attempt
            logger.info("Vendor login attempt for email: {}", vendor.getEmail());

            // Find the vendor by email
            Optional<Vendor> vendorOptional = vendorRepository.findByEmail(vendor.getEmail());
            if (vendorOptional.isPresent() && passwordEncoder.matches(vendor.getPassword(), vendorOptional.get().getPassword())) {
                // Generate token
                String token = jwtUtil.generateToken(vendorOptional.get().getEmail());

                // Create response DTO
                return new LoginResponseDTO(vendor.getEmail(), token);
            } else {
                throw new IllegalArgumentException("Invalid email or password");
            }

        } catch (Exception e) {
            logger.error("Error logging in vendor: " + e.getMessage(), e);
            throw new IllegalArgumentException("Error logging in vendor: " + e.getMessage(), e);
        }
    }
}
