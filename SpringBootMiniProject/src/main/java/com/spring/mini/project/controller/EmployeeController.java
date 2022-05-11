package com.spring.mini.project.controller;

import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.mini.project.captcha.CaptchaUtil;
import com.spring.mini.project.exception.CustomException;
import com.spring.mini.project.exception.EmployeeNotFoundException;
import com.spring.mini.project.model.Employee;
import com.spring.mini.project.service.IEmployeeService;

import cn.apiclub.captcha.Captcha;

@Controller
@RequestMapping("/employee")
public class EmployeeController {
	
	private static final Logger LOG = LoggerFactory.getLogger(EmployeeController.class);
	
	@Autowired
	private IEmployeeService service;

	private void setupCaptcha(Employee e) {
		Captcha cap = CaptchaUtil.createCaptcha(200, 80);
		e.setHidden(cap.getAnswer());
		e.setCaptcha("");
		e.setImage(CaptchaUtil.encodeBase64(cap));
	}
	// Show Register Page
	/**
	 * If End-User enters /register, Get Type then we should display One Register
	 * page in browser
	 */

	@GetMapping("/register")
	public String showReg(Model model) {
		Employee e = new Employee();
		setupCaptcha(e);
		model.addAttribute("employee", e);
		model.addAttribute("message", "");
		return "EmployeeRegister";
	}

	// save(): Click Form Submit

	@PostMapping("/save")
	public String saveEmp(@ModelAttribute("employee") Employee employee, Model model) {
		String message,page = null;
		LOG.info("Entered Into Save Method");
		try {
			System.out.println(employee.getCaptcha());
			System.out.println(employee.getHidden());
			if(employee.getCaptcha().equals(employee.getHidden())) {
		Integer id = service.saveEmployee(employee);
		message= "Employee id: " + id + " is successfully saved";
		page = "redirect:all";
			}
			else {
				message="Captcha is incorrect";
				setupCaptcha(employee);
				page = "EmployeeRegister";
			}
		LOG.debug(message);
		}catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage() + " Problem in save{}");
			message= "Unable To Create Employee";
		}
		LOG.info("Control Left the Save Method");
		model.addAttribute("message", message);
		return page;
	}

	// display all rows
	@GetMapping("/all")
	public String getAll(Model model,
			@PageableDefault(page = 0,size = 3) Pageable pageable,
			HttpServletRequest request) {
		commonData(model,pageable);
		request.getSession().setAttribute("size", pageable.getPageSize());
		System.out.println("size = "+pageable.getPageSize());
		model.addAttribute("size", 0);
		return "EmployeeData";
	}

	// delete by id
	@GetMapping("/delete")
	public String deleteEmp(
			@RequestParam Integer id,
			Model model
			
			) {
		LOG.info("Entered Into Delete Method");
		String message = null;
		try {
		service.delete(id);
		message = "Employee with id:" +id + " was deleted successfully!!";
		LOG.debug(message + " employee is deleted in delete{}");
		}catch (EmployeeNotFoundException e) {
			e.printStackTrace();
			LOG.error("Error Occured at Delete{} -> " + e.getMessage());
			message = e.getMessage();
		}
		commonData(model,PageRequest.of(0, 3));
		LOG.info("Left the Delete Method");
		model.addAttribute("deleted", message);
		model.addAttribute("size", 0);
		return "EmployeeData";
	}
	
	
	// show Data in edit (by id)
	@GetMapping("/edit")
	public String editEmp(
			@RequestParam Integer id,
			Model model
			) {
		String page = null;
		try {
		Employee emp = service.getOne(id);
		
			model.addAttribute("employee", emp);
			page = "EditEmployee";
		}catch (EmployeeNotFoundException e) {
			page = "EmployeeData";
			model.addAttribute("message", e.getMessage());
		}
		
		return page;
	}
	/***
	 * When there is a common data through-out 
	 * the class then we can go for a private method
	 * which can be visible for this class members
	 * @param employee
	 * @return void
	 */
	public void commonData(Model model,Pageable pageable) {
		Page<Employee> page = service.getAllEmployees(pageable);
		model.addAttribute("page", page);
		model.addAttribute("list", page.getContent());
	}
	// Update Data
	
	@PostMapping("/update")
	public String updateEmp(
			@ModelAttribute Employee employee,
			Model model
			) {		
		service.update(employee);
		//update the data 
		commonData(model,PageRequest.of(0, 3));
		model.addAttribute("message", "Employee '"+ employee.getId()+ "' is Updated!");
		return "EmployeeData";
	}
	
	@GetMapping("exp")
	public String getException(Model model) {
		if(new Random().nextInt()<10)
		throw new RuntimeException("");
		commonData(model,PageRequest.of(0, 3));
		return "EmployeeData";
	}
	@GetMapping("exp/5xx")
	public String getException5xx(Model model) {
		if(new Random().nextInt()<8)
			throw new CustomException("This is a custom exception");
		commonData(model, PageRequest.of(0, 3));
		return "EmployeeData";
	}
	
	// Ajax Validation
	/***
	 * Read ename and return message back
	 * Not Page Name
	 */
	
	@GetMapping("/validate")
	public @ResponseBody String validateEName(
			@RequestParam String ename
			) {
		String message = "";
		if(service.isEmployeeExistByName(ename)) {
			message = ename + ", already exists!!";			
		}else {
			message = "";
		}	
		return message;
	}
	
	@GetMapping("setvalue")
	public @ResponseBody Integer setValue(
			@RequestParam Integer value,
			Model model,
			HttpServletRequest request
			) {
		request.getSession().setAttribute("size", value);
		System.out.println(value);
		return value;
	}
}
