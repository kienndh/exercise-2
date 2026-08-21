package com.javaweb.repository;

import java.util.List;

import com.javaweb.entity.BuildingSearchEntity;
import com.javaweb.model.BuildingSearchRequestDTO;

public interface BuildingSearchRepository {
	List<BuildingSearchEntity> searchBuilding(BuildingSearchRequestDTO building);
}
