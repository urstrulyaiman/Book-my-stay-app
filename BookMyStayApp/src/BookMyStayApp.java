import java.util.*;

class ReservationRequest {
    String reservationId;
    String guestName;
    String roomType;

    public ReservationRequest(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }
}

class BookingManager {

    private Map<String, Integer> inventory;

    public BookingManager() {
        inventory = new HashMap<>();
        inventory.put("Single", 1);
        inventory.put("Double", 1);
        inventory.put("Suite", 1);
    }

    public synchronized void bookRoom(ReservationRequest request) {
        int available = inventory.getOrDefault(request.roomType, 0);

        if (available <= 0) {
            System.out.println("Booking Failed for " + request.guestName + " (" + request.roomType + ")");
            return;
        }

        inventory.put(request.roomType, available - 1);

        System.out.println("Booking Success: " + request.reservationId + " | "
                + request.guestName + " | " + request.roomType);
    }

    public void displayInventory() {
        System.out.println("\nFinal Inventory:");
        for (String type : inventory.keySet()) {
            System.out.println(type + ": " + inventory.get(type));
        }
    }
}

class BookingProcessor extends Thread {

    private Queue<ReservationRequest> queue;
    private BookingManager manager;

    public BookingProcessor(Queue<ReservationRequest> queue, BookingManager manager) {
        this.queue = queue;
        this.manager = manager;
    }

    public void run() {
        while (true) {
            ReservationRequest request;

            synchronized (queue) {
                if (queue.isEmpty()) {
                    break;
                }
                request = queue.poll();
            }

            manager.bookRoom(request);
        }
    }
}

public class BookMyStayApp {

    public static void main(String[] args) {

        Queue<ReservationRequest> queue = new LinkedList<>();
        BookingManager manager = new BookingManager();

        queue.add(new ReservationRequest("R1", "Aiman", "Single"));
        queue.add(new ReservationRequest("R2", "Rahul", "Single"));
        queue.add(new ReservationRequest("R3", "Sara", "Double"));
        queue.add(new ReservationRequest("R4", "John", "Double"));
        queue.add(new ReservationRequest("R5", "Ali", "Suite"));
        queue.add(new ReservationRequest("R6", "Zara", "Suite"));

        BookingProcessor t1 = new BookingProcessor(queue, manager);
        BookingProcessor t2 = new BookingProcessor(queue, manager);
        BookingProcessor t3 = new BookingProcessor(queue, manager);

        t1.start();
        t2.start();
        t3.start();

        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            System.out.println("Thread interrupted");
        }

        manager.displayInventory();
    }
}