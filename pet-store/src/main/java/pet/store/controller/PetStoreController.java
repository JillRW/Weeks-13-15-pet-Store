package pet.store.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import pet.store.controller.model.PetStoreData;
import pet.store.controller.model.PetStoreData.PetStoreCustomer;
import pet.store.controller.model.PetStoreData.PetStoreEmployee;
import pet.store.service.PetStoreService;

@RestController
@RequestMapping("/pet_store")
@Slf4j
public class PetStoreController {
	
	@Autowired
	private PetStoreService petStoreService;
	
	@PostMapping("/pet_store")
	@ResponseStatus(code = HttpStatus.CREATED)
	public PetStoreData insertPetStore(@RequestBody PetStoreData petStoreData) {
		log.info("Creating pet_store {}", petStoreData);
		return petStoreService.savePetStore(petStoreData);
		
	}
	@PutMapping("/pet_store/{petStoreId}")
	public PetStoreData updatePetStoreData(@PathVariable Long petStoreId,
			@RequestBody PetStoreData petStoreData) {
		petStoreData.setPetStoreId(petStoreId);
		log.info("Updating petStore {}", petStoreData);
		return petStoreService.savePetStore(petStoreData);
	}
	
//	1.	Create a method in the controller that will add an employee to the employee table. The method should be annotated with @PostMapping and @ResponseStatus. 
//	a.	@PostMapping: This must be configured to allow the caller to send an HTTP POST request to "/pet_store/{petStoreId}/employee", where {petStoreId}  is the ID of the pet store in which to add the employee (for example, "/pet_store/1/employee"). Remember that the "/pet_store" part of the HTTP URI is defined at the class level in the @RequestMapping annotation.
//	b.	@ResponseStatus: This should be configured to return a 201 (Created) status code.
//	2.	The method should be public and return a PetStoreEmployee object.
//	3.	The method should accept the pet store ID and the PetStoreEmployee object as parameters. The pet store ID is passed in the URI and the PetStoreEmployee object is passed as JSON in the request body.
//	4.	Log the request.
//	5.	The method should call the saveEmployee method in the pet store service and should return the results of that method call.
//
	@PostMapping("/pet_store/{petStoreId}/employee")
	@ResponseStatus(code = HttpStatus.CREATED)
	public PetStoreEmployee petStoreEmployee(@PathVariable Long petStoreId, @RequestBody PetStoreEmployee petStoreEmployee) {
		
		log.info("Creating employee {} for petStore with ID={}", petStoreEmployee, petStoreId);
		
		return petStoreService.saveEmployee(petStoreId, petStoreEmployee);
		
	}
	
	// ADD STORE CUSTOMER
	
	@PostMapping("/pet_store/{petStoreId}/customer")
	@ResponseStatus(code = HttpStatus.CREATED)
	public PetStoreCustomer petStoreCustomer(@PathVariable Long petStoreId, @RequestBody PetStoreCustomer petStoreCustomer) {
		
		log.info("Creating customer {} for petStore with ID={}", petStoreCustomer, petStoreId);
		
		return petStoreService.saveCustomer(petStoreId, petStoreCustomer);
		
	
	}
	
	//	Add a method to the controller that returns List<PetStoreData>
	

	@GetMapping("/pet_store")
	public List<PetStoreData> retrieveAllPetStores() {
		log.info("Retrieving all Pet Stores");
		return petStoreService.retrieveAllPetStores();
		
	}
	// Add a controller method to retrieve a single pet store given the pet store ID. 
	//It will be very similar to the retrieve all pet stores method except that the @GetMapping 
	//annotation will take the pet store ID that is passed to the method as a parameter. Create/call the method in the service class.
	@GetMapping("/pet_store/{petStoreId}") // do I need curly brackets around 'petStoreId'? yes- to represent a value
	public PetStoreData retrievePetStoreById(@PathVariable Long petStoreId) {
		log.info("Retrieving a Pet Store by ID");
		return petStoreService.retrievePetStoreById(petStoreId);
	
}
	
//	1.	Add the deletePetStoreById() method in the controller.
//	a.	It should take the pet store ID as a parameter (remember to use @PathVariable).
//	b.	Add the @DeleteMapping annotation. This should specify that the pet store ID is part of the URI. For example, the URI should be http://localhost:8080/pet_store/{ID}, where {ID} is the ID of the pet store to delete.
//	c.	Log the request.
	
	@DeleteMapping("/pet_store/{petStoreId}")
	public Map<String, String> deletePetStoreById(@PathVariable Long petStoreId) {
		log.info("Deleting a Pet Store by ID");
		
//		d.	Call the deletePetStoreById() method in the service, passing the pet store ID as a parameter.
//		e.	Return a Map<String, String> where the key is "message" and the value is a deletion successful message.
		
		petStoreService.deletePetStoreById(petStoreId);
		return Map.of("message", "Deleting pet store with ID= " + petStoreId + "was successful");
	}
	
	
	
	
	
}