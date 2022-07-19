import java.util.Scanner;

public class main {

    //Variables
    static String[] Names;
    static int NumStats;
    static boolean Gender;
    static boolean Natured;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("\nDoes the Pokemon you want to breed have a gender? [Y/N]\n");
        String answer = scanner.next();
        if (answer.equalsIgnoreCase("n")) {
            Gender = false;
            System.out.print("\nDo you want it to be natured?[Y/N] \n");
            Natured = scanner.next().equalsIgnoreCase("y");
            System.out.print("\nHow many perfect IVs do you want on the final pokemon? \n");
            NumStats += scanner.nextInt();
            if (NumStats <= 1) {
                System.out.print("\n...really?\n");
                return;
            }
            if(Natured){
                NumStats++;
            }
            boolean[] Index = new boolean[NumStats];
            int[] available = new int[NumStats];
            Names = new String[NumStats];
            if(Natured){
                Index[0]=true;
                available[0]=1;
                Names[0] = "Nature";
            }
            int statCount=1;
            for (int i = Natured?1:0; i < NumStats; i++) {
                System.out.print("\nName the "+statCount+". IV :\n");
                statCount++;
                Names[i] = scanner.next();
                System.out.print("\nHow many pokemon with perfect "+Names[i]+" do you have?\n");
                available[i] = scanner.nextInt();
                Index[i] = true;
            }
            ReturnClass res = IsPossible(Index, available, NumStats);
            if (res.sucess) {
                System.out.println("\nSuccess!\nBreed like this :\n");
                System.out.println(res.outString);
            } else {
                System.out.println("\nNo Breeding-Tree could be build with this combination\n");
            }
        } else {
            Gender =true;
            System.out.print("\nDo you want it to be natured?[Y/N] \n");
            Natured = scanner.next().equalsIgnoreCase("y");
            boolean NaturedIsMale=false;
            if(Natured){
                NumStats=1;
                System.out.print("\nIs your natured breeding pokemon male?[Y/N] \n");
                NaturedIsMale = scanner.next().equalsIgnoreCase("y");
            }
            System.out.print("\nHow many perfect IVs do you want on the final pokemon? \n");
            NumStats += scanner.nextInt();
            if (NumStats <= 1) {
                System.out.print("\n...really?\n");
                return;
            }
            boolean[] Index = new boolean[NumStats];
            int[] availableM = new int[NumStats];
            int[] availableF = new int[NumStats];
            Names = new String[NumStats];
            if(Natured){
                Index[0] = true;
                Names[0] = "Nature";
                if(NaturedIsMale){
                    availableM[0]=1;
                    availableF[0]=0;
                }else{
                    availableM[0]=0;
                    availableF[0]=1;
                }
            }
            int statCount=1;
            for (int i = Natured?1:0; i < NumStats; i++) {
                System.out.print("\nName the "+statCount+". IV :\n");
                statCount++;
                Names[i] = scanner.next();
                System.out.print("\nHow many MALE pokemon with perfect "+Names[i]+" do you have?\n");
                availableM[i] = scanner.nextInt();
                System.out.print("\nHow many FEMALE pokemon with perfect "+Names[i]+" do you have?\n");
                availableF[i] = scanner.nextInt();
                Index[i] = true;
            }
            ReturnClass res = IsPossibleWithGender(Index, availableM, availableF, NumStats);
            if (res.sucess) {
                System.out.println("\nSuccess!\nBreed like this :\n");
                System.out.println(res.outString);
            } else {
                System.out.println("\nNo Breeding-Tree could be build with this combination\n");
            }
        }
        System.out.println("\n\nWould you like to know how expensive this breed will be? [Y/N]");
        answer = scanner.next();
        if (answer.equalsIgnoreCase("y")) {
            int BreedAmount = (int) (Math.pow(2,NumStats-1)-1);
            int priceForGender=0;
            int priceItems;
            int costEverstone=0;
            if(Natured) {
                System.out.println("\nWhat is the price for everstones? ");
                costEverstone = scanner.nextInt();
            }

            if(Gender){
                System.out.println("\nWhat is the price to force male?");
                int costMale = scanner.nextInt();
                System.out.println("\nWhat is the price to force female?");
                int costFemale = scanner.nextInt();
                double chanceMale=0;
                if(costFemale==costMale){
                    chanceMale=0.5;
                } else if (costFemale>costMale) {
                    if(costFemale==21000){
                        chanceMale = 0.875;
                    } else if (costFemale == 9000) {
                        chanceMale = 0.75;
                    }
                }else {
                    if(costMale==21000){
                        chanceMale = 0.125;
                    } else if (costMale == 9000) {
                        chanceMale = 0.25;
                    }
                }


                double averageCostGender = chanceMale*costMale+(1-chanceMale)*(costFemale);
                priceForGender =(int) averageCostGender*(BreedAmount-1)/2;
                System.out.println("------------------------ COSTS --------------------------");
                System.out.println("The average cost for choosing the gender is :  "+averageCostGender);
                System.out.println("You have to choose a minimum of :  "+(BreedAmount-1)/2+" genders");
                System.out.println("On average this breed needs  "+priceForGender+" PokeYen for choosing the gender");
            }else{
                System.out.println("------------------------ COSTS --------------------------");
            }
            System.out.println("You will need  "+BreedAmount*200+" PokeYen for Pokeballs");
            if(Natured){
                System.out.print("You will need  "+(NumStats-1)*costEverstone+" PokeYen for Everstones ");
                System.out.println("\nand  "+(BreedAmount*2-(NumStats-1))*10000+" PokeYen for BreedingItems");
                priceItems=(BreedAmount*2-(NumStats-1))*10000+(NumStats-1)*costEverstone+BreedAmount*200;
            }else {
                System.out.println("You need  "+(BreedAmount*2)*10000+" PokeYen for BreedingItems");
                priceItems =(BreedAmount*2)*10000+BreedAmount*200;
            }
            System.out.println("\nOverall you will need aproximatley  "+(priceForGender+priceItems)+" PokeYen for the complete breed");
            System.out.println("---------------------------------------------------------");
        }
    }

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
                            outString += Names[i] + "(F)+";
                        } else {
                            return new ReturnClass(false);
                        }
                    } else if (_availableM[i] > 0) {
                        first = i;
                        usedM[i]++;
                        outString += Names[i] + "(M)+";
                    } else {
                        return new ReturnClass(false);
                    }
                } else if (!firstIsFemale) {
                    if (_availableF[i] > 0) {
                        second=i;
                        usedF[i]++;
                        outString += Names[i] + "(F) | ";
                    } else {
                        return new ReturnClass(false);
                    }
                } else if (_availableM[i] > 0) {
                    second=i;
                    usedM[i]++;
                    outString += Names[i] + "(M) | ";
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

