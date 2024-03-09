package ca.flymile.Model;

import lombok.Data;

/**
 * The Aircraft class represents an aircraft.
 * It only consists aircraft name
 * Example : Boeing 777-300ER
 */
@Data
public class Aircraft {
    /**
     * The name of the aircraft.
     */
    private String name;
}
