package com.kike.suscription.repository;

import com.kike.suscription.dto.SuscriptionDto;
import com.kike.suscription.entity.Suscription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SuscriptionRepository extends JpaRepository<Suscription, Long> {
}
