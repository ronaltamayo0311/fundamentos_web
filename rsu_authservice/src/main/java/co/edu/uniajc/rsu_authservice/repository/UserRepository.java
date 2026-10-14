package co.edu.uniajc.rsu_authservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import co.edu.uniajc.rsu_authservice.model.User;
import co.edu.uniajc.rsu_authservice.rowmapper.UserRowMapper;

@Repository
public class UserRepository {

	private final JdbcTemplate jdbcTemplate;
	
	public UserRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate=jdbcTemplate;
	}
	
	public Optional<User> findByUsername(String username){
		try {
			String sql = "SELECT id_user, username, password, name, role, state FROM users WHERE username=?";
			User user = jdbcTemplate.queryForObject(sql, new UserRowMapper(), username);
			return Optional.ofNullable(user);
			
		}catch (EmptyResultDataAccessException ex) {
			return Optional.empty();		
		}
	}
	
	public List<User> showAll(){
		List<User> users = null;
		try {
			String sql = "SELECT id_user, username, password, name, role, state FROM users";
			users = jdbcTemplate.query(sql, new UserRowMapper());
			return users;
		}catch (EmptyResultDataAccessException ex) {
			return users;		
		}
	}


	
	public Long getNextId() {
        String sql = "SELECT COALESCE(MAX(id_user), 0) + 1 FROM users";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }

    public int create(User user) {
        String sql = "INSERT INTO users (id_user, username, password, name, role, state) VALUES (?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                user.getIdUser(),
                user.getUsername(),
                user.getPassword(),
                user.getName(),
                user.getRole(),
                user.getState());
    }
	
	
}
