package GeonomicVariant.variant;

import java.util.*;

public class VariantDetector {
    public static class Variant {
        public int position;
        public char refBase;
        public char altBase;
        public String type;

        public Variant(int position, char refBase, char altBase, String type) {
            this.position = position;
            this.refBase = refBase;
            this.altBase = altBase;
            this.type = type;
        }

        @Override
        public String toString() {
            return "Pos: " + position + ", Type: " + type +
                    ", Ref: " + refBase + ", Alt: " + altBase;
        }
    }

    public List<Variant> detect(String aligned1, String aligned2) {
        List<Variant> variants = new ArrayList<>();

        for (int i = 0; i < aligned1.length(); i++) {
            char base1 = aligned1.charAt(i);
            char base2 = aligned2.charAt(i);

            if (base1 == base2) continue;

            String type;
            if (base1 == '-') {
                type = "Insertion";
            } else if (base2 == '-') {
                type = "Deletion";
            } else {
                type = "Substitution";
            }

            variants.add(new Variant(i + 1, base1, base2, type));
        }

        return variants;
    }
}
