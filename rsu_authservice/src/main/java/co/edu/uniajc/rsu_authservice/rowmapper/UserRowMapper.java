package co.edu.uniajc.rsu_authservice.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import co.edu.uniajc.rsu_authservice.model.User;

public class UserRowMapper implements RowMapper<User> {

	public User mapRow(ResultSet rs, int rowNum) throws SQLException{
		
		User user=new User();
		
		user.setIdUser(rs.getLong("id_user"));
		user.setUsername(rs.getString("username"));
		user.setPassword(rs.getString("password"));
		user.setName(rs.getString("name"));
		user.setRole(rs.getString("role"));
		user.setState(rs.getString("state"));
		return user;
	}
		
}
