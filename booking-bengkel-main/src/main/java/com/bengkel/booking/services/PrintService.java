package com.bengkel.booking.services;

import java.util.List;

import com.bengkel.booking.models.BookingOrder;
import com.bengkel.booking.models.Car;
import com.bengkel.booking.models.Customer;
import com.bengkel.booking.models.ItemService;
import com.bengkel.booking.models.Motorcyle;
import com.bengkel.booking.models.Vehicle;

public class PrintService {
	
	public static void printMenu(String[] listMenu, String title) {
		String line = "+---------------------------------+";
		int number = 1;
		String formatTable = " %-2s. %-25s %n";
		
		System.out.printf("%-25s %n", title);
		System.out.println(line);
		
		for (String data : listMenu) {
			if (number < listMenu.length) {
				System.out.printf(formatTable, number, data);
			}else {
				System.out.printf(formatTable, 0, data);
			}
			number++;
		}
		System.out.println(line);
		System.out.println();
	}

	public static void printVehicle(Customer loggedInCustomer) {
		int num = 1;
		System.out.println("----------------------------------------------------------");
		System.out.format("| %-4s | %-10s | %-7s | %-15s | %-5s | \n", "No. ", "Vehicle ID", "Warna", "Tipe Kendaraan", "Tahun");
        
		List<Vehicle> vehicles = loggedInCustomer.getVehicles();
		System.out.println("----------------------------------------------------------");
			for (Vehicle vehicle : vehicles) {
			System.out.format("| %-4s | %-10s | %-7s | %-15s | %-5s | \n", num, vehicle.getVehiclesId(), vehicle.getColor(), vehicle.getVehicleType(), vehicle.getYearRelease());
			num++;
			System.out.println("----------------------------------------------------------");
	}
	
}

	public static void printService(Vehicle selectedVehicle, List<ItemService> listAllItemService) {
		int num2 = 1;
			System.out.println("---------------------------------------------------------------------------");
			System.out.format("| %-4s | %-15s | %-15s | %-15s | %-10s |\n", "No.", "Service ID", "Nama Service", "Tipe Kendaraan", "Harga");
			System.out.println("---------------------------------------------------------------------------");
	
			for (ItemService service : listAllItemService) {
				if ((selectedVehicle instanceof Car && service.getVehicleType().equalsIgnoreCase("Car")) ||
					(selectedVehicle instanceof Motorcyle && service.getVehicleType().equalsIgnoreCase("Motorcyle"))) {
					System.out.format("| %-4d | %-15s | %-15s | %-15s | %-10.2f |\n", num2, service.getServiceId(), service.getServiceName(), service.getVehicleType(), service.getPrice());
					num2++;
					System.out.println("---------------------------------------------------------------------------");
				}
			}
	}

	public static void printBookingInfo(List<BookingOrder> bookingOrders) {
		int num3 = 1;
		System.out.println("---------------------------------------------------------------------------");
			System.out.format("| %-4s | %-20s | %-15s | %-15s | %-10s |\n", "No.", "Booking ID", "Payment Method", "Total Service", "Total Payment");
			System.out.println("---------------------------------------------------------------------------");
		for (BookingOrder bookingOrder : bookingOrders) {
			System.out.format("| %-4s | %-20s | %-15s | %-15s | %-10s |\n", num3, bookingOrder.getBookingId(), bookingOrder.getPaymentMethod(), bookingOrder.getTotalServicePrice(), bookingOrder.getTotalPayment());
			num3++;
			System.out.println("---------------------------------------------------------------------------");
		}
	}
	
    }

