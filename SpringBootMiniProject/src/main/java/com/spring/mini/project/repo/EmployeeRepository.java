package com.spring.mini.project.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.spring.mini.project.model.Employee;

public interface EmployeeRepository 
					extends JpaRepository<Employee, Integer> {

	@Query("SELECT count(name) FROM Employee WHERE name=:ename")
	public Integer getCountOfEmployees(String ename);
}
