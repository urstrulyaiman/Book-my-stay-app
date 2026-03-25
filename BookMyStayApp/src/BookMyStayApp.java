import java.util.*;

class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

class BookingValidator {

    private static final List<String> validRoomTypes =
            Arrays.asList("Single", "Double", "Suite");

    public static void validate(String reservationId, String guestName,
                                String roomType, int available)
            throws InvalidBookingException {

        if (reservationId == null || reservationId.isEmpty()) {
            throw new InvalidBookingException("Reservation ID cannot be empty");
        }

        if (guestName == null || guestName.isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty");
        }

        if (!validRoomTypes.contains(roomType)) {
            throw new InvalidBookingException("Invalid room type");
        }

        if (available <= 0) {
            throw new InvalidBookingException("No rooms available");
        }
    }
}

class BookingManager {

    private Map<String, Integer> inventory;

    public BookingManager() {
        inventory = new HashMap<>();
        inventory.put("Single", 2);
        inventory.put("Double", 2);
        inventory.put("Suite", 1);
    }

    public void bookRoom(String id, String name, String room)
            throws InvalidBookingException {

        int available = inventory.getOrDefault(room, 0);

        BookingValidator.validate(id, name, room, available);

        inventory.put(room, available - 1);

        System.out.println("Booking successful!");
        System.out.println(id + " | " + name + " | " + room);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Room Availability:");
        for (String type : inventory.keySet()) {
            System.out.println(type + ": " + inventory.get(type));
        }
    }
}

public class BookMyStayApp {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        BookingManager manager = new BookingManager();

        while (true) {
            System.out.println("\n1. Book Room");
            System.out.println("2. View Inventory");
            System.out.println("3. Exit");

            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    try {
                        System.out.print("Enter Reservation ID: ");
                        String id = sc.nextLine();

                        System.out.print("Enter Guest Name: ");
                        String name = sc.nextLine();

                        System.out.print("Enter Room Type (Single/Double/Suite): ");
                        String room = sc.nextLine();

                        manager.bookRoom(id, name, room);

                    } catch (InvalidBookingException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;

                case 2:
                    manager.displayInventory();
                    break;

                case 3:
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}