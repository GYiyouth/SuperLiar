package people;

/**
 * Created by geyao on 16/7/4.
 */
public class Prophet extends Player { // 预言家

    public Prophet(int number){ // 预言家构造函数
        super(number);
        this.setIdentity(2);
    }

    public int getIdentity(Player player){ // 可以查询某个玩家的身份
        return player.getIdentity();
    }
}
