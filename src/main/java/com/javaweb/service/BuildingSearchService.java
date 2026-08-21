package com.javaweb.service;

import java.util.List;

import com.javaweb.model.BuildingSearchRequestDTO;
import com.javaweb.model.BuildingSearchResponseDTO;

public interface BuildingSearchService {
	List<BuildingSearchResponseDTO> searchBuilding(BuildingSearchRequestDTO building);
}
