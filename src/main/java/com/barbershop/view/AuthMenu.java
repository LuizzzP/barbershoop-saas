package com.barbershop.view;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Scanner;

import com.barbershop.entities.Appointment;
import com.barbershop.entities.ClientUser;
import com.barbershop.entities.ProfessionalUser;
import com.barbershop.entities.ServiceProvided;
import com.barbershop.entities.Shop;
import com.barbershop.entities.User;
import com.barbershop.entities.WorkShift;
import com.barbershop.enums.AppointmentStatus;
import com.barbershop.enums.ClientType;
import com.barbershop.services.BarbershopService;
import com.barbershop.services.UserService;

public class AuthMenu {
	private final Scanner sc = new Scanner(System.in);
	private final UserService userService = new UserService();
	private final BarbershopService barbershopService = new BarbershopService();
	private User user;
	
	public AuthMenu() {}
	
	public void menuProgram() {
		int option = 0;
		while(option != 3) {
			System.out.println("--- BARBERSHOP ---");
			System.out.println("Mais do que um corte, um estilo de vida.\n "
			+ "Agende seu horário e renove seu visual com quem entende do assunto.");
			System.out.println("1 - Entrar");
			System.out.println("2 - Registre-se");
			System.out.println("3 - Sair do programa");
			option = readMenuOption(3);
			
			if(option == 1) loginMenu();
			if(option == 2) registerMenu();
		}
		
		System.out.println("Programa fechado!");
		
		sc.close();
	}
	
	private int readInt(String prompt) {
	    while (true) {
	        System.out.print(prompt);
	        if (sc.hasNextInt()) {
	            int value = sc.nextInt();
	            sc.nextLine();
	            return value;
	        }
	        System.out.println("Valor inválido, digite um número.");
	        sc.next();
	    }
	}
	
	private double readDouble(String prompt) {
	    while (true) {
	        System.out.print(prompt);
	        if (sc.hasNextDouble()) {
	            double value = sc.nextDouble();
	            sc.nextLine();
	            return value;
	        }
	        System.out.println("Valor inválido, digite um número.");
	        sc.next();
	    }
	}
	
	
	public int readMenuOption(int totalOptions) {
		while(true) {
			
			int choice = readInt("Digite sua opção:");
			
			if(choice < 1 || choice > totalOptions) {
				System.out.println("Opção invalida.");
				continue;
			}
			
			return choice;
		}
	}
	
	public void registerMenu() {
		System.out.println("--- REGISTRO ---");
		System.out.print("Digite seu email:");
		String email = sc.nextLine();
		System.out.print("Digite sua senha:");
		String password = sc.nextLine();
		System.out.print("Digite seu nome:");
		String name = sc.nextLine();
		System.out.print("Cliente ou profissional? (c/p):");
		char cOrP = sc.next().charAt(0);
		sc.nextLine();
		try {
			userService.registerUser(email, name, password, cOrP);
			user = userService.loginUser(email, password);
			System.out.println("Bem vindo(a) " + user.getName() + "!");
			redirectingToMenu();
		}
		catch(RuntimeException e) {
			System.out.println(e.getMessage());
			registerMenu();
		}
		
	}
	
	public void loginMenu() {
		System.out.println("--- LOGIN ---");
		System.out.print("Digite seu email:");
		String email = sc.nextLine();
		System.out.print("Digite sua senha:");
		String password = sc.nextLine();
		try {
			user = userService.loginUser(email, password);
			redirectingToMenu();
		}
		catch(RuntimeException e) {
			System.out.println(e.getMessage());
			loginMenu();
		}
	}
	
	private String stringLoggedUser() {
		return "--- Usuario: " + user.getName() + " ---" + " " + user.getClientType();
	}
	
	public void redirectingToMenu() {
		if(user.getClientType() == ClientType.PROFESSIONAL) {
			professionalMenu();
			return;
		}
		
		if(user.getClientType() == ClientType.CLIENT) {
			clientMenu();
			return;
		}
	}
	
	public void professionalMenu() {
		System.out.println(stringLoggedUser());
		System.out.println("1 - Agenda");
		System.out.println("2 - Gerenciar Barbearias");
		System.out.println("3 - Sair");
		
		int choice = readMenuOption(3);
		
		
		if(choice == 1) {
			appointmentMenu();
			return;
		}
		
		if(choice == 2) {
			managerBarbershopMenu();
			return;
		}
	}
	
	public void appointmentMenu() {
		System.out.println(stringLoggedUser());
		System.out.println("--- AGENDA ---");
		List<Appointment> listAppointments = userService.getProfessionalListTotalAppointments((ProfessionalUser) user);
		for(int i = 0; i < listAppointments.size(); i++) {
			Appointment appointment = listAppointments.get(i);
			System.out.println(i + 1
					+ " - Cliente: " + appointment.getClient().getName()
					+ " , Barbearia: " + appointment.getShop().getName()
					+ ", Serviço: " + appointment.getService().getName()
					+ ", Inicio: " + appointment.getStartTime()
					+ ", Fim: " + appointment.getEndTime()
					+ ", Valor: $" + appointment.getService().getPrice()
					+ ", Status: " + appointment.getStatus());
		}
		System.out.println(listAppointments.size() + 1 + " - Voltar");
		Optional<Integer> choice = readChoice(listAppointments.size());
		if(choice.isEmpty()) {
			professionalMenu();
			return;
		}
		
		appointmentConfigMenu(listAppointments.get(choice.get()));
		
		
	}
	
	public void appointmentConfigMenu(Appointment appointment) {
		System.out.println("1 - Confirmar");
		System.out.println("2 - Cancelar");
		System.out.println("3 - Voltar");
		int choice = readMenuOption(3);
		
		try {
		    if(choice == 1) {
		        appointment.confirmAppointment();
		        System.out.println("Agendamento confirmado!");
		    }
		    else if(choice == 2) {
		        appointment.cancelAppointment();
		        System.out.println("Agendamento cancelado!");
		    }
		}
		catch(RuntimeException e) {
		    System.out.println(e.getMessage());
		    appointmentConfigMenu(appointment);
		    return;
		}
		appointmentMenu();
	}
	
	public void managerBarbershopMenu() {
		int n = 1;
		System.out.println(stringLoggedUser());
		System.out.println("--- GERENCIAR ---");
		List<Shop> listShop = ((ProfessionalUser) user).getListShop();
		for(Shop shop: listShop) {
			System.out.println(n + " - " + shop.getName());
			n++;
		}
		System.out.println(n + " - " + "Criar Nova Barbearia");
		System.out.println(n + 1 + " - Voltar");
		Optional<Integer> choice = readChoice(listShop.size() + 1);
		
		if(choice.isEmpty()) {
			professionalMenu();
			return;
		}
		
		if(choice.get() == n - 1) {
			createNewBarbershopMenu();
			return;
		}
		
		configBarbershopMenu(listShop.get(choice.get()));
		return;
	}
	
	public void configBarbershopMenu(Shop shop) {
		
		
		System.out.println("--- CONFIG " + shop.getName() + " ---");
		System.out.println("1 - Mudar nome");
		System.out.println("2 - Serviços");
		System.out.println("3 - Excluir barbearia");
		System.out.println("4 - Voltar");
		int option = readMenuOption(4);
		switch(option) {
			case 1:
				changeBarbershopNameMenu(shop);
			break;
			case 2:
				chooseServiceMenu(shop);
			break;
			case 3:
				deleteBarbershopMenu(shop);
			break;
			case 4:
				managerBarbershopMenu();
			break;
					
		}
	}
	
	public void deleteBarbershopMenu(Shop shop) {
		System.out.print("Confirme a exclusão da barbearia (s/n):");
		if(!sc.hasNext()) {
			System.out.println("Você precisa digitar uma das opções.");
			deleteBarbershopMenu(shop);
			return;
		}
		char sOrN = sc.next().charAt(0);
		if(sOrN == ('s')) {
			((ProfessionalUser) user).getListShop().remove(shop);
			System.out.println("Barbearia deletada.");
			managerBarbershopMenu();
			return;
		}
		else if(sOrN == ('n')) {
			System.out.println("Exclusão negada.");
			configBarbershopMenu(shop);
		}
		else {
			System.out.print("Opção invalida.");
			configBarbershopMenu(shop);
		}
	}
	
	public void changeBarbershopNameMenu(Shop shop) {
		System.out.print("Digite o novo nome:");
		String name = sc.nextLine();
		shop.setName(name);
		System.out.println("Nome alterado para " + name + " com sucesso!");
		configBarbershopMenu(shop);
	}
	
	public void chooseServiceMenu(Shop shop) {
		System.out.println("--- SERVIÇOS ---");
		System.out.println("Escolha o serviço.");
		List<ServiceProvided> serviceList = shop.getServiceList();
		for(int i = 0; i < serviceList.size(); i++){
			ServiceProvided service = serviceList.get(i);
			System.out.println(i + 1 + " - " + service.getName());
		}
		System.out.println(serviceList.size() + 1 + " - Voltar");
		Optional<Integer> choice = readChoice(serviceList.size());
		
		if(choice.isEmpty()) {
			configBarbershopMenu(shop);
			return;
		}
		
		serviceConfigMenu(serviceList.get(choice.get()), shop);
	}
	
	public void serviceConfigMenu(ServiceProvided service, Shop shop) {
		System.out.println("--- SERVIÇO " + service.getName() + " PREÇO:" + service.getPrice() + " ---");
		System.out.println("1 - Mudar nome");
		System.out.println("2 - Alterar preço");
		System.out.println("3 - Excluir serviço");
		System.out.println("4 - Voltar");
		int option = readMenuOption(4);
		switch(option) {
			case 1:
				System.out.print("Digite o novo nome:");
				String name = sc.nextLine();
				service.setName(name);
				System.out.println("Nome alterado para " + name + " com sucesso!");
			break;
			case 2:
			    System.out.print("Digite o novo preço: ");
			    while (!sc.hasNextDouble()) {
			        System.out.println("Valor inválido.");
			        sc.next();
			    }
			    double price = sc.nextDouble();
			    sc.nextLine();
			    service.setPrice(price);
			    System.out.println("Valor alterado para " + service.getPrice() + " com sucesso!");
			break;
			case 3:
				System.out.print("Confirme a exclusão do serviço " + service.getName() + " (s/n");
				char sOrN = sc.next().charAt(0);
				if(sOrN == ('s')) {
					shop.getServiceList().remove(service);
					System.out.println("Serviço deletado.");
					configBarbershopMenu(shop);
					return;
				}
				else if(sOrN == ('n')) {
					System.out.println("Exclusão negada.");
				}
				else {
					System.out.print("Opção invalida.");
				}
			break;
			case 4:
				configBarbershopMenu(shop);
				return;
			
			}
		serviceConfigMenu(service, shop);

	}
	
	public void createNewBarbershopMenu() {
	    System.out.print("Digite o nome da sua barbearia:");
	    String name = sc.nextLine();
	    Shop newShop = barbershopService.registerBarbershop(name, ((ProfessionalUser) user));

	    int n = readInt("Digite a quantidade de serviços que essa barbearia vai ter:");
	    for (int i = 0; i < n; i++) {
	        int nService = i + 1;
	        System.out.print("Digite o nome do serviço #" + nService + ": ");
	        String serviceName = sc.nextLine();
	        int minutesValue = readInt("Digite a estimativa de duração desse serviço em minutos:");
	        double servicePrice = readDouble("Digite o valor do serviço #" + nService + ":");

	        ServiceProvided service = new ServiceProvided(serviceName, Duration.ofMinutes(minutesValue), servicePrice);
	        newShop.addServiceProvided(service);
	        System.out.println("Serviço #" + nService + " adicionado!");
	    }

	    System.out.println("--- DEFINIR HORÁRIO DE SERVIÇO ---");
	    n = readInt("Quantos turnos terá a barbearia?:");
	    for (int i = 0; i < n; i++) {
	        System.out.print("Que horário irá abrir o turno #" + (i + 1) + "? (ex: 8h30):");
	        String stringTimeOpen = sc.nextLine();
	        System.out.print("Que horas o turno vai fechar? (ex: 12h30):");
	        String stringTimeClose = sc.nextLine();
	        DateTimeFormatter formater = DateTimeFormatter.ofPattern("H'h'mm");

	        try {
	            LocalTime timeStart = LocalTime.parse(stringTimeOpen, formater);
	            LocalTime timeClose = LocalTime.parse(stringTimeClose, formater);
	            newShop.addWorkShift(new WorkShift(timeStart, timeClose));
	        } catch (DateTimeParseException e) {
	            System.out.println("Erro: formato inválido, por favor use o padrão (18h30)");
	        }
	    }

	    int nDays = readInt("Sua barbearia vai fechar quantos dias?:");
	    if (nDays > 0) {
	        System.out.println("=== CONFIGURAÇÃO DE FECHAMENTO DA LOJA ===");
	        System.out.println("Selecione os dias que a loja estará FECHADA.");
	    }
	    for (int i = 0; i < nDays; i++) {
	        DayOfWeek[] dias = DayOfWeek.values();
	        for (int d = 0; d < dias.length; d++) {
	            System.out.println((d + 1) + " - " + dias[d].getDisplayName(TextStyle.FULL, Locale.of("pt", "BR")));
	        }
	        int nClose = readInt("Digite o número do dia que vai fechar: ");
	        if (nClose < 1 || nClose > dias.length) {
	            System.out.println("Opção inválida.");
	            i--;
	            continue;
	        }
	        newShop.addClosedDay(dias[nClose - 1]);
	    }

	    System.out.println("Barbearia adicionada com sucesso!");
	    professionalMenu();
	}
	
	public void clientMenu() {
		System.out.println(stringLoggedUser());
		System.out.println("1 - Barbearias");
		System.out.println("2 - Agendamentos");
		System.out.println("3 - Sair");
		int choice = readMenuOption(3);
		
		if(choice == 3) {
			return;
		}
		
		if(choice == 1) {
			barbershopsListMenu();
		}
		else appointmentsListMenu();
	}
	
	private Optional<Integer> readChoice(int totalOptions) {
	    while (true) {
	        
	        int choice = readInt("Digite sua opção:");

	        int goBack = totalOptions + 1;
	        if (choice == goBack) {
	            return Optional.empty();
	        }
	        if (choice < 1 || choice > totalOptions) {
	            System.out.println("Opção inválida.");
	            continue;
	        }

	        return Optional.of(choice - 1);
	    }
	}
	
	public void appointmentsListMenu() {
		System.out.println("--- AGENDAMENTOS ---");
		System.out.println("Digite o número do agendamento para cancelar.");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		DateTimeFormatter formatterHour = DateTimeFormatter.ofPattern("HH:mm");
		List<Appointment> listAppointments = user.getAppointments();
		if(listAppointments.size() == 0) {
			System.out.println("Nenhum agendamento marcado.");
		}
		for(int i = 0; i < listAppointments.size(); i++) {
			Appointment appointment = listAppointments.get(i);
			LocalDate date = appointment.getStartTime().toLocalDate();
			LocalTime startTime = appointment.getStartTime().toLocalTime();
			LocalTime endTime = appointment.getEndTime().toLocalTime();
			
			System.out.println(i + 1 + " - " 
			+ "Barbearia " + appointment.getShop().getName()  
			+ ", Profissional " + appointment.getProfessional()
			+ ", Data:" + date.format(formatter)
			+ ", Inicio: " + startTime.format(formatterHour)
			+ ", Fim: " + endTime.format(formatterHour));
		}
		System.out.println(listAppointments.size() + 1 + " - Voltar");
		
		Optional<Integer> choice = readChoice(listAppointments.size());
		
		if(choice.isEmpty()) {
			clientMenu();
			return;
		}
		
		clientCancelAppointmentMenu(listAppointments.get(choice.get()));
	}
	
	public void clientCancelAppointmentMenu(Appointment appointment) {
		System.out.println("1 - Confirmar cancelamento");
		System.out.println("2 - Voltar");
		int option = readMenuOption(2);
		
		if(option == 1) {
			try {
				appointment.cancelAppointment();
				System.out.println("Agendamento cancelado!");
			} catch(RuntimeException e) {
				System.out.println(e.getMessage());
			}
		}
		
		appointmentsListMenu();
		
		
	};
	
	public void barbershopsListMenu() {
	    List<Shop> shops = barbershopService.getBarbershopsList();

	    System.out.println("--- SELECIONE A BARBEARIA ---");
	    for (int i = 0; i < shops.size(); i++) {
	        System.out.println((i + 1) + " - " + shops.get(i).getName());
	    }
	    System.out.println((shops.size() + 1) + " - Voltar");

	    Optional<Integer> choice = readChoice(shops.size());

	    if (choice.isEmpty()) {
	        clientMenu();
	        return;
	    }

	    barbershopAppointmentMenu(shops.get(choice.get()));
	    return;
	}

	
	public void barbershopAppointmentMenu(Shop barbershop) {
		List<ServiceProvided> serviceList = barbershop.getServiceList();
		System.out.println("--- " + barbershop.getName() + " ---");
		for(int i = 0; i < serviceList.size(); i++) {
			ServiceProvided service = serviceList.get(i);
			System.out.println(i + 1 + " - " + service.getName() + ", $" + service.getPrice() + ", " +
			service.getMinutesDuration() + " minutos");
		}
		int goBack = serviceList.size() + 1;
		System.out.println(goBack + " - Voltar");
			
		Optional<Integer> choice = readChoice(serviceList.size());
			
		if(choice.isEmpty()) {
			barbershopsListMenu();
			return;
		}
	        
	    clientAppointmentServiceProvidedMenu(barbershop, serviceList.get(choice.get()));
	}
	
	public void clientAppointmentServiceProvidedMenu(Shop shop, ServiceProvided service) {
	    System.out.print("Digite a data desejada para o serviço (ex dd/mm/yyyy): ");
	    String stringDate = sc.nextLine();
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	    DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd/MM/yyyy H:mm");
	    LocalDate date;
	    try {
		    date = LocalDate.parse(stringDate, formatter);
	    } catch (DateTimeParseException e) {
	    	System.out.println("Formato de data invalido. Digite novamente.");
	    	clientAppointmentServiceProvidedMenu(shop, service);
	    	return;
	    }
	    List<Appointment> appointmentsAvailable = null;

	    try {
	    	appointmentsAvailable = shop.getAvailableAppointments(date, service);
	    } catch (RuntimeException e) {
	    	System.out.println(e.getMessage());
	    	clientAppointmentServiceProvidedMenu(shop, service);
	    	return;
	    }

	    for (int i = 0; i < appointmentsAvailable.size(); i++) {
	        Appointment slot = appointmentsAvailable.get(i);
	        String stringStartTime = slot.getStartTime().format(formatter2);
	        String stringEndTime = slot.getEndTime().format(formatter2);
	        System.out.println((i + 1) + ": " + stringStartTime + " - " + stringEndTime);
	    }
	    System.out.println((appointmentsAvailable.size() + 1) + " - Voltar");

	    Optional<Integer> choice = readChoice(appointmentsAvailable.size());

	    if (choice.isEmpty()) {
	        barbershopAppointmentMenu(shop);
	        return;
	    }

	    Appointment slotEscolhido = appointmentsAvailable.get(choice.get());
	    Appointment newAppointment = new Appointment(
	        AppointmentStatus.PENDING,
	        slotEscolhido.getStartTime(),
	        slotEscolhido.getEndTime(),
	        (ClientUser) user,
	        shop,
	        service
	    );
	    try {
		    shop.makeAppointment(newAppointment);
		    user.addAppointments(newAppointment);
	
		    System.out.println("Pedido de agendamento enviado, aguarde confirmação do barbeiro!");
	    }
	    catch(RuntimeException e) {
	    	System.out.println(e.getMessage());
	    	clientAppointmentServiceProvidedMenu(shop, service);
	    	return;
	    }
	    clientMenu();
	}
}
