package com.example.demo.model;

import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "roles")
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(unique = true)
	private String name;
	
	@ManyToMany(mappedBy = "roles")
	private Set<User> users;

	public Role() {
		super();
	}

	public Role(Long id, String name, Set<User> users) {
		super();
		this.id = id;
		this.name = name;
		this.users = users;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}
	
}
