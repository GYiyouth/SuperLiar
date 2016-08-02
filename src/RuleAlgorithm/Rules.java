package RuleAlgorithm;

/**
 * Created by geyao on 16/8/1. Rules for day and night
 */
public class Rules {


    private final int totalNumber = this.creatPlayerNumbers(); // 产生随机8~12名玩家
    private Group group = new Group(totalNumber);

    public int creatPlayerNumbers(){ // 获取玩家个数, 目前自己产生, 日后争取从服务器获取
        int numbers = (int)(( 5 * Math.random() )+8 ); // double转int, 舍去小数部分
        return numbers;
    }

    public boolean creatCharacters(){
        return true;
    }

}
