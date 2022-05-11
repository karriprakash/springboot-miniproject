package com.spring.mini.project.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.spring.mini.project.model.Employee;

public interface IEmployeeService {

	Integer saveEmployee(Employee emp);
	
	Employee update(Employee emp); 
	
	void delete(Integer id);
	
	Employee getOne(Integer id);
	
	List<Employee> getAll();
	
	boolean isEmployeeExistByName(String ename);
	Page<Employee> getAllEmployees(Pageable pageable);
}
