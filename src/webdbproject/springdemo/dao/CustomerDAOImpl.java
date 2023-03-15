package webdbproject.springdemo.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import webdbproject.springdemo.entity.Customer;

@Repository
public class CustomerDAOImpl implements CustomerDAO {

	// need to inject the session factory
	@Autowired
	private SessionFactory sessionFactory; 
	
	@Override
	public List<Customer> getCustomers() {
		
		// get current session
		Session curSession = sessionFactory.getCurrentSession();
		
		// create query 
		Query<Customer> theQuery = curSession.createQuery("from Customer order by lastName", Customer.class);
		
		// execute query and get result list
		List<Customer> customers = theQuery.getResultList();
		
		// return results
		return customers;
	}

	@Override
	public void saveCustomer(Customer theCustomer) {
		// get current hibernate session
		Session curSession = sessionFactory.getCurrentSession();
		
		// save the customer
		curSession.saveOrUpdate(theCustomer);
	}

	@Override
	public Customer getCustomer(int theId) {
		Session curSession = sessionFactory.getCurrentSession();
		
		Customer tempCustomer = curSession.get(Customer.class, theId);
		
		return tempCustomer;
	}

	@Override
	public void deleteCustmer(int theId) {
		Session curSession = sessionFactory.getCurrentSession();
		
		Query theQuery = curSession.createQuery("delete from Customer where id=:customerId");
		theQuery.setParameter("customerId", theId);
		theQuery.executeUpdate();
	}

	@Override
	public List<Customer> searchCustomers(String theSearchName) {
		Session curSession = sessionFactory.getCurrentSession();
		
		Query theQuery = null;
		
		if(theSearchName != null && theSearchName.trim().length() > 0) {
			theQuery = curSession.createQuery("from Customer where lower(firstName) like :theName or lower(lastName) like :theName", Customer.class);
			theQuery.setParameter("theName", "%" + theSearchName.toLowerCase() + "%");
		}
		else {
			theQuery = curSession.createQuery("from Customer", Customer.class);
		}
		
		List<Customer> theCustomers = theQuery.getResultList();
		
		return theCustomers;
	}

}
