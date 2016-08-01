package OthersPlayers;

/**
 * Created by geyao on 16/7/5.
 */
public class God { // 上帝 , 饿汉单例模式

    private God(){

    }

    private static God god = new God();

    public God getGod(){ // God访问函数
        return god;
    }
}
