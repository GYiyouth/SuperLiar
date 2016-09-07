package characters;

import RuleAlgorithm.Alive;

/**
 * Created by geyao on 16/7/4.
 */
public class Prophet extends Village { // 预言家

    public Prophet(){ // 预言家构造函数
        super();
        this.setIdentity(3);
    }

    public int checkIdentity(Village village){ // 可以查询某个玩家的身份
        sendMessage((village.getNumber() + 1)+"号玩家的身份是\n");
        switch (village.getIdentity()){
            case 0:this.sendMessage("中立\n"); break;
            case 1:this.sendMessage("好人\n"); break;
            case 2:this.sendMessage("坏人\n"); break;
            default:this.sendMessage("好人\n"); break;
        }
        return village.getIdentity();
    }
    @Override
    public void night(){
        while (Alive.nightKey) {
            while (Alive.voteKey[getNumber()]) { // 可以投票了
                sendMessage("\n预言家，你想查看谁的身份？\n");


                int res = input(); // 夜晚投票, 候选人即全部存活玩家
                Alive.candidates.get(this.getNumber()).clear(); // 投票完清空候选人列表
                if (res >= 0) { // 未弃票
                    Alive.voteResult[res]++; // 被投玩家加一票
                }
                Alive.voteKey[this.getNumber()] = false; // 已经投票完了
                return;
            }
        }
    }
}
