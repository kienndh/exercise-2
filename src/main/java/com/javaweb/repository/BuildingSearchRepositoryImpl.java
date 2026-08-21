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

import com.javaweb.entity.BuildingSearchEntity;
import com.javaweb.model.BuildingSearchRequestDTO;

@Repository
public class BuildingSearchRepositoryImpl implements BuildingSearchRepository{
	
	private static String url = "jdbc:mysql://localhost:3306/estatebasic?autoReconnect=true&useSSL=false";
    private static String username = "root";
    private static String password = "123456";

	@Override
	public List<BuildingSearchEntity> searchBuilding(BuildingSearchRequestDTO building) {
		StringBuilder sql = new StringBuilder("select distinct b.id, b.name, d.name as district, b.ward, b.street, b.numberofbasement, b.managername, b.managerphonenumber, b.floorarea, \r\n"
				+ "ra.value as rentarea, b.rentprice, b.servicefee, b.brokeragefee\r\n"
				+ "from building as b\r\n"
				+ "left join district as d on b.districtid = d.id\r\n"
				+ "left join rentarea as ra on ra.buildingid = b.id\r\n"
				+ "left join assignmentbuilding as ab on b.id = ab.buildingid\r\n"
				+ "left join buildingrenttype as brt on brt.buildingid = b.id\r\n"
				+ "left join renttype as rt on rt.id = brt.renttypeid\r\n"
				+ "where 1 = 1 ");
		if(building.getName() != null && building.getName() != "") {
			sql.append("and b.name like '%" + building.getName() + "%' ");
		}
		if(building.getFloorArea() != null) {
			sql.append("and b.floorarea = " + building.getFloorArea() + " ");
		}
		if(building.getDistrictId() != null) {
			sql.append("and b.districtid = " + building.getDistrictId() + " ");
		}
		if(building.getWard() != null && building.getWard() != "") {
			sql.append("and b.ward like '%" + building.getWard() + "%' ");
		}
		if(building.getStreet() != null && building.getStreet() != "") {
			sql.append("and b.street like '%" + building.getStreet() + "%' ");
		}
		if(building.getNumberOfBasement() != null) {
			sql.append("and b.numberofbasement = " + building.getNumberOfBasement() + " ");
		}
		if(building.getDirection() != null && building.getDirection() != "") {
			sql.append("and b.direction like '%" + building.getDirection() + "%' ");
		}
		if(building.getLevel() != null && building.getLevel() != "") {
			sql.append("and b.level like '%" + building.getLevel() + "%' ");
		}
		if(building.getAreaForm() != null) {
			sql.append("and ra.value >= " + building.getAreaForm() + " ");
		}
		if(building.getAreaTo() != null) {
			sql.append("and ra.value <= " + building.getAreaTo() + " ");
		}
		if(building.getRentPriceFrom() != null) {
			sql.append("and b.rentprice >= " + building.getRentPriceFrom() + " ");
		}
		if(building.getRentPriceTo() != null) {
			sql.append("and b.rentprice <= " + building.getRentPriceTo() + " ");
		}
		if(building.getManagerName() != null && building.getManagerName() != "") {
			sql.append("and b.managername like '%" + building.getManagerName() + "%' ");
		}
		if(building.getManagerPhoneNumber() != null && building.getManagerPhoneNumber() != "") {
			sql.append("and b.managerphonenumber like '%" + building.getManagerPhoneNumber() + "%' ");
		}
		if(building.getStaffId() != null) {
			sql.append("and ab.staffid = " + building.getStaffId() + " ");
		}
		if(building.getRentType() != null && building.getRentType().size() != 0) {
			String types = "";
	        for (int i = 0; i < building.getRentType().size(); i++) {
	            types += "'" + building.getRentType().get(i) + "'" + (i < building.getRentType().size() - 1 ? "," : "");
	        }
	        sql.append("and rt.code in(" + types + ") ");
		}
		sql.append("\n order by b.id");
		
		Map<Integer, BuildingSearchEntity> map = new LinkedHashMap<>();
		try(Connection conn = DriverManager.getConnection(url, username, password);
	    		Statement stmt = conn.createStatement();
	    		ResultSet rs = stmt.executeQuery(sql.toString());){
			
	    		while(rs.next()) {
	    			Integer id = rs.getObject("id", Integer.class);
	    			BuildingSearchEntity buildingSearchEntity = map.get(id);
	    			if(buildingSearchEntity == null) {
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
	    		
	        } 
	    	
	    	catch (SQLException e) {
	            e.printStackTrace();
	            System.out.println("Connected database failed...");
	        }
		
		return new ArrayList<>(map.values());
	}

}
