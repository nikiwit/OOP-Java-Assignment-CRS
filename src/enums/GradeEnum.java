package enums;

/**
 * Enumeration representing all possible letter grades in the academic system.
 * Each grade corresponds to a specific grade point value on a 4.0 scale.
 */
public enum GradeEnum {
    A_PLUS,
    A,
    A_MINUS,
    B_PLUS,
    B,
    B_MINUS,
    C_PLUS,
    C,
    C_MINUS,
    D_PLUS,
    D,
    F;

    /**
     * Converts the letter grade to its corresponding grade point value.
     * @return the grade point value on a 4.0 scale
     */
    public double getGradePoint() {
        // To be implemented
        return 0.0;
    }
}
