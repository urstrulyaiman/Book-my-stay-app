import java.io.*;
import java.util.*;

class Reservation implements Serializable {
    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getRoomType() {
        return roomType;
    }

    @Override
    public String toString() {
        return reservationId + " | " + guestName + " | " + roomType;
    }
}

class SystemState implements Serializable {
    Map<String, Integer> inventory;
    Map<String, Reservation> bookings;

    public SystemState(Map<String, Integer> inventory, Map<String, Reservation> bookings) {
        this.inventory = inventory;
        this.bookings = bookings;
    }
}

class PersistenceService {

    private static final String FILE_NAME = "system_state.dat";

    public void save(SystemState state) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(state);
            System.out.println("System state saved.");
        } catch (Exception e) {
            System.out.println("Error saving data");
        }
    }

    public SystemState load() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            return (SystemState) in.readObject();
        } catch (Exception e) {
            System.out.println("No previous data found. Starting fresh.");
            return null;
        }
    }
}

class BookingManager {

    Map<String, Integer> inventory;
    Map<String, Reservation> bookings;

    public BookingManager() {
        inventory = new HashMap<>();
        bookings = new HashMap<>();

        inventory.put("Single", 2);
        inventory.put("Double", 2);
        inventory.put("Suite", 1);
    }

    public void restore(SystemState state) {
        if (state != null) {
            inventory = state.inventory;
            bookings = state.bookings;
            System.out.println("System state restored.");
        }
    }

    public SystemState getState() {
        return new SystemState(inventory, bookings);
    }

    public void bookRoom(String id, String name, String room) {
        int available = inventory.getOrDefault(room, 0);

        if (available <= 0) {
            System.out.println("No rooms available");
            return;
        }

        Reservation r = new Reservation(id, name, room);
        bookings.put(id, r);
        inventory.put(room, available - 1);

        System.out.println("Booking successful!");
        System.out.println(r);
    }

    public void displayBookings() {
        if (bookings.isEmpty()) {
            System.out.println("No bookings");
            return;
        }

        System.out.println("\nBookings:");
        for (Reservation r : bookings.values()) {
            System.out.println(r);
        }
    }

    public void displayInventory() {
        System.out.println("\nInventory:");
        for (String type : inventory.keySet()) {
            System.out.println(type + ": " + inventory.get(type));
        }
    }
}

public class BookMyStayApp {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        BookingManager manager = new BookingManager();
        PersistenceService persistence = new PersistenceService();

        SystemState loadedState = persistence.load();
        manager.restore(loadedState);

        while (true) {
            System.out.println("\n1. Book Room");
            System.out.println("2. View Bookings");
            System.out.println("3. View Inventory");
            System.out.println("4. Save & Exit");

            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter Reservation ID: ");
                    String id = sc.nextLine();

                    System.out.print("Enter Guest Name: ");
                    String name = sc.nextLine();

                    System.out.print("Enter Room Type (Single/Double/Suite): ");
                    String room = sc.nextLine();

                    manager.bookRoom(id, name, room);
                    break;

                case 2:
                    manager.displayBookings();
                    break;

                case 3:
                    manager.displayInventory();
                    break;

                case 4:
                    persistence.save(manager.getState());
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}