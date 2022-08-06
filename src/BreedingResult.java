public class BreedingResult {

}
 class TreeNode{
    boolean [] stats=new boolean[7];
    boolean male=true;
    boolean genderIsRelevant=false;

    public TreeNode left,right;

    public TreeNode(boolean[] Stats, boolean Male,boolean GenderIsRelevant){
        stats=Stats;
        male=Male;
        genderIsRelevant=GenderIsRelevant;
    }

    public TreeNode(TreeNode Left, TreeNode Right,boolean[] Stats, boolean Male,boolean GenderIsRelevant){
        this(Stats, Male ,GenderIsRelevant);
        left=Left;
        right=Right;
    }
}
