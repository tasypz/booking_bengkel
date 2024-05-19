package com.bengkel.booking.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.bengkel.booking.models.BookingOrder;
import com.bengkel.booking.models.Car;
import com.bengkel.booking.models.Customer;
import com.bengkel.booking.models.ItemService;
import com.bengkel.booking.models.MemberCustomer;
import com.bengkel.booking.models.Motorcyle;
import com.bengkel.booking.models.Vehicle;

public class BengkelService {
	
	public static void viewCustomerInfo(Customer loggedInCustomer, List<Customer> listAllCustomers) {
		if(loggedInCustomer instanceof MemberCustomer) {
			System.out.println("Member");
		} else {
			System.out.println("Non Member");
		}
		
		System.out.println("                Customer Profile");

		System.out.println("Customer ID       : " + loggedInCustomer.getCustomerId());
		System.out.println("Nama              : " + loggedInCustomer.getName());
		if(loggedInCustomer instanceof MemberCustomer) {
			System.out.println("Customer Status   : Member");
			System.out.println("Saldo Koin        : " + ((MemberCustomer) loggedInCustomer).getSaldoCoin());
		} else {
			System.out.println("Customer Status   : Non Member");
		}
		System.out.println("Alamat            : " + loggedInCustomer.getAddress());
		
		System.out.println("List Kendaraan:");
		PrintService.printVehicle(loggedInCustomer);
	}

	private static int bookingOrderCounter = 1;
	public static void bookingBengkel(Customer loggedInCustomer, List<ItemService> listAllItemService, List<BookingOrder> bookingOrders) {
		Scanner input = new Scanner(System.in);
		List<Vehicle> vehicles = loggedInCustomer.getVehicles();
		Vehicle selectedVehicle = null;

		System.out.println("Masukkan vehicle ID: ");
		String vehicleID = input.nextLine();

		for (Vehicle vehicle : vehicles) {
			if (vehicle.getVehiclesId().equals(vehicleID)) {
				selectedVehicle = vehicle;
				break;
			}
		}

		if (selectedVehicle == null) {
			System.out.println("Notifikasi Pesan bahwa Kendaraan Tidak ditemukan.");
			return;
		}

		PrintService.printService(selectedVehicle, listAllItemService);

		int maxService = loggedInCustomer instanceof MemberCustomer ? 2 : 1;
		List<ItemService> selectedServices = new ArrayList<>();

		for (int i = 0; i < maxService; i++) {
			ItemService selectedService = null;
			while (selectedService == null) {
				System.out.println("Masukkan Service ID: ");
				String serviceID = input.nextLine();

				for (ItemService service : listAllItemService) {
					if (service.getServiceId().equals(serviceID) && (
						(selectedVehicle instanceof Car && service.getVehicleType().equalsIgnoreCase("Car")) ||
						(selectedVehicle instanceof Motorcyle && service.getVehicleType().equalsIgnoreCase("Motorcyle")))) {
						selectedService = service;
						break;
					}
				}

				if (selectedService == null) {
					System.out.println("Service ID tidak valid. Silakan input ulang.");
				} else {
					selectedServices.add(selectedService);
					System.out.println("Service berhasil ditambahkan: " + selectedService.getServiceName());
				}
			}

			if (i < maxService - 1) {
				String jawaban;
				while (true) {
					System.out.println("Apakah anda ingin menambah service lagi? (Y/T)");
					jawaban = input.nextLine();
					if (jawaban.equalsIgnoreCase("Y") || jawaban.equalsIgnoreCase("T")) {
						break;
					} else {
						System.out.println("Input tidak valid. Silakan masukkan 'Y' atau 'T'.");
					}
				} 
				if (jawaban.equalsIgnoreCase("T")) {
					break;
				}
			}	
		}

		if (selectedServices.isEmpty()) {
			System.out.println("Tidak ada service yang dipilih.");
			return;
		}

		double totalServicePrice = 0.0;
		for (ItemService service : selectedServices) {
			totalServicePrice += service.getPrice();
		}

		String paymentMethod;
		while (true) {
			System.out.println("Pilih metode pembayaran: ");
			if (loggedInCustomer instanceof MemberCustomer) {
				System.out.println("Saldo Coin atau Cash");
				paymentMethod = input.nextLine();
				if (paymentMethod.equalsIgnoreCase("Saldo Coin") || paymentMethod.equalsIgnoreCase("Cash")) {
					break;
				} else {
					System.out.println("Metode pembayaran tidak valid. Silakan input ulang.");
				}
			} else {
				System.out.println("Cash");
				paymentMethod = input.nextLine();
				if (paymentMethod.equalsIgnoreCase("Cash")) {
					break;
				} else {
					System.out.println("Metode pembayaran tidak valid. Silakan input ulang.");
				}
			}	
		}

		
		BookingOrder order = new BookingOrder();
		order.setBookingId("Book-" + loggedInCustomer.getCustomerId() + "-00" + bookingOrderCounter++);
		order.setCustomer(loggedInCustomer);
		order.setServices(selectedServices);
		order.setPaymentMethod(paymentMethod);
		order.setTotalServicePrice(totalServicePrice);
		order.calculatePayment();

		if (paymentMethod.equalsIgnoreCase("Saldo Coin")) {
			MemberCustomer memberCustomer = (MemberCustomer) loggedInCustomer;
			if (memberCustomer.getSaldoCoin() >= order.getTotalPayment()) {
				memberCustomer.setSaldoCoin(memberCustomer.getSaldoCoin() - order.getTotalPayment());
				System.out.println("Pembayaran berhasil menggunakan Saldo Coin. Sisa saldo: " + memberCustomer.getSaldoCoin());
			} else {
				System.out.println("Saldo Coin tidak cukup. Pembayaran gagal.");
				return;
			}
		} else {
			System.out.println("Pembayaran berhasil menggunakan Cash.");
		}

		bookingOrders.add(order);

		System.out.println("Booking Berhasil!!");
		System.out.println("Booking order: " + order.getBookingId());
		System.out.println("Total 0harga service: " + order.getTotalServicePrice());
		System.out.println("Total pembayaran: " + order.getTotalPayment());
	}

	public static void topUpSaldoCoin(Customer loggedInCustomer) {
		if(!(loggedInCustomer instanceof MemberCustomer)) {
			System.out.println("Maaf, fitur hanya untuk member saja");
			return;
		}

		Scanner input = new Scanner(System.in);

		MemberCustomer memberCustomer = (MemberCustomer) loggedInCustomer;
		System.out.println("Masukkan jumlah saldo coin yang ingin ditambahkan: ");
		
		double topUpCoin = 0;
		boolean valid = false;
	
		while (!valid) {
			if (input.hasNextDouble()) {
				topUpCoin = input.nextDouble();
				if (topUpCoin <= 0) {
					System.out.println("Saldo coin harus lebih dari 0! Silakan masukkan ulang: ");
				} else {
					valid = true;
				}
			} else {
				System.out.println("Input tidak valid. Silakan masukkan angka: ");
				input.next();
			}
		}
	
		double currentCoin = memberCustomer.getSaldoCoin();
		memberCustomer.setSaldoCoin(currentCoin + topUpCoin);
		System.out.println("Top up saldo coin berhasil! Saldo coin sekarang: " + memberCustomer.getSaldoCoin());
	}


	// Menampilkan Informasi Booking
	public static void viewBookingOrderInfo(List<BookingOrder> bookingOrders) {
		PrintService.printBookingInfo(bookingOrders);
	}
}


	// Logout


