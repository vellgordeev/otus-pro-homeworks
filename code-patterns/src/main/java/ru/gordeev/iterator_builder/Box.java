package ru.gordeev.iterator_builder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;


public class Box implements Iterable<String> {

    private final List<String> firstList;

    private final List<String> secondList;

    private final List<String> thirdList;

    private final List<String> fourthList;


    public Box() {
        this.firstList = new ArrayList<>();
        this.secondList = new ArrayList<>();
        this.thirdList = new ArrayList<>();
        this.fourthList = new ArrayList<>();
    }

    public List<String> getFirstList() {
        return firstList;
    }

    public List<String> getSecondList() {
        return secondList;
    }

    public List<String> getFourthList() {
        return fourthList;
    }

    public List<String> getThirdList() {
        return thirdList;
    }

    @Override
    public Iterator<String> iterator() {
        return new Iterator<>() {
            private int index = 0;
            private int listNumber = 1;

            private int getSizeOfCurrentList() {
                return switch (listNumber) {
                    case 1 -> firstList.size();
                    case 2 -> secondList.size();
                    case 3 -> thirdList.size();
                    case 4 -> fourthList.size();
                    default -> 0;
                };
            }

            private String getElementOfCurrentList() {
                return switch (listNumber) {
                    case 1 -> firstList.get(index++);
                    case 2 -> secondList.get(index++);
                    case 3 -> thirdList.get(index++);
                    case 4 -> fourthList.get(index++);
                    default -> throw new NoSuchElementException();
                };
            }

            @Override
            public boolean hasNext() {
                while (listNumber <= 4) {
                    if (index < getSizeOfCurrentList()) {
                        return true;
                    } else {
                        listNumber++;
                        index = 0;
                    }
                }
                return false;
            }

            @Override
            public String next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("No more elements in the box");
                }

                return getElementOfCurrentList();
            }
        };
    }
}
