package com.javaweb.service;

import java.util.ArrayList;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.javaweb.entity.BuildingSearchEntity;
import com.javaweb.model.BuildingSearchResponseDTO;
import com.javaweb.repository.BuildingSearchRepository;

@Service
public class BuildingSearchServiceImpl implements BuildingSearchService{
	
	@Autowired
	private BuildingSearchRepository buildingSearchRepository;
	@Override
	public List<BuildingSearchResponseDTO> searchBuilding(@RequestParam Map<String, Object> params,
			                                              @RequestParam List<String> typeRent) {
		List<BuildingSearchResponseDTO> result = new ArrayList<>();
		List<BuildingSearchEntity> buildingSearchEntities = buildingSearchRepository.searchBuilding(params, typeRent);
		
		for(BuildingSearchEntity item : buildingSearchEntities) {
			BuildingSearchResponseDTO tmp = new BuildingSearchResponseDTO();
			tmp.setName(item.getName());
			tmp.setAddress(item.getStreet() + "," + item.getWard() + "," + item.getDistrict());
			tmp.setNumberOfBasement(item.getNumberOfBasement());
			tmp.setManegerName(item.getManegerName());
			tmp.setManegerPhoneNumber(item.getManegerPhoneNumber());
			tmp.setFloorArea(item.getFloorArea());
			tmp.setRentArea(item.getRentArea());
			tmp.setEmptyArea(item.getEmptyArea());
			tmp.setRentPrice(item.getRentPrice());
			tmp.setServiceFee(item.getServiceFee());
			tmp.setBrokerageFee(item.getBrokerageFee());
			result.add(tmp);
		}
		
		return result;
	}

}
