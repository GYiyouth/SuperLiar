package characters;

/**
 * Created by geyao on 16/7/4.
 */
public class Prophet extends Village { // 预言家

    public Prophet(){ // 预言家构造函数
        super();
        this.setIdentity(3);
    }

    public int checkIdentity(Village village){ // 可以查询某个玩家的身份
        System.out.print(village.getNumber()+"号玩家的身份是");
        switch (village.getIdentity()){
            case 0:this.sendMessage("中立"); break;
            case 1:this.sendMessage("好人"); break;
            case 2:this.sendMessage("好人"); break;
            case 3:this.sendMessage("坏人"); break;
        }
        return village.getIdentity();
    }
}
