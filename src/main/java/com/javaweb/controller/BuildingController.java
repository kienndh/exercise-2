package com.javaweb.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javaweb.model.BuildingSearchRequestDTO;
import com.javaweb.model.BuildingSearchResponseDTO;
import com.javaweb.service.BuildingSearchService;

@RestController
public class BuildingController {
	
	@Autowired
	private BuildingSearchService buildingSearchService;
	
	@GetMapping("/api/building")
	public List<BuildingSearchResponseDTO> searchBuilding(BuildingSearchRequestDTO building){
		List<BuildingSearchResponseDTO> result = buildingSearchService.searchBuilding(building);
		return result;
	}
}
