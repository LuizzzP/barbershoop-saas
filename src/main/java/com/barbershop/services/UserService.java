package com.barbershop.services;

import java.util.ArrayList;
import java.util.List;

import com.barbershop.entities.Appointment;
import com.barbershop.entities.ClientUser;
import com.barbershop.entities.ProfessionalUser;
import com.barbershop.entities.Shop;
import com.barbershop.entities.User;
import com.barbershop.enums.ClientType;
import com.barbershop.exceptions.EmailAlreadyExistsException;
import com.barbershop.exceptions.EmailDoesNotExistException;
import com.barbershop.exceptions.WrongPasswordException;
import com.barbershop.repositories.UserRepository;

public class UserService {
	
	private final UserRepository repository = new UserRepository();
	
	
	public void registerUser(String email, String name, String passwordHash, char cOrP) {
		Long id = repository.getNextId();
		if(repository.emailExists(email)) throw new EmailAlreadyExistsException("Esse email já possui conta.");
		User user = cOrP == 'c' 
				? new ClientUser(id, email, name, passwordHash, ClientType.CLIENT) 
				: new ProfessionalUser(id, email, name, passwordHash, ClientType.PROFESSIONAL);
		repository.saveUser(user);
	}
	
	public User loginUser(String email, String passwordHash) {
		User user = repository.findByEmail(email).orElseThrow(() -> new EmailDoesNotExistException("Essa conta não existe.")); 
		if(user.checkPassword(passwordHash)) return user;
		else throw new WrongPasswordException("Senha errada.");
	}
	
	public List<Appointment> getProfessionalListTotalAppointments(ProfessionalUser user) {
		List<Shop> listShops = user.getListShop();
		List<Appointment> listAppointments = new ArrayList<>();
		for(int i = 0; i < listShops.size(); i++) {
			listAppointments.addAll(listShops.get(i).getAppointmentList());
		}
		return listAppointments;
	}
}
