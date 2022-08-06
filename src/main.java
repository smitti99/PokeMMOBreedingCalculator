import java.nio.file.FileAlreadyExistsException;
import java.util.Scanner;

public class main {

    //Variables
    static String[] Names;
    static int NumStats;
    static boolean Gender;
    static boolean Natured;

    public static void main(String[] args) {
       /* BreedingData Input = new BreedingData();
        Input.Goal = new boolean[]{true, true, true, false, false, false, false};
        Input.Stats1[0].male = 0;
        Input.Stats1[1].male = 1;
        Input.Stats1[2].male = 1;
        Input.Stats2[0][2].male = 1;
        Input.has[0] = true;
        Input.has[1] = true;
        BreedingData out = CreateBreedingTree(Input, 3);
        //System.out.println(out.outString);
        System.out.println(out.success);
        */
        StartProgram();
    }

    public static void StartProgram() {
        GUI window= new GUI();
    }

    protected static BreedingData CreateBreedingTreeGender(BreedingData data, int statsToDo) {
        BreedingData localData = new BreedingData(false);
        if (statsToDo <= 0) return localData;


        for (int i = 0; i < 7; i++) {
            localData = new BreedingData(data);
            BreedingData leftData = new BreedingData(data);
            if (!leftData.Goal[i]) continue;
            leftData.Goal[i] = false;
            BreedingData copyOfLeftData = new BreedingData(leftData);
            boolean male = true;
            boolean female = false;
            leftData = BreedingTreeSubFunction(copyOfLeftData, statsToDo-1, true);
            if (!leftData.success) {
                male = false;
                female=true;
                leftData = BreedingTreeSubFunction(copyOfLeftData, statsToDo-1, false);
                if (!leftData.success) {
                    female = false;
                    leftData = CreateBreedingTreeGender(copyOfLeftData, statsToDo - 1);
                }
            }
            if (!leftData.success) continue;
            localData.SubtractStats(leftData);
            for (int j = i + 1; j < 7; j++) {

                BreedingData rightData = new BreedingData(localData);
                if (!rightData.Goal[j]) continue;
                rightData.Goal[j] = false;
                BreedingData copyOfRightData = new BreedingData(rightData);
                if (!male) {
                    rightData = BreedingTreeSubFunction(copyOfRightData, statsToDo-1, true);
                }
                if (!rightData.success && !female) {
                    rightData = BreedingTreeSubFunction(copyOfRightData, statsToDo-1, false);
                }
                if (!rightData.success) {
                    rightData = CreateBreedingTreeGender(copyOfRightData, statsToDo - 1);
                }
                if (!rightData.success) continue;
                BreedingData outData = new BreedingData(rightData);
                outData.root = new TreeNode(leftData.root,rightData.root,data.Goal,true,false);

                outData.AddStats(leftData);
                outData.success=true;
                return outData;
            }
        }
        return new BreedingData(false);
    }

    protected static BreedingData CreateBreedingTree(BreedingData data, int statsToDo) {
        BreedingData localData = new BreedingData(false);
        if (statsToDo <= 0) return localData;
        localData = BreedingTreeSubFunction(data,statsToDo,true);
        if(localData.success)return localData;

        for (int i = 0; i < 7; i++) {
            localData = new BreedingData(data);
            BreedingData leftData = new BreedingData(data);
            if (!leftData.Goal[i]) continue;
            leftData.Goal[i] = false;
            leftData = CreateBreedingTree(leftData, statsToDo - 1);
            if (!leftData.success) continue;
            localData.SubtractStats(leftData);
            for (int j = i + 1; j < 7; j++) {
                BreedingData rightData = new BreedingData(localData);
                if (!rightData.Goal[j]) continue;
                rightData.Goal[j] = false;
                rightData = CreateBreedingTree(rightData, statsToDo - 1);
                if (!rightData.success) continue;
                rightData.AddStats(leftData);
                BreedingData outData = new BreedingData(rightData);
                outData.root = new TreeNode(leftData.root,rightData.root,data.Goal,true,false);
                return outData;
            }
        }
        return new BreedingData(false);
    }

    private static BreedingData BreedingTreeSubFunction(BreedingData data, int statsToDo, boolean male) {
        if(statsToDo<=0){
            return new BreedingData(false);
        }
        if (data.has[statsToDo-1]) {
            BreedingData localData = new BreedingData();
            int first = 7, second = 7, third = 7, fourth = 7, fifth = 7, sixth = 7;
            switch (statsToDo) {
                case 6:
                    sixth = 6;
                case 5:
                    for (int i = sixth - 1; i >= 0; i--) {
                        if (data.Goal[i]) {
                            fifth = i;
                            break;
                        }
                    }
                case 4:
                    for (int i = fifth - 1; i >= 0; i--) {
                        if (data.Goal[i]) {
                            fourth = i;
                            break;
                        }
                    }
                case 3:
                    for (int i = fourth - 1; i >= 0; i--) {
                        if (data.Goal[i]) {
                            third = i;
                            break;
                        }
                    }
                case 2:
                    for (int i = third - 1; i >= 0; i--) {
                        if (data.Goal[i]) {
                            second = i;
                            break;
                        }
                    }
                default:
                    for (int i = second - 1; i >= 0; i--) {
                        if (data.Goal[i]) {
                            first = i;
                            break;
                        }
                    }
                    break;
            }

            boolean foundMatch = false;
            if (male) {
                switch (statsToDo) {
                    case 1:
                        if (data.Stats1[first].male > 0) {
                            localData.Stats1[first].male++;
                            foundMatch = true;
                            localData.has[0] = true;
                            localData.root = new TreeNode(data.Goal,true,true);
                        }
                        break;
                    case 2:
                        if (data.Stats2[first][second].male > 0) {
                            localData.Stats2[first][second].male++;
                            foundMatch = true;
                            localData.has[1] = true;
                            localData.root = new TreeNode(data.Goal,true,true);                        }
                        break;
                    case 3:
                        if (data.Stats3[first][second][third].male > 0) {
                            localData.Stats3[first][second][third].male++;
                            foundMatch = true;
                            localData.has[2] = true;
                            localData.root = new TreeNode(data.Goal,true,true);                        }
                        break;
                    case 4:
                        if (data.Stats4[first][second][third][fourth].male > 0) {
                            localData.Stats4[first][second][third][fourth].male++;
                            foundMatch = true;
                            localData.has[3] = true;
                            localData.root = new TreeNode(data.Goal,true,true);                        }
                        break;
                    case 5:
                        if (data.Stats5[first][second][third][fourth][fifth].male > 0) {
                            localData.Stats5[first][second][third][fourth][fifth].male++;
                            foundMatch = true;
                            localData.has[4] = true;
                            localData.root = new TreeNode(data.Goal,true,true);                        }
                        break;
                    default:
                        if (data.Stats6[0][1][2][3][4][5].male > 0) {
                            localData.Stats6[0][1][2][3][4][5].male++;
                            foundMatch = true;
                            localData.has[5] = true;
                            localData.root = new TreeNode(data.Goal,true,true);                        }
                        break;
                }
            } else {
                switch (statsToDo) {
                    case 1:
                        if (data.Stats1[first].female > 0) {
                            localData.Stats1[first].female++;
                            foundMatch = true;
                            localData.has[0] = true;
                            localData.root = new TreeNode(data.Goal,false,true);                        }
                        break;
                    case 2:
                        if (data.Stats2[first][second].female > 0) {
                            localData.Stats2[first][second].female++;
                            foundMatch = true;
                            localData.has[1] = true;
                            localData.root = new TreeNode(data.Goal,false,true);                        }
                        break;
                    case 3:
                        if (data.Stats3[first][second][third].female > 0) {
                            localData.Stats3[first][second][third].female++;
                            foundMatch = true;
                            localData.has[2] = true;
                            localData.root = new TreeNode(data.Goal,false,true);                        }
                        break;
                    case 4:
                        if (data.Stats4[first][second][third][fourth].female > 0) {
                            localData.Stats4[first][second][third][fourth].female++;
                            foundMatch = true;
                            localData.has[3] = true;
                            localData.root = new TreeNode(data.Goal,false,true);                        }
                        break;
                    case 5:
                        if (data.Stats5[first][second][third][fourth][fifth].female > 0) {
                            localData.Stats5[first][second][third][fourth][fifth].female++;
                            foundMatch = true;
                            localData.has[4] = true;
                            localData.root = new TreeNode(data.Goal,false,true);                        }
                        break;
                    default:
                        if (data.Stats6[0][1][2][3][4][5].female > 0) {
                            localData.Stats6[0][1][2][3][4][5].female++;
                            foundMatch = true;
                            localData.has[5] = true;
                            localData.root = new TreeNode(data.Goal,false,true);                        }
                        break;
                }
            }
            if (foundMatch) {
                localData.success = true;
                if(!data.hasGender) localData.root.genderIsRelevant=false;
                return localData;
            }

        }
        return new BreedingData(false);
    }
}

