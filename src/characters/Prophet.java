package characters;

/**
 * Created by geyao on 16/7/4.
 */
public class Prophet extends Village { // 预言家

    public Prophet(){ // 预言家构造函数
        super();
        this.setIdentity(2);
    }

    public int getIdentity(Village village){ // 可以查询某个玩家的身份
        return village.getIdentity();
    }
}
