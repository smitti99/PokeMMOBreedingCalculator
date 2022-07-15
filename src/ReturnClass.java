public class ReturnClass {
    int[] used;
    int[] usedF;
    String outString;
    boolean sucess;

    ReturnClass(int[] Used,String out,boolean success){
        used = Used;
        outString=out;
        sucess=success;
    }
    ReturnClass(int[] Used,int[] UsedF,String out,boolean success){
        used = Used;
        outString=out;
        sucess=success;
        usedF = UsedF;
    }
    ReturnClass(boolean success){
        sucess=success;
    }
}
