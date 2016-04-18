package com.training.hibernate.dao.impl;

import com.training.hibernate.model.Person;
import com.training.hibernate.model.Address;
import com.training.hibernate.model.Contact;
import com.training.hibernate.model.Role;
import com.training.hibernate.dao.PersonDao;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import java.util.List;

public class PersonDaoImpl implements PersonDao{

	private SessionFactory sessionFactory;

	public PersonDaoImpl(){}

	public PersonDaoImpl(SessionFactory sessionFactory){
		this.sessionFactory = sessionFactory;
	}

	@Override
	public List<Person> getAllPersons(){
		Session session = sessionFactory.openSession();
		List<Person> persons = (List<Person>) session.createCriteria(Person.class)
			.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
			.list();
		session.close();
		return persons;
	}

	@Override
	public Person getPersonById(int id) {
		Session session = sessionFactory.openSession();
		Person person = (Person) session
			.createCriteria(Person.class)
			.add(Restrictions.eq("id",id))
			.uniqueResult();
		session.close();	
		return person;
	}

	@Override
	public List<Person> searchPerson(String lastName, String firstName, String middleName, String role) {
		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(Person.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		if (!lastName.isEmpty()){
			criteria.add(Restrictions.eq("lastName",lastName));	
		}
		if(!firstName.isEmpty()){
			criteria.add(Restrictions.eq("firstName",firstName));	
		}
		if(!middleName.isEmpty()){
			criteria.add(Restrictions.eq("middleName",middleName));	
		}
		if (!role.isEmpty()){
			criteria.createAlias("roles","role");
			criteria.add(Restrictions.eq("role.role",role));
		}
		List<Person> persons = (List<Person>) criteria.list();
		session.close();
		return persons;
	}

	@Override
	public Person saveOrUpdatePerson(Person person) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		if(person.getId() == 0){
			session.save(person);
		}
		else{
			session.update(person);
		}
		session.getTransaction().commit();
		session.close();
		return person;
	}

	@Override
	public Person deletePerson(int id){
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Person person = (Person) session.get(Person.class, id);
		session.delete(person);
		session.getTransaction().commit();
		session.close();
		return person;
	}

	@Override
	public Role getRoleByName(String name){
		Session session = sessionFactory.openSession();
		Role role = (Role) session.createCriteria(Role.class)
			.add(Restrictions.eq("role",name)).uniqueResult();
		session.close();	
		return role;
	}

	@Override
	public List<Role> getRoles(){
		Session session = sessionFactory.openSession();
		List<Role> roles = (List<Role>) session.createCriteria(Role.class)
			.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
		session.close();	
		return roles;
	}
}