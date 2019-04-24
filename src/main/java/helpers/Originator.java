package helpers;

public class Originator {
    private long state;

    public void setState(long state){
        this.state = state;
    }

    public long getState(){
        return state;
    }

    public long saveStateToMemento(){
        return state;
    }

    public void getStateFromMemento(Memento memento){
        state = memento.getMouseCount();
    }
}