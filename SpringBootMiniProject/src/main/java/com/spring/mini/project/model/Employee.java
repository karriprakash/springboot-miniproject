package com.spring.mini.project.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="emptab")
public class Employee {

	@Id
	@Column(name="eid")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="ename")
	private String name;
	
	@Column(name="esal")
	private Double salary;
	
	@Column(name="edept")
	private String department;
	
	@Column(name="ehra")
	private Double hra;
	
	@Column(name="eta")
	private Double ta;
}
