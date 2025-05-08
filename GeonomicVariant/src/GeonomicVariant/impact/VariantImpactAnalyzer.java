package GeonomicVariant.impact;

import GeonomicVariant.variant.VariantDetector.Variant;
import java.util.*;

public class VariantImpactAnalyzer {
    public static class ScoredVariant {
        public Variant variant;
        public double impactScore;

        public ScoredVariant(Variant variant, double score) {
            this.variant = variant;
            this.impactScore = score;
        }

        @Override
        public String toString() {
            return variant.toString() + ", Impact Score: " + impactScore;
        }
    }

    // Simulate a simplified impact scoring system
    public List<ScoredVariant> scoreVariants(List<Variant> variants) {
        List<ScoredVariant> scoredList = new ArrayList<>();

        for (Variant v : variants) {
            double score = 0.0;

            switch (v.type) {
                case "Substitution":
                    score += substitutionImpact(String.valueOf(v.refBase), String.valueOf(v.altBase));
                    break;
                case "Insertion":
                case "Deletion":
                    score += 2.0; // More severe
                    break;
            }

            if (v.position < 5) score += 0.5;

            scoredList.add(new ScoredVariant(v, score));
        }

        return scoredList;
    }

    private double substitutionImpact(String ref, String alt) {
        // Transitions (A<->G, C<->T) are usually less harmful than transversions
        Set<String> transitions = new HashSet<>(Arrays.asList("AG", "GA", "CT", "TC"));
        String pair = ref + alt;
        return transitions.contains(pair) ? 0.5 : 1.0;
    }
}
