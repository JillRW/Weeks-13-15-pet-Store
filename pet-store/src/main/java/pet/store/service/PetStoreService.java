package pet.store.service;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pet.store.controller.model.PetStoreData;
import pet.store.controller.model.PetStoreData.PetStoreCustomer;
import pet.store.controller.model.PetStoreData.PetStoreEmployee;
import pet.store.dao.CustomerDao;
import pet.store.dao.EmployeeDao;
import pet.store.dao.PetStoreDao;
import pet.store.entity.Customer;
import pet.store.entity.Employee;
import pet.store.entity.PetStore;

@Service
public class PetStoreService {
	@Autowired
	private PetStoreDao petStoreDao;
	@Autowired
	private EmployeeDao employeeDao;
	@Autowired
	private CustomerDao customerDao;

	@Transactional(readOnly = false)
	public PetStoreData savePetStore(PetStoreData petStoreData) {
		Long petStoreId = petStoreData.getPetStoreId();
		PetStore petStore = findOrCreatePetStore(petStoreId);
		copyPetStoreFields(petStore, petStoreData);
		return new PetStoreData(petStoreDao.save(petStore));

	}

	private void copyPetStoreFields(PetStore petStore, PetStoreData petStoreData) {
		petStore.setPetStoreId(petStoreData.getPetStoreId());
		petStore.setPetStoreName(petStoreData.getPetStoreName());
		petStore.setPetStoreAddress(petStoreData.getPetStoreAddress());
		petStore.setPetStoreCity(petStoreData.getPetStoreCity());
		petStore.setPetStoreState(petStoreData.getPetStoreState());
		petStore.setPetStoreZip(petStoreData.getPetStoreZip());
		petStore.setPetStorePhone(petStoreData.getPetStorePhone());

	}

	private PetStore findOrCreatePetStore(Long petStoreId) {
		PetStore petStore;

		if (Objects.isNull(petStoreId)) {
			petStore = new PetStore();

		} else {
			petStore = findPetStoreById(petStoreId);
		}
		return petStore;
	}

	private PetStore findPetStoreById(Long petStoreId) {
		return petStoreDao.findById(petStoreId)
				.orElseThrow(() -> new NoSuchElementException("Pet Store with ID=" + petStoreId + " was not found."));

	}

	@Transactional(readOnly = false)
	public PetStoreEmployee saveEmployee(Long petStoreId, PetStoreEmployee petStoreEmployee) {

		PetStore petStore = findPetStoreById(petStoreId);
		Long employeeId = petStoreEmployee.getEmployeeId();
		Employee employee = findOrCreateEmployee(petStoreId, employeeId);
		copyEmployeeFields(employee, petStoreEmployee);
		employee.setPetStore(petStore);
		petStore.getEmployees().add(employee);

		return new PetStoreEmployee(employeeDao.save(employee));
	}

	private Employee findOrCreateEmployee(Long petStoreId, Long employeeId) {
		Employee employee = new Employee();

		// should this be petStoreId instead of employeeId??
		if (Objects.isNull(employeeId)) {
			employee = new Employee();

		} else {

			employee = findEmployeeById(petStoreId, employeeId);
		}

		return employee;
	}

	private Employee findEmployeeById(Long petStoreId, Long employeeId) {
		// Note that findById returns an Optional. If the Optional is
		// empty .orElseThrow throws a NoSuchElementException. If the
		// Optional is not empty an Employee is returned.
		Employee employee = employeeDao.findById(employeeId)
				.orElseThrow(() -> new NoSuchElementException("Employee with given ID not found"));

//		  if employee pet store ID equals petStoreId
//		  then return employee
//		  else throw IllegalArgumentException(msg)
		if (employee.getPetStore().getPetStoreId().equals(petStoreId)) {
			return employee;
		} else {
			throw new IllegalArgumentException("Invalid ID given");
		}
	}

	private void copyEmployeeFields(Employee employee, PetStoreEmployee petStoreEmployee) {
		employee.setEmployeeId(petStoreEmployee.getEmployeeId());
		employee.setEmployeeFirstName(petStoreEmployee.getEmployeeFirstName());
		employee.setEmployeeLastName(petStoreEmployee.getEmployeeLastName());
		employee.setEmployeePhone(petStoreEmployee.getEmployeePhone());
		employee.setEmployeeJobTitle(petStoreEmployee.getEmployeeJobTitle());

	}

	@Transactional(readOnly = false)
	public PetStoreCustomer saveCustomer(Long petStoreId, PetStoreCustomer petStoreCustomer) {
		PetStore petStore = findPetStoreById(petStoreId);
		Long customerId = petStoreCustomer.getCustomerId();
		Customer customer = findOrCreateCustomer(petStoreId, customerId);

		copyCustomerFields(customer, petStoreCustomer);
		

		customer.getPetStores().add(petStore);
		petStore.getCustomers().add(customer);

		return new PetStoreCustomer(customerDao.save(customer));
	}

	private Customer findOrCreateCustomer(Long petStoreId, Long customerId) {
		Customer customer = new Customer();
		if (Objects.isNull(customerId)) {
			customer = new Customer();

		} else {
			customer = findCustomerById(petStoreId, customerId);
		}
		return customer;
	}

	private Customer findCustomerById(Long petStoreId, Long customerId) {
					  // Note that findById returns an Optional. If the Optional is
					  // empty .orElseThrow throws a NoSuchElementException. If the
					  // Optional is not empty an Employee is returned.
					  Customer customer = customerDao.findById(customerId).orElseThrow(() -> new
							  NoSuchElementException("Customer with given ID not found"));
					  boolean found = false;
					  for (PetStore petStore : customer.getPetStores()) {
					  if(petStore.getPetStoreId().equals(petStoreId)) {
						  found = true;
						  break;
					  }
					  }
						  if( found = false) {
							  throw new IllegalArgumentException("Invalid ID given.");
						  
					  } else {
						  return customer;
					  }
					 }
					  
	

	private void copyCustomerFields(Customer customer, PetStoreCustomer petStoreCustomer) {
		customer.setCustomerId(petStoreCustomer.getCustomerId());
		customer.setCustomerFirstName(petStoreCustomer.getCustomerFirstName());
		customer.setCustomerLastName(petStoreCustomer.getCustomerLastName());
		customer.setCustomerEmail(petStoreCustomer.getCustomerEmail());
					  

		}
	// why isn't there a red squiggly under findAll()?
	// Call the findAll() method in the pet store DAO. Convert the List of PetStore objects to a List of PetStoreData objects.
	@Transactional(readOnly = true)
	public List<PetStoreData> retrieveAllPetStores() {
		// is this correct?? -> petStoreDao.findAll();
	
		List<PetStore> petStores = petStoreDao.findAll();
		List<PetStoreData> result = new LinkedList<>();
		
		for(PetStore petStore : petStores) {
			PetStoreData psd = new PetStoreData(petStore);
			
			psd.getCustomers().clear();
			psd.getEmployees().clear();
			
			result.add(psd);
		
		}
		return result;
			
		}
	@Transactional(readOnly = true)
	public PetStoreData retrievePetStoreById(Long petStoreId) {
		PetStore petStore = findPetStoreById(petStoreId);
		return new PetStoreData(petStore);
		
	}

	public void deletePetStoreById(Long petStoreId) {
//		2.	Add the deletePetStoreById() method in the service class.
//		a.	Call findPetStoreById() to retrieve the PetStore entity.
//		b.	Call the delete() method in the PetStoreDao interface, passing in the PetStore entity.
		PetStore petStore = findPetStoreById(petStoreId);
		petStoreDao.delete(petStore);
		
		
	}
		
		
	}

	
