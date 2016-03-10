package chihane.poping;

public class Level {
    private long score;
    private long level;
    private long requiredScore;

    public Level() {
        score = 0;
        level = 1;
        requiredScore = Algorithm.calcRequiredScore(level);
    }

    public void next() {
        level++;
        requiredScore = Algorithm.calcRequiredScore(level);
    }

    public void gainScore(long score) {
        this.score += score;
    }

    public long getScore() {
        return score;
    }

    public boolean hasEnoughScore() {
        return score >= requiredScore;
    }

    public long getLevel() {
        return level;
    }

    public long getRequiredScore() {
        return requiredScore;
    }
}
