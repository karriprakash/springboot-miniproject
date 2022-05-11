package com.spring.mini.project.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.spring.mini.project.exception.EmployeeNotFoundException;
import com.spring.mini.project.model.Employee;
import com.spring.mini.project.repo.EmployeeRepository;

@Service
public class EmployeeServiceImpl implements IEmployeeService {

	@Autowired
	private EmployeeRepository repo;

	// 1
	public Integer saveEmployee(Employee emp) {
		System.out.println("---------------");
		System.out.println(emp);
		double sal = emp.getSalary();
		double hra = sal * 20 / 100.0;
		double ta = sal * 10 / 100.0;
		emp.setHra(hra);
		emp.setTa(ta);
		Integer id = repo.save(emp).getId();
		return id;
	}

	// 2
	public Employee update(Employee emp) {
		// if given id is present or not
		if (repo.existsById(emp.getId())) {
			double hra = emp.getSalary() * (20 / 100.0);
			double ta = emp.getSalary() * (10 / 100.0);
			emp.setHra(hra);
			emp.setTa(ta);
		}
		return repo.save(emp);
	}

	// 3
	public void delete(Integer id) {

		// check ID exists or not before deleting

		repo.deleteById(getOne(id).getId());

	}

	// 4
	public Employee getOne(Integer id) {
		Optional<Employee> ope = repo.findById(id);
		if (ope.isPresent())
			return ope.get();
		else
			throw new EmployeeNotFoundException("Employee '" + id + "' Not Found");
	}

	// 5
	public List<Employee> getAll() {

		return repo.findAll().stream().
				sorted(
						(e1, e2) -> e2.getId().compareTo(e1.getId()))
				.collect(Collectors.toList());
	}

	// 6
	@Override
	public boolean isEmployeeExistByName(String ename) {

		return repo.getCountOfEmployees(ename) > 0;
	}
	
	// 7
	@Override
	public Page<Employee> getAllEmployees(Pageable pageable) {
		
		Page<Employee> page = repo.findAll(pageable);
		
		return page;
	}
}
