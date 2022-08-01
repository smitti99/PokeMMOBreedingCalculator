//1.HP 2.ATK 3.DEF 4. S_ATK 5.S_DEF 6.INIT 7.Nature
public class BreedingData {

    //used in algorithm
    public boolean success;
    public TreeNode root;

    //input from GUI
    public boolean hasGender=true;
    public boolean[] Goal;
    public boolean[] has;
    public Count[][][][][][] Stats6;
    public Count[][][][][] Stats5;
    public Count[][][][] Stats4;
    public Count[][][] Stats3;
    public Count[][] Stats2;
    public Count[] Stats1;

    public BreedingData() {
        Stats6 = new Count[6][6][6][6][6][6];
        Stats5 = new Count[6][6][6][6][6];
        Stats4 = new Count[6][6][6][6];
        Stats3 = new Count[6][6][6];
        Stats2 = new Count[6][6];
        Stats1 = new Count[7];
        Goal = new boolean[]{false, false, false, false, false, false, false};
        has = new boolean[]{false, false, false, false, false, false};
        for (int k = 0; k < 7; k++) {
            this.Stats1[k] = new Count();
        }
        for (int k = 0; k < 6; k++) {
            for (int j = k + 1; j < 6; j++) {
                this.Stats2[k][j] = new Count();
                for (int h = j + 1; h < 6; h++) {
                    this.Stats3[k][j][h] = new Count();
                    for (int g = h + 1; g < 6; g++) {
                        this.Stats4[k][j][h][g] = new Count();
                        for (int f = g + 1; f < 6; f++) {
                            this.Stats5[k][j][h][g][f] = new Count();
                            for (int d = f + 1; d < 6; d++) {
                                this.Stats6[k][j][h][g][f][d] = new Count();
                            }
                        }
                    }
                }
            }
        }
    }

    public BreedingData(boolean Success) {
        this();
        success = Success;
    }

    BreedingData(BreedingData other) {
        this.hasGender = other.hasGender;
        has = new boolean[]{false, false, false, false, false, false};
        this.has = other.has.clone();
        Goal = new boolean[]{false, false, false, false, false, false, false};
        this.Goal = other.Goal.clone();
        Stats1 = new Count[7];
        Stats2 = new Count[6][6];
        Stats3 = new Count[6][6][6];
        Stats4 = new Count[6][6][6][6];
        Stats5 = new Count[6][6][6][6][6];
        Stats6 = new Count[6][6][6][6][6][6];
        for (int k = 0; k < 7; k++) {
            this.Stats1[k] = new Count(other.Stats1[k]);
        }
        for (int k = 0; k < 6; k++) {
            for (int j = k + 1; j < 6; j++) {
                this.Stats2[k][j] = new Count(other.Stats2[k][j]);
                for (int h = j + 1; h < 6; h++) {
                    this.Stats3[k][j][h] = new Count(other.Stats3[k][j][h]);
                    for (int g = h + 1; g < 6; g++) {
                        this.Stats4[k][j][h][g] = new Count(other.Stats4[k][j][h][g]);
                        for (int f = g + 1; f < 6; f++) {
                            this.Stats5[k][j][h][g][f] = new Count(other.Stats5[k][j][h][g][f]);
                            for (int d = f + 1; d < 6; d++) {
                                this.Stats6[k][j][h][g][f][d] = new Count(other.Stats6[k][j][h][g][f][d]);
                            }
                        }
                    }
                }
            }
        }


        this.success = other.success;

    }

    void SubtractStats(BreedingData other) {
        for (int k = 0; k < 7; k++) {
            this.Stats1[k].male -= other.Stats1[k].male;
        }
        for (int k = 0; k < 6; k++) {
            for (int j = k + 1; j < 6; j++) {
                this.Stats2[k][j].male -= other.Stats2[k][j].male;
                for (int h = j + 1; h < 6; h++) {
                    this.Stats3[k][j][h].male -= other.Stats3[k][j][h].male;
                    for (int g = h + 1; g < 6; g++) {
                        this.Stats4[k][j][h][g].male -= other.Stats4[k][j][h][g].male;
                        for (int f = g + 1; f < 6; f++) {
                            this.Stats5[k][j][h][g][f].male -= other.Stats5[k][j][h][g][f].male;
                            for (int d = f + 1; d < 6; d++) {
                                this.Stats6[k][j][h][g][f][d].male -= other.Stats6[k][j][h][g][f][d].male;
                            }
                        }
                    }
                }
            }
        }
    }

    void AddStats(BreedingData other) {
        for (int k = 0; k < 7; k++) {
            this.Stats1[k].male += other.Stats1[k].male;
        }
        for (int k = 0; k < 6; k++) {
            for (int j = k + 1; j < 6; j++) {
                this.Stats2[k][j].male += other.Stats2[k][j].male;
                for (int h = j + 1; h < 6; h++) {
                    this.Stats3[k][j][h].male += other.Stats3[k][j][h].male;
                    for (int g = h + 1; g < 6; g++) {
                        this.Stats4[k][j][h][g].male += other.Stats4[k][j][h][g].male;
                        for (int f = g + 1; f < 6; f++) {
                            this.Stats5[k][j][h][g][f].male += other.Stats5[k][j][h][g][f].male;
                            for (int d = f + 1; d < 6; d++) {
                                this.Stats6[k][j][h][g][f][d].male += other.Stats6[k][j][h][g][f][d].male;
                            }
                        }
                    }
                }
            }
        }
    }
}

class Count {
    public int male = 0;
    public int female = 0;

    Count() {
    }

    Count(Count other) {
        this.male = other.male;
        this.female = other.female;
    }
}

