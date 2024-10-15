package com.kike.suscription.repository;

import com.kike.suscription.entity.Suscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SuscriptionRepository extends JpaRepository<Suscription, Long> {
    Optional<Suscription> findByClientIdAndVendorId(String userId, String vendorId);

    List<Suscription> findByClientId(String clientId);

    List<Suscription> findByVendorId(String vendorId);
}
