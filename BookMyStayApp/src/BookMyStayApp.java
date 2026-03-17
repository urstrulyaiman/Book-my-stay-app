import java.util.*;

public class BookMyStayApp {

    static class Reservation {
        private String guestName;
        private String roomType;

        public Reservation(String guestName, String roomType) {
            this.guestName = guestName;
            this.roomType = roomType;
        }

        public String getGuestName() {
            return guestName;
        }

        public String getRoomType() {
            return roomType;
        }
    }

    static class BookingRequestQueue {
        private Queue<Reservation> queue = new LinkedList<>();

        public void addRequest(Reservation r) {
            queue.offer(r);
        }

        public Reservation getNextRequest() {
            return queue.poll();
        }

        public boolean hasRequests() {
            return !queue.isEmpty();
        }
    }

    static class RoomInventory {
        private Map<String, Integer> availability = new HashMap<>();

        public RoomInventory() {
            availability.put("Single", 2);
            availability.put("Double", 1);
            availability.put("Suite", 1);
        }

        public int getAvailable(String type) {
            return availability.getOrDefault(type, 0);
        }

        public void reduce(String type) {
            availability.put(type, availability.get(type) - 1);
        }
    }

    static class RoomAllocationService {

        private Set<String> allocatedRoomIds = new HashSet<>();
        private Map<String, Set<String>> assignedRoomsByType = new HashMap<>();
        private Map<String, Integer> counters = new HashMap<>();

        public void allocateRoom(Reservation r, RoomInventory inv) {

            String type = r.getRoomType();

            if (inv.getAvailable(type) <= 0) return;

            String roomId = generateRoomId(type);

            allocatedRoomIds.add(roomId);

            assignedRoomsByType
                    .computeIfAbsent(type, k -> new HashSet<>())
                    .add(roomId);

            inv.reduce(type);

            System.out.println("Booking confirmed for Guest: " + r.getGuestName() + ", Room ID: " + roomId);
        }

        private String generateRoomId(String type) {
            int count = counters.getOrDefault(type, 0) + 1;
            counters.put(type, count);
            return type + "-" + count;
        }
    }

    public static void main(String[] args) {

        System.out.println("Room Allocation Processing");

        BookingRequestQueue queue = new BookingRequestQueue();

        queue.addRequest(new Reservation("Abhi", "Single"));
        queue.addRequest(new Reservation("Subha", "Single"));
        queue.addRequest(new Reservation("Vanmathi", "Suite"));

        RoomInventory inventory = new RoomInventory();
        RoomAllocationService service = new RoomAllocationService();

        while (queue.hasRequests()) {
            Reservation r = queue.getNextRequest();
            service.allocateRoom(r, inventory);
        }
    }
}