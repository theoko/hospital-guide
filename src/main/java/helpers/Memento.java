package helpers;

public class Memento
{
    public long mouseCount;
    public Memento(long mouseCount)
    {
        this.mouseCount = mouseCount;

    }
    public long getMouseCount() {
        return mouseCount;
    }

    public void setMouseCount(long mouseCnt) {
        this.mouseCount = mouseCnt;
    }

    /*This class does not have the
    setter method.We need to use this class
    to get the state of the object only.*/
    /*public void setState(String state) {
        this.state = state;
    }*/
}