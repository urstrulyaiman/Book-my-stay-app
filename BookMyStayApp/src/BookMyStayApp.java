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

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    @Override
    public String toString() {
        return reservationId + " | " + guestName + " | " + roomType;
    }
}

class BookingHistory {
    private List<Reservation> reservations;

    public BookingHistory() {
        reservations = new ArrayList<>();
    }

    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
    }

    public List<Reservation> getAllReservations() {
        return reservations;
    }
}

class BookingReportService {

    public void displayBookings(List<Reservation> reservations) {
        if (reservations.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        System.out.println("\nBooking History:");
        for (Reservation r : reservations) {
            System.out.println(r);
        }
    }

    public void showSummary(List<Reservation> reservations) {
        System.out.println("\nBooking Summary:");
        System.out.println("Total Bookings: " + reservations.size());

        Map<String, Integer> count = new HashMap<>();

        for (Reservation r : reservations) {
            count.put(r.getRoomType(),
                    count.getOrDefault(r.getRoomType(), 0) + 1);
        }

        for (String type : count.keySet()) {
            System.out.println(type + ": " + count.get(type));
        }
    }
}

public class  BookMyStayApp {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        BookingHistory history = new BookingHistory();
        BookingReportService report = new BookingReportService();

        while (true) {
            System.out.println("\n1. Add Booking");
            System.out.println("2. View Booking History");
            System.out.println("3. View Summary Report");
            System.out.println("4. Exit");

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

                    history.addReservation(new Reservation(id, name, room));
                    System.out.println("Booking added successfully!");
                    break;

                case 2:
                    report.displayBookings(history.getAllReservations());
                    break;

                case 3:
                    report.showSummary(history.getAllReservations());
                    break;

                case 4:
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}