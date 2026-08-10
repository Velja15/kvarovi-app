package com.kvarovi.app.repository;

import com.kvarovi.app.entity.Building;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuildingRepository extends JpaRepository<Building, Long> {
}