package ca.flymile.ModelAlaska30Days;

import lombok.Data;

import java.util.List;

/**
 * Represents the details of the cheapest flights available on a daily basis for a given month.
 * The @Data annotation is used to automatically generate boilerplate code like getters, setters,
 * equals, hashCode, and toString methods.
 */
@Data
public class MonthlyDetails {

    /**
     * A list of dailyCheapest objects, each representing the cheapest flight details for a specific day of the month.
     */
    private List<dailyCheapest> shoulderDates;

    public List<dailyCheapest> shoulderDates() {
        return this.shoulderDates;
    }
}