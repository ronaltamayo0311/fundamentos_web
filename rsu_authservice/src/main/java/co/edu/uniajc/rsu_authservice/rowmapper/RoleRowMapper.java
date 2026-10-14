package co.edu.uniajc.rsu_authservice.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import co.edu.uniajc.rsu_authservice.model.Role;

public class RoleRowMapper implements RowMapper<Role> {

	public Role mapRow(ResultSet rs, int rowNum) throws SQLException{
		
		Role role=new Role();
		
		return Role.builder().idRole(rs.getLong("id_role"))
				.name(rs.getString("name"))
				.code(rs.getString("code"))
				.description(rs.getString("description"))
				.dateCreation(rs.getString("date_creation"))
				.state(rs.getString("state"))
				.build();
	}
		
}
