package com.javaweb.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javaweb.entity.BuildingSearchEntity;
import com.javaweb.model.BuildingSearchRequestDTO;
import com.javaweb.model.BuildingSearchResponseDTO;
import com.javaweb.repository.BuildingSearchRepository;

@Service
public class BuildingSearchServiceImpl implements BuildingSearchService{
	
	@Autowired
	private BuildingSearchRepository buildingSearchRepository;
	@Override
	public List<BuildingSearchResponseDTO> searchBuilding(BuildingSearchRequestDTO building) {
		List<BuildingSearchResponseDTO> result = new ArrayList<>();
		List<BuildingSearchEntity> buildingSearchEntities = buildingSearchRepository.searchBuilding(building);
		
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
