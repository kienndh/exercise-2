package com.javaweb.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestParam;

import com.javaweb.model.BuildingSearchResponseDTO;

public interface BuildingSearchService {
	List<BuildingSearchResponseDTO> searchBuilding(@RequestParam Map<String, Object> params,
			                                       @RequestParam List<String> typeRent);
}
