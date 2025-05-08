package GeonomicVariant;

import GeonomicVariant.auth.UserAuthManager;
import GeonomicVariant.parser.FastaParser;
import GeonomicVariant.alignment.NeedlemanWunsch;
import GeonomicVariant.variant.VariantDetector;
import GeonomicVariant.variant.VariantDetector.Variant;
import GeonomicVariant.impact.VariantImpactAnalyzer;
import GeonomicVariant.impact.VariantImpactAnalyzer.ScoredVariant;
import GeonomicVariant.output.VariantReportWriter;

import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UserAuthManager authManager = new UserAuthManager();
        String username = authManager.startSession();

        while (true) {
            System.out.println("\nHello " + username + "! Choose an option:");
            System.out.println("1. Take the Genomic Variant Test");
            System.out.println("2. Learn about the tool");
            System.out.println("3. Logout");
            System.out.print("Your choice: ");
            String choice = scanner.nextLine();

            if (choice.equals("1")) {
                System.out.print("Enter your DNA sequence: ");
                String userSequence = scanner.nextLine();

                System.out.println("Processing...\n");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                runVariantAnalysis(username, userSequence);
            } else if (choice.equals("2")) {
                System.out.println("\nThis tool analyzes DNA sequences to detect genetic mutations (variants).");
                System.out.println("It compares your input with known perfect sequences and identifies");
                System.out.println("substitutions, insertions, and deletions. Each variant is scored to predict impact.");
            } else if (choice.equals("3")) {
                System.out.println("Goodbye, " + username + "!");
                break;
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    public static void runVariantAnalysis(String username, String userSequence) {
        String fastaFilePath = "data/perfect_sequences.fasta";
        FastaParser parser = new FastaParser();
        List<String> referenceSequences = parser.readFasta(fastaFilePath);

        if (referenceSequences.isEmpty()) {
            System.out.println("No reference sequences found in FASTA file.");
            return;
        }

        String bestReference = referenceSequences.get(0);
        int maxMatch = -1;

        for (String ref : referenceSequences) {
            int match = 0;
            int minLen = Math.min(ref.length(), userSequence.length());
            for (int i = 0; i < minLen; i++) {
                if (ref.charAt(i) == userSequence.charAt(i)) {
                    match++;
                }
            }
            if (match > maxMatch) {
                maxMatch = match;
                bestReference = ref;
            }
        }

        NeedlemanWunsch aligner = new NeedlemanWunsch();
        int[][] matrix = aligner.computeMatrix(bestReference, userSequence);
        String[] aligned = aligner.backtrack(bestReference, userSequence, matrix);

        VariantDetector detector = new VariantDetector();
        List<Variant> variantList = detector.detect(aligned[0], aligned[1]);

        VariantImpactAnalyzer analyzer = new VariantImpactAnalyzer();
        List<ScoredVariant> scoredVariants = analyzer.scoreVariants(variantList);

        
        String reportFilePath = "data/" + username + "_variant_report.txt";
        VariantReportWriter writer = new VariantReportWriter();
        writer.writeReport(aligned[0], aligned[1], variantList, scoredVariants, reportFilePath);

        System.out.println("Report generated: " + reportFilePath);
    }
}
