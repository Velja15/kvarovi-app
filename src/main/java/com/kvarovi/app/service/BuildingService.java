package com.kvarovi.app.service;

import com.kvarovi.app.entity.Building;
import com.kvarovi.app.repository.BuildingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BuildingService {

    private final BuildingRepository buildingRepository;

    public BuildingService(BuildingRepository buildingRepository) {
        this.buildingRepository = buildingRepository;
    }

    public List<Building> getAll() {
        return buildingRepository.findAll();
    }

    public Building create(Building building) {
        return buildingRepository.save(building);
    }
}