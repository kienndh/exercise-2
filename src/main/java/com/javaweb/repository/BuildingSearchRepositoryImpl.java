package com.javaweb.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import com.javaweb.entity.BuildingSearchEntity;

@Repository
public class BuildingSearchRepositoryImpl implements BuildingSearchRepository {
	
	private static String url = "jdbc:mysql://localhost:3306/estatebasic?autoReconnect=true&useSSL=false";
	private static String username = "root";
	private static String password = "123456";

	@Override
	public List<BuildingSearchEntity> searchBuilding(@RequestParam Map<String, Object> params,
													 @RequestParam List<String> typeRent) {
		StringBuilder sql = new StringBuilder("select distinct b.id, b.name, d.name as district, b.ward, b.street, b.numberofbasement, b.managername, b.managerphonenumber, b.floorarea, \r\n"
				+ "ra.value as rentarea, b.rentprice, b.servicefee, b.brokeragefee\r\n"
				+ "from building as b\r\n"
				+ "left join district as d on b.districtid = d.id\r\n"
				+ "left join rentarea as ra on ra.buildingid = b.id\r\n"
				+ "left join assignmentbuilding as ab on b.id = ab.buildingid\r\n"
				+ "left join buildingrenttype as brt on brt.buildingid = b.id\r\n"
				+ "left join renttype as rt on rt.id = brt.renttypeid\r\n"
				+ "where 1 = 1 ");
		
		if (params.get("name") != null && !params.get("name").toString().isEmpty()) {
			sql.append("and b.name like '%" + params.get("name") + "%' ");
		}
		if (params.get("floorArea") != null && !params.get("floorArea").toString().isEmpty()) {
			sql.append("and b.floorarea = " + params.get("floorArea") + " ");
		}
		if (params.get("districtId") != null && !params.get("districtId").toString().isEmpty()) {
			sql.append("and b.districtid = " + params.get("districtId") + " ");
		}
		if (params.get("ward") != null && !params.get("ward").toString().isEmpty()) {
			sql.append("and b.ward like '%" + params.get("ward") + "%' ");
		}
		if (params.get("street") != null && !params.get("street").toString().isEmpty()) {
			sql.append("and b.street like '%" + params.get("street") + "%' ");
		}
		if (params.get("numberOfBasement") != null && !params.get("numberOfBasement").toString().isEmpty()) {
			sql.append("and b.numberofbasement = " + params.get("numberOfBasement") + " ");
		}
		if (params.get("direction") != null && !params.get("direction").toString().isEmpty()) {
			sql.append("and b.direction like '%" + params.get("direction") + "%' ");
		}
		if (params.get("level") != null && !params.get("level").toString().isEmpty()) {
			sql.append("and b.level like '%" + params.get("level") + "%' ");
		}
		if (params.get("areaFrom") != null && !params.get("areaFrom").toString().isEmpty()) {
			sql.append("and ra.value >= " + params.get("areaFrom") + " ");
		}
		if (params.get("areaTo") != null && !params.get("areaTo").toString().isEmpty()) {
			sql.append("and ra.value <= " + params.get("areaTo") + " ");
		}
		if (params.get("rentPriceFrom") != null && !params.get("rentPriceFrom").toString().isEmpty()) {
			sql.append("and b.rentprice >= " + params.get("rentPriceFrom") + " ");
		}
		if (params.get("rentPriceTo") != null && !params.get("rentPriceTo").toString().isEmpty()) {
			sql.append("and b.rentprice <= " + params.get("rentPriceTo") + " ");
		}
		if (params.get("managerName") != null && !params.get("managerName").toString().isEmpty()) {
			sql.append("and b.managername like '%" + params.get("managerName") + "%' ");
		}
		if (params.get("managerPhoneNumber") != null && !params.get("managerPhoneNumber").toString().isEmpty()) {
			sql.append("and b.managerphonenumber like '%" + params.get("managerPhoneNumber") + "%' ");
		}
		if (params.get("staffId") != null && !params.get("staffId").toString().isEmpty()) {
			sql.append("and ab.staffid = " + params.get("staffId") + " ");
		}
		
		if (typeRent != null && !typeRent.isEmpty()) {
			String types = "";
			for (int i = 0; i < typeRent.size(); i++) {
				types += "'" + typeRent.get(i) + "'" + (i < typeRent.size() - 1 ? "," : "");
			}
			sql.append("and rt.code in(" + types + ") ");
		}
		
		sql.append("\n order by b.id");
		
		Map<Integer, BuildingSearchEntity> map = new LinkedHashMap<>();
		try (Connection conn = DriverManager.getConnection(url, username, password);
			 Statement stmt = conn.createStatement();
			 ResultSet rs = stmt.executeQuery(sql.toString());) {
			
			while (rs.next()) {
				Integer id = rs.getObject("id", Integer.class);
				BuildingSearchEntity buildingSearchEntity = map.get(id);
				if (buildingSearchEntity == null) {
					buildingSearchEntity = new BuildingSearchEntity();
					
					buildingSearchEntity.setName(rs.getString("name"));
					buildingSearchEntity.setDistrict(rs.getString("district"));
					buildingSearchEntity.setWard(rs.getString("ward"));
					buildingSearchEntity.setStreet(rs.getString("street"));
					buildingSearchEntity.setNumberOfBasement(rs.getObject("numberofbasement", Integer.class));
					buildingSearchEntity.setManegerName(rs.getString("managername"));
					buildingSearchEntity.setManegerPhoneNumber(rs.getString("managerphonenumber"));
					buildingSearchEntity.setFloorArea(rs.getObject("floorarea", Integer.class));
					buildingSearchEntity.setRentPrice(rs.getObject("rentprice", Integer.class));
					buildingSearchEntity.setServiceFee(rs.getObject("servicefee", Integer.class));
					buildingSearchEntity.setBrokerageFee(rs.getObject("brokeragefee", Integer.class));
					buildingSearchEntity.setRentArea("");
					
					map.put(id, buildingSearchEntity);
				}
				String currentRentArea = rs.getString("rentarea");
				if (currentRentArea != null && !currentRentArea.isEmpty()) {
					String existing = buildingSearchEntity.getRentArea();
					if (existing.isEmpty()) {
						buildingSearchEntity.setRentArea(currentRentArea);
					} else {
						buildingSearchEntity.setRentArea(existing + ", " + currentRentArea);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Connected database failed...");
		}
		
		return new ArrayList<>(map.values());
	}
}