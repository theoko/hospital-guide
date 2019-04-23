package helpers;

import java.util.ArrayList;
import java.util.List;

public class Caretaker {
    public ArrayList<Long> mementoList = new ArrayList<>();
    public void add(long index){
        mementoList.add(index);
    }

    public long get(int index){
        return (long) mementoList.get(index);
    }
}