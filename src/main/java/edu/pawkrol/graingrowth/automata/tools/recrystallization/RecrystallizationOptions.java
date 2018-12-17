package edu.pawkrol.graingrowth.automata.tools.recrystallization;

public class RecrystallizationOptions {
    public enum NucleationType {
        CONSTANT,
        INCREASING,
        STATIC
    }

    private NucleationType nucleationType = NucleationType.CONSTANT;
    private int nucleationValue = 0;
    private Boolean nucleationGrainBoundary = false;

    public RecrystallizationOptions() {
    }

    public RecrystallizationOptions(NucleationType nucleationType, int nucleationValue, Boolean nucleationGrainBoundary) {
        this.nucleationType = nucleationType;
        this.nucleationValue = nucleationValue;
        this.nucleationGrainBoundary = nucleationGrainBoundary;
    }

    public NucleationType getNucleationType() {
        return nucleationType;
    }

    public void setNucleationType(NucleationType nucleationType) {
        this.nucleationType = nucleationType;
    }

    public int getNucleationValue() {
        return nucleationValue;
    }

    public void setNucleationValue(int nucleationValue) {
        this.nucleationValue = nucleationValue;
    }

    public Boolean getNucleationGrainBoundary() {
        return nucleationGrainBoundary;
    }

    public void setNucleationGrainBoundary(Boolean nucleationGrainBoundary) {
        this.nucleationGrainBoundary = nucleationGrainBoundary;
    }
}
