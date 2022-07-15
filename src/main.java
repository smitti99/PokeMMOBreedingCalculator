import java.util.Scanner;

public class main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Does the Pokemon you want to breed have a gender? [Y/N]");
        String answer = scanner.next();
        if (answer.equalsIgnoreCase("n")) {
            Gender = false;

            System.out.print("How many Stats do you want? ");
            NumStats = scanner.nextInt();
            if (NumStats <= 1) {
                System.out.print("...really?");
                return;
            }
            boolean[] Index = new boolean[NumStats];
            int[] available = new int[NumStats];
            Names = new String[NumStats];
            for (int i = 0; i < NumStats; i++) {
                System.out.print("Name of Stat :");
                System.out.print(i + 1);
                Names[i] = scanner.next();
                System.out.print("You have how many? :");
                available[i] = scanner.nextInt();
                Index[i] = true;
            }
            ReturnClass res = IsPossible(Index, available, NumStats);
            if (res.sucess) {
                System.out.println("Success!");
                System.out.print(res.outString);
            } else {
                System.out.println("No Tree could be build with tis combination");
            }
        } else {
            System.out.print("How many Stats do you want?");
            NumStats = scanner.nextInt();
            if (NumStats <= 1) {
                System.out.print("...really?");
                return;
            }
            boolean[] Index = new boolean[NumStats];
            int[] availableM = new int[NumStats];
            int[] availableF = new int[NumStats];
            Names = new String[NumStats];
            for (int i = 0; i < NumStats; i++) {
                System.out.print("Name of Stat ");
                System.out.print(i + 1);
                Names[i] = scanner.next();
                System.out.print("You have how many male? ");
                availableM[i] = scanner.nextInt();
                System.out.print("You have how many female? ");
                availableF[i] = scanner.nextInt();
                Index[i] = true;
            }
            ReturnClass res = IsPossibleWithGender(Index, availableM, availableF, NumStats);
            if (res.sucess) {
                System.out.println("Success!");
                System.out.print(res.outString);
            } else {
                System.out.println("No Tree could be build with tis combination");
            }
        }

    }

    static String[] Names;
    static int NumStats;
    static boolean Gender;

    private static ReturnClass IsPossibleWithGender(boolean[] _Index, int[] _availableM, int[] _availableF, int _statsToDo) {

        int[] localAvM;
        int[] localAvF;
        for (int i = 0; i < NumStats; i++) {
            if (!_Index[i]) continue;
            boolean[] leftIndex = _Index.clone();
            leftIndex[i] = false;
            ReturnClass leftRes;
            if (_statsToDo == 3) {
                leftRes = GenderSubFuntion(leftIndex, _availableM, _availableF, true);
                if(!leftRes.sucess) leftRes = GenderSubFuntion(leftIndex, _availableM, _availableF, false);
            }else {
                leftRes = IsPossibleWithGender(leftIndex,_availableM,_availableF,_statsToDo-1);
            }
            if(!leftRes.sucess) continue;
            localAvM = _availableM.clone();
            localAvF = _availableF.clone();
            for (int k=0;k<NumStats;k++){
                localAvF[k] -= leftRes.usedF[k];
                localAvM[k] -= leftRes.used[k];
            }
            for (int j = 0; j < NumStats; j++) {
                if(j==i||!_Index[j]) continue;
                boolean[] rightIndex = _Index.clone();
                rightIndex[j] = false;
                ReturnClass rightRes;
                if (_statsToDo == 3) {
                    rightRes = GenderSubFuntion(rightIndex, localAvM, localAvF, true);
                    if(!rightRes.sucess)  rightRes = GenderSubFuntion(rightIndex, localAvM, localAvF, false);
                }else {
                    rightRes = IsPossibleWithGender(rightIndex,localAvM,localAvF,_statsToDo-1);
                }
                if(rightRes.sucess){
                    for (int k=0;k<NumStats;k++){
                        rightRes.usedF[k] += leftRes.usedF[k];
                        rightRes.used[k] += leftRes.used[k];
                    }
                    return new ReturnClass(rightRes.used, rightRes.usedF, leftRes.outString+rightRes.outString,true );
                }
            }
        }

       /* for (int i = 0; i < NumStats; i++) {
            if (!_Index[i]) continue;
            boolean[] leftIndex = _Index.clone();
            leftIndex[i] = false;
            ReturnClass leftRes;
            if (_statsToDo == 3) {
                leftRes = GenderSubFuntion(leftIndex, _availableM, _availableF, false);
            }else {
                leftRes = IsPossibleWithGender(leftIndex,_availableM,_availableF,_statsToDo-1);
            }
            if(!leftRes.sucess) continue;
            localAvM = _availableM.clone();
            localAvF = _availableF.clone();
            for (int k=0;k<NumStats;k++){
                localAvF[k] -= leftRes.usedF[k];
                localAvM[k] -= leftRes.used[k];
            }
            for (int j = 0; j < NumStats; j++) {
                if(j==i||!_Index[j]) continue;
                boolean[] rightIndex = _Index.clone();
                rightIndex[j] = false;
                ReturnClass rightRes;
                if (_statsToDo == 3) {
                    rightRes = GenderSubFuntion(rightIndex, localAvM, localAvF, true);
                }else {
                    rightRes = IsPossibleWithGender(rightIndex,localAvM,localAvF,_statsToDo-1);
                }
                if(rightRes.sucess){
                    for (int k=0;k<NumStats;k++){
                        rightRes.usedF[k] += leftRes.usedF[k];
                        rightRes.used[k] += leftRes.used[k];
                    }
                    return new ReturnClass(rightRes.used, rightRes.usedF, leftRes.outString+rightRes.outString,true );
                }
            }
        }*/
        return new ReturnClass(false);
    }

    private static ReturnClass GenderSubFuntion(boolean[] _Index, int[] _availableM, int[] _availableF, boolean firstIsFemale) {
        int first = -1;
        int second=-1;
        String outString = "";
        int[] usedF = new int[NumStats];
        int[] usedM = new int[NumStats];
        for (int i = 0; i < NumStats; i++) {
            if (_Index[i]) {
                if (first == -1) {
                    if (firstIsFemale) {
                        if (_availableF[i] > 0) {
                            first = i;
                            usedF[i]++;
                            outString += Names[i] + "F+";
                        } else {
                            return new ReturnClass(false);
                        }
                    } else if (_availableM[i] > 0) {
                        first = i;
                        usedM[i]++;
                        outString += Names[i] + "M+";
                    } else {
                        return new ReturnClass(false);
                    }
                } else if (!firstIsFemale) {
                    if (_availableF[i] > 0) {
                        second=i;
                        usedF[i]++;
                        outString += Names[i] + "F | ";
                    } else {
                        return new ReturnClass(false);
                    }
                } else if (_availableM[i] > 0) {
                    second=i;
                    usedM[i]++;
                    outString += Names[i] + "M | ";
                } else {
                    return new ReturnClass(false);
                }
            }
        }
        if(second==-1){
            return new ReturnClass(false);
        }
        return new ReturnClass(usedM, usedF, outString, true);
    }

    private static ReturnClass IsPossible(boolean[] _Index, int[] _available, int _statsToDo) {
        int[] available = _available.clone();
        if (_statsToDo == 2) {
            String returnString = "";
            int[] used = new int[NumStats];
            for (int k = 0; k < NumStats; k++) {
                if (_Index[k]) {
                    if (available[k] > 0) {
                        used[k]++;
                        returnString += Names[k];
                        returnString += "+";
                    } else {
                        return new ReturnClass(false);
                    }
                }
            }
            returnString += " | ";
            return new ReturnClass(used, returnString, true);
        }
        int[] localAvailable = available.clone();
        for (int i = 0; i < NumStats; i++) {
            boolean[] left = _Index.clone();
            left[i] = false;
            ReturnClass leftRes = IsPossible(left, available, _statsToDo - 1);
            if (!leftRes.sucess) continue;
            for (int k = 0; k < NumStats; k++) {
                available[k] -= leftRes.used[k];
            }
            for (int j = i + 1; j < NumStats; j++) {
                if (_Index[i] && _Index[j]) {
                    boolean[] right = _Index.clone();
                    right[j] = false;
                    ReturnClass rightRes = IsPossible(right, available, _statsToDo - 1);
                    if (rightRes.sucess) {
                        for (int k = 0; k < NumStats; k++) {
                            rightRes.used[k] += leftRes.used[k];

                        }
                        return new ReturnClass(rightRes.used, leftRes.outString + rightRes.outString, true);
                    }

                    available = localAvailable.clone();
                }
            }
        }
        return new ReturnClass(false);
    }
}

