package org.development.sensor;



/**
 * @author Nazar Zhuhan
 * @file Location.java
 * @brief The enum is used as a set of possible locations for sensors;.
 */
public enum Location {
    LOCATION1("Power Generator"),
    LOCATION2("Basement"),
    LOCATION3("Medical Room"),
    LOCATION4("Comms Control"),
    LOCATION5("Supply Storage"),
    LOCATION6("Staff Quarters"),
    LOCATION7("Server Farm"),
    LOCATION8("Computronium C"),
    LOCATION9("Continuity B");

    String name;

    Location(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
