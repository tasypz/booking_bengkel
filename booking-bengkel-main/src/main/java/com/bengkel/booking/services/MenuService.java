package com.bengkel.booking.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.bengkel.booking.models.BookingOrder;
import com.bengkel.booking.models.Customer;
import com.bengkel.booking.models.ItemService;
import com.bengkel.booking.models.MemberCustomer;
import com.bengkel.booking.models.Vehicle;
import com.bengkel.booking.repositories.CustomerRepository;
import com.bengkel.booking.repositories.ItemServiceRepository;

public class MenuService {
    private static List<Customer> listAllCustomers = CustomerRepository.getAllCustomer();
    private static List<ItemService> listAllItemService = ItemServiceRepository.getAllItemService();
    private static List<BookingOrder> bookingOrders = new ArrayList<>();
    private static Scanner input = new Scanner(System.in);

    public static void run() {
        boolean isLooping = true;
        do {
            login(listAllCustomers);
            // mainMenu();
        } while (isLooping);
    }

    public static void login(List<Customer> listAllCustomers) {
        int attempts = 0;
        final int maxAttempts = 3;
        Customer loggedInCustomer = null;

        while (attempts < maxAttempts && loggedInCustomer == null) {
            System.out.println("Enter Customer ID: ");
            String customerId = input.nextLine();
            Customer customer = getCustomerByCustomerId(customerId, listAllCustomers);
            if (customer == null) {
                System.out.println("Customer Id Tidak Ditemukan atau Salah!");
                attempts++;
                if (attempts == maxAttempts) {
                    System.out.println("Anda telah melakukan kesalahan sebanyak tiga kali. Aplikasi akan berhenti.");
                    System.exit(0);
                }
                continue;
            }

            System.out.println("Enter password: ");
            String password = input.nextLine();

            if (!customer.getPassword().equals(password)) {
                System.out.println("Password yang anda masukkan salah!");
                attempts++;
                if (attempts == maxAttempts) {
                    System.out.println("Anda telah melakukan kesalahan sebanyak tiga kali. Aplikasi akan berhenti.");
                    System.exit(0);
                }
            } else {
                System.out.println("Login Succesfull! Selamat datang di aplikasi Bengkel, " + customer.getName());
                loggedInCustomer = customer;
            }
        }

        if (loggedInCustomer != null) {
            mainMenu(loggedInCustomer);
        }
    }

    private static Customer getCustomerByCustomerId(String customerId, List<Customer> listAllCustomers) {
        for (Customer customer : listAllCustomers) {
            if (customer.getCustomerId().equals(customerId)) {
                return customer;
            }
        }
        return null;
    }

    public static void mainMenu(Customer loggedInCustomer) {
        String[] listMenu = { "Informasi Customer", "Booking Bengkel", "Top Up Bengkel Coin", "Informasi Booking", "Logout" };
        int menuChoice = 0;
        boolean isLooping = true;

        do {
            PrintService.printMenu(listMenu, "Booking Bengkel Menu");
            menuChoice = Validation.validasiNumberWithRange("Masukan Pilihan Menu:", "Input Harus Berupa Angka!", "^[0-4]+$", listMenu.length - 1, 0);
            System.out.println(menuChoice);

            switch (menuChoice) {
                case 1:
                    BengkelService.viewCustomerInfo(loggedInCustomer, listAllCustomers);
                    break;
                case 2:
                    BengkelService.bookingBengkel(loggedInCustomer, listAllItemService, bookingOrders);
                    break;
                case 3:
                    BengkelService.topUpSaldoCoin(loggedInCustomer);
                    break;
                case 4:
					BengkelService.viewBookingOrderInfo(bookingOrders);
                    break;
                default:
                    System.out.println("Logout");
                    isLooping = false;
                    break;
            }
        } while (isLooping);
    }
}