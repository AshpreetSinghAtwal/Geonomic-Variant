package GeonomicVariant.parser;

import java.io.*;
import java.util.*;

public class FastaParser {
    public List<String> readFasta(String filePath) {
        List<String> sequences = new ArrayList<>();
        StringBuilder currentSeq = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                if (line.startsWith(">")) {
                    if (!currentSeq.isEmpty()) {
                        sequences.add(currentSeq.toString());
                        currentSeq.setLength(0); // reset builder
                    }
                } else {
                    currentSeq.append(line.trim());
                }
            }

            if (!currentSeq.isEmpty()) {
                sequences.add(currentSeq.toString());
            }

        } catch (IOException e) {
            System.err.println("Error reading FASTA file: " + e.getMessage());
        }

        return sequences;
    }
}
