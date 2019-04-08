package database;

import helpers.Constants;
import helpers.MapHelpers;
import models.map.Edge;
import models.map.Location;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EdgeTable {

    private static void EdgeTable(){ }

    public static void createTable(){
        Statement statement = null;
        try {
            statement = Database.getConnection().createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String edgeTable = "CREATE TABLE " + Constants.EDGES_TABLE +
                "(edgeID VARCHAR(100) PRIMARY KEY," +
                "startNodeID VARCHAR(100)," +
                "endNodeID VARCHAR(100)," +
                "CONSTRAINT startNodeID_fk FOREIGN KEY(startNodeID) REFERENCES " + Constants.LOCATION_TABLE + "(nodeID)," +
                "CONSTRAINT endNodeID_fk FOREIGN KEY(endNodeID) REFERENCES " + Constants.LOCATION_TABLE + "(nodeID))";
        try {
            statement.execute(edgeTable);
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    public static boolean dropEdgeTable() {
        try {
            Statement statement;

            statement = Database.getConnection().createStatement();

            return statement.execute("DROP TABLE " + Constants.EDGES_TABLE);

        } catch (SQLException e) {
            System.out.println("Table " + Constants.EDGES_TABLE + " cannot be dropped");

            return false;
        }
    }

    public static boolean hasEdgeByID(String id) {
        List<Edge> edges = getEdges(LocationTable.getLocations());
        for(Edge e : edges) {
            if(e.getEdgeID().equals(id)) return true;
        }
        return false;
    }

    public static List<Edge> getEdges(HashMap<String, Location> lstLocations) {
        try {
            Statement statement;
            statement = Database.getConnection().createStatement();
            String query = "SELECT * FROM " + Constants.EDGES_TABLE;
            ResultSet resultSet = statement.executeQuery(query);
            List<Edge> returnList = new ArrayList<>();

            while (resultSet.next()) {
                String edgeID = resultSet.getString("EDGEID");
                Edge edge = new Edge(
                        edgeID,
                        lstLocations.get(resultSet.getString("STARTNODEID")),
                        lstLocations.get(resultSet.getString("ENDNODEID"))
                );

                returnList.add(edge);
            }

            return returnList;

        } catch (SQLException e) {
            System.out.println("Failed to get edges!");
            return null;
        }
    }

    /**
     * Updates the edge object specified on the database
     *
     * @param updatedEdge
     * @return true if the edge was updated successfully, false otherwise
     */
    public static boolean updateEdge(Edge updatedEdge) {

        try {
            PreparedStatement statement;

            statement = Database.getConnection().prepareStatement(
                    "UPDATE " + Constants.EDGES_TABLE +
                            " SET STARTNODEID=?, ENDNODEID=?" +
                            " WHERE EDGEID=?"
            );

            statement.setString(1, updatedEdge.getStart().getNodeID());
            statement.setString(2, updatedEdge.getEnd().getNodeID());
            statement.setString(3, updatedEdge.getEdgeID());

            return statement.execute();

        } catch (SQLException e) {
            System.out.println("Failed to update edge: " + updatedEdge.getEdgeID());
            e.printStackTrace();

            return false;
        }

    }

    public static boolean deleteEdge(Edge edge){
        try {
            String statement = "DELETE FROM " + Constants.ROOM_TABLE + " WHERE EDGEID=?";
            PreparedStatement preparedStatement = Database.getConnection().prepareStatement(statement);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean edgeExists(Edge e) {
        return hasEdgeByID(MapHelpers.generateEdgeID(e, Constants.START_FIRST))
                || hasEdgeByID(MapHelpers.generateEdgeID(e, Constants.END_FIRST));
    }

    public static void removeEdgeByID(Edge e) {
        if(edgeExists(e)) {
            String EdgeID = hasEdgeByID(MapHelpers.generateEdgeID(e, Constants.START_FIRST)) ?
                    MapHelpers.generateEdgeID(e, Constants.START_FIRST)
                    : MapHelpers.generateEdgeID(e, Constants.END_FIRST);
        }
    }

    public static boolean toggleEdge(Edge e) {
        boolean edgeExists = edgeExists(e);

        if(e.getEdgeID() == null) {
            e.setEdgeID(MapHelpers.generateEdgeID(e, Constants.START_FIRST));
        }

        if(edgeExists) {
            removeEdgeByID(e);
            return Constants.DESELECTED;
        }
        else {
            addEdge(e);
            return Constants.SELECTED;
        }
    }

    public static boolean addEdge(Edge edge) {
        try {
            PreparedStatement statement;

            statement = Database.getConnection().prepareStatement(
                    "INSERT INTO " + Constants.EDGES_TABLE + " (EDGEID, STARTNODEID, ENDNODEID) " +
                            "VALUES (?, ?, ?)"
            );

            statement.setString(1, edge.getEdgeID());
            statement.setString(2, edge.getStart().getNodeID());
            statement.setString(3, edge.getEnd().getNodeID());

            return statement.execute();

        } catch (SQLException e) {
            System.out.println("SubPath cannot be added!");

            return false;
        }
    }
}
