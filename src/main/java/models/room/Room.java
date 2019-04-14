package models.room;

public class Room {
    private String roomID;
    private int capacity;
    public Room(String roomID, int capacity) {
        this.roomID = roomID;
        this.capacity = capacity;
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Room) {
            if((((Room)obj).capacity)==this.getCapacity() && ((Room) obj).getRoomID().equals(this.getRoomID())){
                return true;
            }else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public String toString(){
        return "roomID: "+this.getRoomID()+", Capacity:"+this.getCapacity();
    }
//    @Override
//    public boolean equals(Room roomA){
//        if(roomA.capacity==this.getCapacity() && roomA.roomID.equals(this.getRoomID())){
//            return true;
//        }else {
//            return false;
//        }
//    }
}

