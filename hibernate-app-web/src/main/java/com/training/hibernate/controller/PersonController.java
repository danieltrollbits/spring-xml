package com.training.hibernate.controller;

import java.util.List;
import java.util.ArrayList;

import com.training.hibernate.services.PersonService;
import com.training.hibernate.dto.PersonDto;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PersonController extends MultiActionController {
	
	private PersonService personService;

	public void setPersonService(PersonService personService){
		this.personService = personService;
	}

	public ModelAndView list(HttpServletRequest request, HttpServletResponse response) throws Exception{
		List<PersonDto> personDtos = personService.getAllPersons();
		ModelAndView model = new ModelAndView("index");
		model.addObject("persons",personDtos);
		model.addObject("roles",personService.getRoles());
		return model;
	}

	public ModelAndView search(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<PersonDto> personDtos = null;
		ModelAndView model = new ModelAndView("index");

		if (request.getParameter("lastName").isEmpty() && request.getParameter("firstName").isEmpty()
			 && request.getParameter("middleName").isEmpty() && request.getParameter("roles").isEmpty()){
			personDtos = personService.getAllPersons();		
		}
		else{
			personDtos = personService.searchPerson(request.getParameter("lastName"),request.getParameter("firstName"),
				request.getParameter("middleName"),request.getParameter("roles"));
		}
		model.addObject("persons",personDtos);
		model.addObject("roles",personService.getRoles());
		return model;
	}

	public ModelAndView view(HttpServletRequest request, HttpServletResponse response) throws Exception {
		PersonDto personDto = new PersonDto();
		if (request.getParameterMap().containsKey("personId") && request.getParameterValues("personId").length == 1){
			personDto = personService.getPersonById(Integer.parseInt(request.getParameter("personId")));
			ModelAndView model = new ModelAndView("person");
			model.addObject("person",personDto);
			model.addObject("view","view");
    		return model;
    	}
    	else{
    		return new ModelAndView("redirect:/person/list.htm?message=Please select one person");
    	}
	}

	public ModelAndView add(HttpServletRequest request, HttpServletResponse response) throws Exception{
		ModelAndView model = new ModelAndView("person");
		return model;
	}

	public ModelAndView update(HttpServletRequest request, HttpServletResponse response) throws Exception{
		PersonDto personDto = new PersonDto();

		if (request.getParameterMap().containsKey("personId") && request.getParameterValues("personId").length == 1){
			personDto = personService.getPersonById(Integer.parseInt(request.getParameter("personId")));
			ModelAndView model = new ModelAndView("person");
			if (request.getParameterMap().containsKey("view")){
				model.addObject("view","view");
			}
			model.addObject("person",personDto);
    		return model;
    	}
    	else{
    		return new ModelAndView("redirect:/person/list.htm?message=Please select one person");
    	}
	}

	public ModelAndView delete(HttpServletRequest request, HttpServletResponse response) throws Exception{
		if(request.getParameterMap().containsKey("personId")){
			for(String id : request.getParameterValues("personId")){
				personService.deletePerson(Integer.parseInt(id));
			}
			return new ModelAndView("redirect:/person/list.htm?message=Person/s deleted");
		}else{
			return new ModelAndView("redirect:/person/list.htm?message=Please select person/s to delete");
		}
	}

	public ModelAndView save(HttpServletRequest request, HttpServletResponse response) throws Exception {
		PersonDto personDto = personService.createPersonDto(request.getParameter("personId"), request.getParameter("firstName"),
				request.getParameter("middleName"), request.getParameter("lastName"),
				request.getParameter("gender"), request.getParameter("birthdate"), request.getParameter("employed"),
				request.getParameter("gwa"), request.getParameter("street"), request.getParameter("houseNo"),
				request.getParameter("barangay"), request.getParameter("subdivision"), request.getParameter("city"),
				request.getParameter("zipcode"), request.getParameterValues("contactType"),
				request.getParameterValues("contactValue"), request.getParameterValues("contactId"),
				request.getParameterValues("role"),request.getParameterValues("savedContactValue"),request.getParameterValues("savedContactType"));

		boolean isRequired = personService.isRequired(request.getParameter("firstName"),
				request.getParameter("middleName"), request.getParameter("lastName"),
				request.getParameter("gender"), request.getParameter("employed"), request.getParameter("street"),
				request.getParameter("barangay"), request.getParameter("subdivision"),
				request.getParameter("city"), request.getParameter("zipcode"),
				request.getParameterValues("contactType"), request.getParameterValues("contactValue"),
				request.getParameterValues("role"));

		boolean isNumber = personService.isNumber(request.getParameter("houseNo"));
		boolean isDate = personService.isDate(request.getParameter("birthdate"));
		boolean isDecimal = personService.isDecimal(request.getParameter("gwa"));

		if (isRequired && isNumber && isDate && isDecimal){
			personService.saveOrUpdatePerson(personDto);
			return new ModelAndView("redirect:/person/list.htm?message="+"Person saved");
		}
		else{
			List<String> errors = new ArrayList<>();
			if(!isRequired){
				errors.add("Missing required fields");
			}
			if(!isDate){
				errors.add("Invalid date format");
			}
			if(!isNumber){
				errors.add("Invalid House no");
			}
			if(!isDecimal){
				errors.add("Invalid gwa");
			}
			ModelAndView model = new ModelAndView("person");
			model.addObject("errors",errors);
			model.addObject("person",personDto);
			return model;
		}	
	}

}
