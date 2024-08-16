package com.user.crud.dto;

public class UserDTO {
	private Long id;
	private String username;
	private Boolean active;

	public UserDTO(Long id, String username, Boolean active) {
		this.id = id;
		this.username = username;
		this.active = active;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
}
