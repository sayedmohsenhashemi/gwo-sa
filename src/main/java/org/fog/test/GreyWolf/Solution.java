package org.fog.test.GreyWolf;

import java.util.List;

public class Solution {
    private Define bestWolf;
    private List<Double> progression;

    public Solution(Define bestWolf, List<Double> progression) {
        this.bestWolf = bestWolf;
        this.progression = progression;
    }

    public int iterationsPassed() {
        return progression.size();
    }

    public List<Double> getProgression() {
        return progression;
    }

    public Define getBestWolf() {
        return bestWolf;
    }

    public double solution() {
        return progression.get(iterationsPassed() - 1);
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        return str.toString();
    }
}
