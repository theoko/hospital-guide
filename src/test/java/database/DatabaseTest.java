package database;

import helpers.Constants;
import models.map.Edge;
import models.map.Location;
import models.room.Room;
import models.services.SanitationRequest;
import models.user.User;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static helpers.Constants.NodeType.HALL;
import static org.junit.Assert.*;

public class DatabaseTest {


    @Before
    public void setUp() {

        // Parse locations and edges
        // Add locations and edges to the database
//        if (!Database.databaseExists()) {
//            CSVParser.parse(FileHelpers.getNodesCSV(), FileHelpers.getEdgesCSV());
//        }

    }

    @Test
    public void getUserByID() {
//        Database.getUserByID();
        assertTrue(true);
    }

    @Test
    public void getUsers() {
        assertTrue(true);
    }

    @Test
    public void getAdmins() {
        assertTrue(true);
    }

    @Test
    public void getLocations() {
        // Get locations from the database
        // Compare locations to the ones that were retrieved from the database

        Location newLoc = new Location("AHALL00201", 1608, 2596, "1", "BTM", HALL, "Hall", "Hall");
        HashMap<String, Location> locations = LocationTable.getLocations();

        // check that all fields are equal to the original after being added and and pulled from the database
        assertTrue(newLoc.getBuilding().equals((locations.get(newLoc.getNodeID())).getBuilding()));
        assertTrue(newLoc.getFloor().equals((locations.get(newLoc.getNodeID())).getFloor()));
        assertTrue(newLoc.getShortName().equals((locations.get(newLoc.getNodeID())).getShortName()));
        assertTrue(newLoc.getLongName().equals((locations.get(newLoc.getNodeID())).getLongName()));
        assertTrue(newLoc.getNodeType() == (locations.get(newLoc.getNodeID())).getNodeType());
        assertTrue(newLoc.getxCord() == (locations.get(newLoc.getNodeID())).getxCord());
        assertTrue(newLoc.getyCord() == (locations.get(newLoc.getNodeID())).getyCord());
        assertTrue(newLoc.getNodeID().equals((locations.get(newLoc.getNodeID())).getNodeID()));
    }

    @Test
    public void getLocationsByID() {
        Location newLoc = new Location("AHALL00201", 1608, 2596, "1", "BTM", HALL, "Hall", "Hall");
        assertTrue(newLoc.equals(LocationTable.getLocationByID(newLoc.getNodeID())));
    }

    @Test
    public void getEdges() {
        // Get edges from the database
        // Compare edges to the ones that were retrieved from the database
        Location AHALL00202 = new Location("AHALL00202", 1590, 2604, "2", "BTM", HALL, "Hall", "Hall");
        Location AHALL00302 = new Location("AHALL00302", 1590, 2745, "2", "BTM", HALL, "Hall", "Hall");
        Edge newEdg = new Edge("AHALL00202_AHALL00302", AHALL00302, AHALL00202);
        HashMap<String, Location> locations = LocationTable.getLocations();
        List<Edge> edges = EdgeTable.getEdges(locations);
        Edge testEdge = null;
        Location testStart = null;
        Location testEnd = null;
        for (Edge e : edges) {
            if (e.getEdgeID().equals(newEdg.getEdgeID())) {
                testEdge = e;
                testStart = e.getStart();
                testEnd = e.getEnd();
            }
        }
        assertEquals(newEdg.getEdgeID(), testEdge.getEdgeID());
        assertEquals(newEdg.getStart().getNodeID(), testEdge.getStart().getNodeID());
        assertEquals(newEdg.getEnd().getNodeID(), testEdge.getEnd().getNodeID());
    }

    @Test
    public void getRoomByID() {
        assertNull(RoomTable.getRoomByID("12345678900987654321"));

        Location location = new Location("123asdfasdf", 55, 55, "4", "N", HALL, "asdfasdf", "asdfasdf");
        LocationTable.addLocation(location);
        Room room = new Room("123asdfasdf", 50);
        RoomTable.addRoom(room);
        // RoomTable.getRoomByID("ABC").setCapacity(5);
        assertTrue(RoomTable.getRoomByID("123asdfasdf").getCapacity() == 50);
    }

    @Test
    public void addRoom() {
        Location loc = new Location("RM101Z", 3, 3, "s", "a", HALL, "h", "H");
        LocationTable.addLocation(loc);
        Room room = new Room(loc.getNodeID(), 2);

        RoomTable.addRoom(room);

        System.out.println(room.toString());
        System.out.println("\n");
        System.out.println(RoomTable.getRoomByID(room.getRoomID()).toString());

        assertTrue(RoomTable.getRoomByID(room.getRoomID()).equals(room));
    }

    public void getRooms() {
        Room room = new Room("ABC", 50);
        RoomTable.addRoom(room);
        boolean F = false;
        List<Room> rooms = RoomTable.getRooms();
        for (Room E : rooms) {
            if (room.equals(E)) {
                F = true;
            }
        }
        assertTrue(F);
    }

    @Test
    public void addSanitationRequest() {
        Location newLoc = new Location("AHALL00201", 1608, 2596, "1", "BTM", HALL, "Hall", "Hall");

        User user = new User(1, "Niko1", "password1234", Constants.Auth.ADMIN);
        UserTable.createUser(user);

        SanitationRequest request = new SanitationRequest(
                newLoc,
                SanitationRequest.Priority.valueOf("HIGH"),
                "DiRtY",
                user);

        boolean F = false;
        SanitationTable.addSanitationRequest(request);
        List<SanitationRequest> lstReqs = SanitationTable.getSanitationRequests();
        for (SanitationRequest E : lstReqs) {
            if (E.getDescription().equals(request.getDescription())) {
                F = true;
            }
        }
        assertTrue(F);

    }

    @Test
    public void getSanitationRequests() {
        //Location newLoc = new Location("AHALL00201", 1608, 2596, "1", "BTM", HALL, "Hall", "Hall");
        Location loc = LocationTable.getLocationByID("AHALL00201");
        User user = new User(2, "Niko2", "password1234", Constants.Auth.ADMIN);
        UserTable.createUser(user);
        SanitationRequest request = new SanitationRequest(loc, SanitationRequest.Priority.valueOf("HIGH"), "DIRTY", user);
//        SanitationRequest request = new SanitationRequest(
//                123,
//                newLoc,
//                SanitationRequest.Priority.valueOf("HIGH"),
//                "DiRtY",
//                user
//        );

        boolean F = false;
        SanitationTable.addSanitationRequest(request);
        List<SanitationRequest> lstReqs = SanitationTable.getSanitationRequests();
        for (SanitationRequest E : lstReqs) {
            if (E.getDescription().equals(request.getDescription())) {
                F = true;
            }
        }
        assertTrue(F);

    }

    @Test
    public void addEdge() {
        Location AHALL00202 = new Location("AHALL00202", 1590, 2604, "2", "BTM", HALL, "Hall", "Hall");
        Location AHALL00302 = new Location("AHALL00302", 1590, 2745, "2", "BTM", HALL, "Hall", "Hall");
        Edge newEdg = new Edge("AHALLTEST", AHALL00302, AHALL00202);
        EdgeTable.addEdge(newEdg);
        HashMap<String, Location> locations = LocationTable.getLocations();
        List<Edge> edges = EdgeTable.getEdges(locations);
        Edge testEdge = null;
        for (Edge e : edges) {
            if (e.getEdgeID().equals(newEdg.getEdgeID())) {
                testEdge = e;
            }
        }
        assertEquals(newEdg.getEdgeID(), testEdge.getEdgeID());
    }

    @Test
    public void addLocation() {
        Location newLoc = new Location("AHALL00202", 1590, 2604, "2", "BTM", HALL, "Hall", "Hall");

        LocationTable.addLocation(newLoc);
        HashMap<String, Location> locations = LocationTable.getLocations();
        Edge testLoc = null;

        assertEquals(newLoc.getNodeID(), locations.get(newLoc.getNodeID()).getNodeID());
        assertEquals(newLoc.getxCord(), locations.get(newLoc.getNodeID()).getxCord());
        assertEquals(newLoc.getyCord(), locations.get(newLoc.getNodeID()).getyCord());
        assertEquals(newLoc.getFloor(), locations.get(newLoc.getNodeID()).getFloor());
    }

    @Test
    public void getBookByRoomID() {
        Location newLoc = new Location("AHALL00201", 1608, 2596, "1", "BTM", HALL, "Hall", "Hall");
        Room newRoom = RoomTable.getRoomByID(newLoc.getNodeID());
        Date newDate1 = new Date(2019, 10, 16, 5, 9, 24);

        Date newDate2 = new Date(2019, 10, 16, 5, 58, 24);

        //Book theBooking = new Book(10, "123123", 100, newDate1, newDate2);
    }

    @Test
    public void deleteLocation() {
        Location newLoc = new Location("fakeLoc", 1608, 2596, "1", "BTM", HALL, "Hall", "Hall");
        LocationTable.addLocation(newLoc);
        assertTrue(LocationTable.getLocationByID(newLoc.getNodeID()).equals(newLoc));//Check that is was added
        LocationTable.deleteLocation(newLoc);
        assertNull(LocationTable.getLocationByID(newLoc.getNodeID()));//check that it was deleted
    }

    @Test
    public void createUser() {
        //TODO: users should not take in a user ID to instantiate
        User Niko = new User(2, "Niko1499", "2442", Constants.Auth.ADMIN);
        UserTable.createUser(Niko);
        List<User> userlist = UserTable.getUsers();
        boolean f = false;
        for (User e : userlist) {
            System.out.println(e.toString());
            if (e.equals(Niko)) {
                f = true;
                System.out.println("here");
            }
        }
        assertTrue(f);
    }

//    @Test
//    public void updateEdge() {
//        Location AHALL00202 = new Location("AHALL00202", 1500, 2600, "2", "BTM", HALL, "Hall", "Hall");
//        Location AHALL00312 = new Location("AHALL00302", 1500, 2700, "2", "BTM", HALL, "Hall", "Hall");
//        Edge newEdg = new Edge("AHALL00202_AHALL00302", AHALL00312, AHALL00202);
//
//        assertTrue(Database.updateEdge(newEdg));
//    }


}