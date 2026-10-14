package co.edu.uniajc.rsu_authservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import co.edu.uniajc.rsu_authservice.model.Role;
import co.edu.uniajc.rsu_authservice.model.User;
import co.edu.uniajc.rsu_authservice.rowmapper.RoleRowMapper;
import co.edu.uniajc.rsu_authservice.rowmapper.UserRowMapper;

@Repository
public class RoleRepository {

	private final JdbcTemplate jdbcTemplate;
	
	public RoleRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate=jdbcTemplate;
	}
	
	public List<Role> showAll(){
		List<Role> roles = null;
		try {
			String sql = "SELECT id_role, name, code, description, date_creation, state FROM role";
			roles = jdbcTemplate.query(sql, new RoleRowMapper());
			return roles;
		}catch (EmptyResultDataAccessException ex) {
			return roles;		
		}
	}
	
	public Optional<Role> findByCode(String code){
		try {
			String sql = "SELECT id_role, name, code, description, date_creation, state FROM role WHERE username=?";
			Role role = jdbcTemplate.queryForObject(sql, new RoleRowMapper(), code);
			return Optional.ofNullable(role);
			
		}catch (EmptyResultDataAccessException ex) {
			return Optional.empty();		
		}
	}
	
	public Long getNextId() {
        String sql = "SELECT COALESCE(MAX(id_role), 0) + 1 FROM roles";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }

    public int create(Role role) {
        String sql = "INSERT INTO role (id_role, name, code, description) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                role.getIdRole(),
                role.getName(),
                role.getCode(),
                role.getDescription());
    }
}
