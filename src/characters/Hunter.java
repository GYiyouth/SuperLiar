package characters;

import RuleAlgorithm.Alive;
import act.Kill;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by geyao on 16/7/7.
 */
public class Hunter extends Village  {

    public Hunter(  ){ // 猎人构造函数
        super();
        this.setIdentity(5);
    }
    @Override
    public int die(int way){ // 猎人死亡函数

        if (Alive.Gods.size() <= 1){ // 如果猎人是最后一位神
            for (int i : Alive.Players.keySet()) // 给每一个玩家发送信息
                Alive.Players.get(i).sendMessage("猎人死亡, 由于猎人是最后一位神民, 神民败走, 狼人获胜!");
//            System.exit(0);
        }
        ArrayList<Integer> temp = new ArrayList<>(); // 新建一个包含所有存活玩家,但不包含将死玩家的数组
        temp.clear();
        temp.addAll(Alive.intPlayers);
        temp.removeAll(Alive.Leaving);

        jTextArea.append("你已死亡 请选择是否发动技能带走一位玩家\n");
        jTextArea.append("目前存活玩家序号为\n"+ temp+"\n");
        jTextArea.append("请输入欲击杀的玩家 不击杀则点放弃\n");

        int target = input(temp); //
        Alive.Players.get(target).die(1); // 猎人带走无遗言
        super.die(way);
        return this.getNumber();

    }

//    public int kill( Village village){ // 猎人击杀函数
//        if(village.getalive()){
//            village.die(1);
//            return 4;
//        }
//        else return 0;
//    }


}
