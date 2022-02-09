package uk.co.risi.telematicsservice.domain;

public enum Gear {
    FIRST,
    SECOND,
    THIRD,
    FOURTH,
    FIFTH,
    SIXTH,
    REVERSE,
    NEUTRAL;

    static Gear getGear(int gear) {
        Gear selectedGear;
        switch (gear) {
            case 1:
                selectedGear = FIRST;
                break;
            case 2:
                selectedGear = SECOND;
                break;
            case 3:
                selectedGear = THIRD;
                break;
            case 4:
                selectedGear = FOURTH;
                break;
            case 5:
                selectedGear = FIFTH;
                break;
            case 6:
                selectedGear = SIXTH;
                break;
            case -1:
                selectedGear = REVERSE;
            default:
                selectedGear = NEUTRAL;
        }
        return selectedGear;
    }
}
