package com.javaweb.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.javaweb.model.BuildingSearchResponseDTO;
import com.javaweb.service.BuildingSearchService;

@RestController
public class BuildingController {
	
	@Autowired
	private BuildingSearchService buildingSearchService;
	
	@GetMapping("/api/building")
	public List<BuildingSearchResponseDTO> searchBuilding(@RequestParam Map<String, Object> params,
			                                              @RequestParam List<String> typeRent){
		List<BuildingSearchResponseDTO> result = buildingSearchService.searchBuilding(params, typeRent);
		return result;
	}
}
