import java.util.*;

class Reservation {
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

class BookingManager {

    private Map<String, Integer> inventory;
    private Map<String, Reservation> activeBookings;
    private Stack<String> rollbackStack;

    public BookingManager() {
        inventory = new HashMap<>();
        inventory.put("Single", 2);
        inventory.put("Double", 2);
        inventory.put("Suite", 1);

        activeBookings = new HashMap<>();
        rollbackStack = new Stack<>();
    }

    public void bookRoom(String id, String name, String room) {
        int available = inventory.getOrDefault(room, 0);

        if (available <= 0) {
            System.out.println("No rooms available");
            return;
        }

        Reservation r = new Reservation(id, name, room);
        activeBookings.put(id, r);
        inventory.put(room, available - 1);

        System.out.println("Booking successful!");
        System.out.println(r);
    }

    public void cancelBooking(String id) {
        if (!activeBookings.containsKey(id)) {
            System.out.println("Invalid or already cancelled reservation");
            return;
        }

        Reservation r = activeBookings.remove(id);
        String room = r.getRoomType();

        rollbackStack.push(id);
        inventory.put(room, inventory.get(room) + 1);

        System.out.println("Booking cancelled successfully!");
        System.out.println("Rolled back Reservation ID: " + id);
    }

    public void displayBookings() {
        if (activeBookings.isEmpty()) {
            System.out.println("No active bookings");
            return;
        }

        System.out.println("\nActive Bookings:");
        for (Reservation r : activeBookings.values()) {
            System.out.println(r);
        }
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (String type : inventory.keySet()) {
            System.out.println(type + ": " + inventory.get(type));
        }
    }

    public void displayRollbackStack() {
        System.out.println("\nRollback Stack:");
        if (rollbackStack.isEmpty()) {
            System.out.println("No cancellations yet");
            return;
        }

        for (String id : rollbackStack) {
            System.out.println(id);
        }
    }
}

public class BookMyStayApp {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        BookingManager manager = new BookingManager();

        while (true) {
            System.out.println("\n1. Book Room");
            System.out.println("2. Cancel Booking");
            System.out.println("3. View Active Bookings");
            System.out.println("4. View Inventory");
            System.out.println("5. View Rollback Stack");
            System.out.println("6. Exit");

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
                    System.out.print("Enter Reservation ID to cancel: ");
                    String cancelId = sc.nextLine();
                    manager.cancelBooking(cancelId);
                    break;

                case 3:
                    manager.displayBookings();
                    break;

                case 4:
                    manager.displayInventory();
                    break;

                case 5:
                    manager.displayRollbackStack();
                    break;

                case 6:
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}