package GeonomicVariant.output;

import GeonomicVariant.variant.VariantDetector.Variant;
import GeonomicVariant.impact.VariantImpactAnalyzer.ScoredVariant;

import java.io.*;
import java.util.*;

public class VariantReportWriter {
    public void writeReport(String aligned1, String aligned2,
                            List<Variant> variants,
                            List<ScoredVariant> scoredVariants,
                            String filePath) {
        try (PrintWriter writer = new PrintWriter(filePath)) {
            writer.println("=== Genomic Variant Analysis Report ===");
            writer.println("\n--- Aligned Sequences ---");
            writer.println("Ref : " + aligned1);
            writer.println("Alt : " + aligned2);

            writer.println("\n--- Detected Variants ---");
            for (Variant v : variants) {
                writer.println(v);
            }

            writer.println("\n--- Scored Variants ---");
            for (ScoredVariant sv : scoredVariants) {
                writer.println(sv);
            }

            writer.println("\n--- Variant Type Summary ---");
            int substitutions = 0, insertions = 0, deletions = 0;
            for (Variant v : variants) {
                switch (v.type) {
                    case "Substitution":
                        substitutions++;
                        break;
                    case "Insertion":
                        insertions++;
                        break;
                    case "Deletion":
                        deletions++;
                        break;
                }
            }
            writer.println("Substitutions: " + substitutions);
            writer.println("Insertions   : " + insertions);
            writer.println("Deletions    : " + deletions);
            writer.println("Total        : " + variants.size());

            long highImpactCount = scoredVariants.stream()
                    .filter(sv -> sv.impactScore > 1.5)
                    .count();

            writer.println("\n--- Consensus Call ---");
            if (scoredVariants.size() == 0) {
                writer.println("No mutations detected. Sequence is identical.");
            } else if (highImpactCount == 0) {
                writer.println("Low-impact mutations detected. Likely benign.");
            } else if (highImpactCount < 3) {
                writer.println("Moderate mutation load. Some impactful variants detected.");
            } else {
                writer.println("High mutation load. Further investigation recommended.");
            }

            writer.println("\n--- Processing Summary ---");
            writer.println("Reference Length : " + aligned1.replace("-", "").length());
            writer.println("Sample Length    : " + aligned2.replace("-", "").length());
            writer.println("Total Variants   : " + scoredVariants.size());
            writer.println("High Impact Vars : " + highImpactCount);
            writer.println("Processed At     : " + new java.util.Date());
        } catch (IOException e) {
            System.err.println("Error writing report: " + e.getMessage());
        }
    }
}
