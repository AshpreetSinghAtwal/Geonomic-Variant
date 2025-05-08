package GeonomicVariant.alignment;

public class NeedlemanWunsch {
    private int match = 1;
    private int mismatch = -1;
    private int gap = -2;

    public int[][] computeMatrix(String seq1, String seq2) {
        int[][] score = new int[seq1.length() + 1][seq2.length() + 1];

        // Initialize first row and column
        for (int i = 0; i <= seq1.length(); i++) score[i][0] = i * gap;
        for (int j = 0; j <= seq2.length(); j++) score[0][j] = j * gap;

        // Fill matrix
        for (int i = 1; i <= seq1.length(); i++) {
            for (int j = 1; j <= seq2.length(); j++) {
                int matchScore = score[i - 1][j - 1] + (seq1.charAt(i - 1) == seq2.charAt(j - 1) ? match : mismatch);
                int delete = score[i - 1][j] + gap;
                int insert = score[i][j - 1] + gap;
                score[i][j] = Math.max(matchScore, Math.max(delete, insert));
            }
        }

        return score;
    }

    public String[] backtrack(String seq1, String seq2, int[][] score) {
        StringBuilder aligned1 = new StringBuilder();
        StringBuilder aligned2 = new StringBuilder();

        int i = seq1.length();
        int j = seq2.length();

        while (i > 0 && j > 0) {
            int scoreDiag = score[i - 1][j - 1];
            int scoreUp = score[i - 1][j];
            int scoreLeft = score[i][j - 1];

            if (score[i][j] == scoreDiag + (seq1.charAt(i - 1) == seq2.charAt(j - 1) ? match : mismatch)) {
                aligned1.append(seq1.charAt(i - 1));
                aligned2.append(seq2.charAt(j - 1));
                i--; j--;
            } else if (score[i][j] == scoreUp + gap) {
                aligned1.append(seq1.charAt(i - 1));
                aligned2.append('-');
                i--;
            } else {
                aligned1.append('-');
                aligned2.append(seq2.charAt(j - 1));
                j--;
            }
        }

        while (i > 0) {
            aligned1.append(seq1.charAt(i - 1));
            aligned2.append('-');
            i--;
        }

        while (j > 0) {
            aligned1.append('-');
            aligned2.append(seq2.charAt(j - 1));
            j--;
        }

        return new String[]{aligned1.reverse().toString(), aligned2.reverse().toString()};
    }
}
