package com.javaweb.repository;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestParam;

import com.javaweb.entity.BuildingSearchEntity;

public interface BuildingSearchRepository {
	List<BuildingSearchEntity> searchBuilding(@RequestParam Map<String, Object> params,
                                              @RequestParam List<String> typeRent);
}
