package RuleAlgorithm;

import characters.*;
import com.sun.istack.internal.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by geyao on 16/8/1.
 */
public class Group {
    private final int tableNumber = this.creatPlayerNumbers(); // 产生随机8~12名玩家
    private Scanner in = new Scanner(System.in);
    public int creatPlayerNumbers(){ // 获取玩家个数, 目前自己产生, 日后争取从服务器获取
        int numbers = (int)(( 5 * Math.random() )+8 ); // double转int, 舍去小数部分
        return numbers;
    }

    private int[] intPlayers ; // 所有玩家, 标志其存活状态, 1为生, 2为死
    private ArrayList<Integer> intAlivePlayers = new ArrayList<>(); // 存活玩家的数组,包含玩家座次号
     ArrayList<Integer> WfKill = new ArrayList<>(); // 狼人刀杀的目标
    private ArrayList<Integer> VoteKill = new ArrayList<>(); // 票杀的目标组合
    private ArrayList<Integer> Leaving = new ArrayList<>(); // 即将离场的人



    private HashMap<Integer, Village> MapPlayers = new HashMap<>(); // 数字对应身份 排列的哈希表
//    private HashMap<Integer, Village> Alive.Villagers = new HashMap<>();
//    private HashMap<Integer, Village> Alive.Gods = new HashMap<>();
//    private HashMap<Integer, Village> Alive.Wolves = new HashMap<>();
//    private  HashMap<Integer, Village> Alive.Players = new HashMap<>(); // 数字对应座次号 排列的哈希表



    public Group(  ){ // 构造函数

        intPlayers = new int[tableNumber];
//        MapPlayers = new HashMap<>(tableNumber);
//        AryPlayers = new ArrayList<>(tableNumber);
        /**
         *  玩家  民 神 狼
         *   8   2  3  3
         *   9   3  3  3
         *  10   4  3  3
         *  11   3  4  4
         *  12   4  4  4
         *  注意,0预言家 1女巫 2猎人 9白痴(11人局以上有)
         * */

        Alive.Gods.put(0, new Prophet());
        Alive.Gods.put(1, Witch.WitchBorn());
        Alive.Gods.put(2, new Hunter());
        for (int i = 3; i < 6; i++)
            Alive.Wolves.put(i, new Wolf());
        for (int i = 6; i < 8; i++)
            Alive.Villagers.put(i, new Village());
        // 至此, 神3, 狼3, 民2
        if (tableNumber >= 9)
            Alive.Villagers.put(8, new Village());
        // 至此,神3, 狼3, 民3
        if (tableNumber >=10 )
            Alive.Villagers.put(9, new Village());
        // 至此, 神3, 狼3, 民3, 民1
        if (tableNumber >= 11 ){
            Alive.Villagers.remove(9);
            Alive.Gods.put(9, new Fool());
            Alive.Wolves.put(10, new Wolf());
        }
        // 至此, 神3, 狼3, 民3, 神1, 狼1
        if (tableNumber == 12 )
            Alive.Villagers.put(11, new Village());
        // 至此, 神3, 狼3, 民3, 神1, 狼1, 民1 ,可以合并了
        MapPlayers.putAll(Alive.Gods);
        MapPlayers.putAll(Alive.Villagers);
        MapPlayers.putAll(Alive.Wolves);
        // 合并完成


        for (int i = 0 ; i < tableNumber ; i ++ ) { // 将存活玩家的 【序号】 添加到数组中
            intPlayers[i] = 1; //
            Alive.intPlayers.add(i);
        }


        for (int i = 0; i < intPlayers.length ; i++ ){ // 把玩家序号依次撒到MapPlayers上面
            boolean done = false;
            while (!done) {
                double random = Math.random();
                int num = (int) (random * tableNumber); // num 为0 ~ tableNumber-1 的整形数,刚好对应下标
                if (MapPlayers.get(num).getNumber() == -1){
                    MapPlayers.get(num).setNum(i);
                    done = true;
                }
            } // while()
        } // for() 至此, 每个Village都有了独一无二的座次号




        for (HashMap.Entry<Integer, Village> entry : MapPlayers.entrySet()) { // 创建有序新表
            Alive.Players.put(entry.getValue().getNumber(), entry.getValue());
        }
        for (HashMap.Entry<Integer, Village> entry : Alive.Players.entrySet()) { // 遍历新表,打印
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
        }
        {// 创建有序新表
            HashMap<Integer, Village> map = new HashMap<>();
            for (HashMap.Entry<Integer, Village> entry : Alive.Gods.entrySet()) { // 神的新表
                map.put(entry.getValue().getNumber(), entry.getValue());
            }

            Alive.Gods.clear();
            Alive.Gods.putAll(map);
            map.clear();
            for (HashMap.Entry<Integer, Village> entry : Alive.Villagers.entrySet()) { // 民的新表
                map.put(entry.getValue().getNumber(), entry.getValue());
            }

            Alive.Villagers.clear();
            Alive.Villagers.putAll(map);
            map.clear();
            for (HashMap.Entry<Integer, Village> entry : Alive.Wolves.entrySet()) { // 狼的新表
                map.put(entry.getValue().getNumber(), entry.getValue());
            }
            Alive.Wolves.clear();
            Alive.Wolves.putAll(map);
            map.clear();
        }
        for (HashMap.Entry<Integer, Village> entry : Alive.Gods.entrySet()) { // 遍历新表,打印
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
        }for (HashMap.Entry<Integer, Village> entry : Alive.Villagers.entrySet()) { // 遍历新表,打印
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
        }for (HashMap.Entry<Integer, Village> entry : Alive.Wolves.entrySet()) { // 遍历新表,打印
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
        }
//        Alive.intPlayers.clear();
//        Alive.intPlayers.addAll(Alive.intPlayers); // 将存活玩家添加到全局变量类中
    } // 构造函数

    public HashMap getHisMap(int num){ // 根据座位号返回玩家所在阵营的HashMap
        Village V = MapPlayers.get(num);
        if (Alive.Gods.containsKey(V.getNumber()))
            return Alive.Gods;
        if (Alive.Villagers.containsKey(V.getNumber()))
            return Alive.Villagers;
        if (Alive.Wolves.containsKey(V.getNumber()))
            return Alive.Wolves;
        else
            return Alive.Players;
    }

    public Village getPlayers(int num){ // 根据座位号返回玩家
        return Alive.Players.get(num);
    }

    public void kill(int num){ // 杀死某位玩家, 死亡函数
        if (Alive.Players.get(num).getalive()){
            Alive.Players.get(num).die(1);
        }

    }

    public void leave (int num){ // 离场
        if (!Alive.Players.get(num).getalive() && !Alive.Players.get(num).isLeave()){
            Alive.Players.get(num).leave();
            getHisMap(num).remove(num);
            int len = Alive.intPlayers.size();
            for (int i =0; i< len; i++){ // 从存活玩家数组中将其移除
                if (Alive.intPlayers.get(i) == num ) {
                    Alive.intPlayers.remove(i);
                    break;
                }
            }
            if (isGameEndAndWhoLose() !=0 ){
                //调用游戏结束函数
            }
        }
    }

    public Prophet getProphet(){ // 获取预言家
        return (Prophet) MapPlayers.get(0);
    }

    public Witch getWitch(){ // 获取女巫
        return (Witch) MapPlayers.get(1);
    }

    public Hunter getHunter(){ // 获取猎人
        return (Hunter) MapPlayers.get(2);
    }

    public HashMap getWolvesMap(){ // 获取狼人的HashMap
        return Alive.Wolves;
    }

    public HashMap getVillagersMap(){ // 获取村民的HashMap
        return Alive.Villagers;
    }

    public HashMap getGodsMap(){ // 获取神的HashMap
        return Alive.Gods;
    }

    public int isGameEndAndWhoLose(){ // 判断游戏是否结束, 返回失败方
        boolean WfLost = true;
        boolean VillagerLost = true;
        boolean GodLost = true;
        for (int x : Alive.intPlayers){
            if (Alive.Wolves.containsKey(x))
                WfLost = false;
            if (Alive.Villagers.containsKey(x))
                VillagerLost = false;
            if (Alive.Gods.containsKey(x))
                GodLost = false;
        }
        if (WfLost) { // 狼输返3
            System.out.println("狼人败走,  好人团队获胜,游戏结束!");
            System.exit(0);
            return 3;
        }
        if (GodLost) { // 神输返2
            System.out.println("神民败走, 狼人团队获胜,游戏结束!");
            System.exit(0);
            return 2;
        }
        if (VillagerLost) {// 民输返1
            System.out.println("平民败走, 狼人团队获胜,游戏结束!");
            System.exit(0);
            return 1;
        }
        return 0; // 游戏继续
    }

    public void Night(){ // 夜晚函数,主要阶段是狼人刀,女巫救,预言家查看
        System.out.println("【【【天黑了!!!】】】");
        ProphetAct();
        WolfAct();
        WitchAct();
        // 此时留下了Leaving数组给上帝宣布死亡
    }



    public void FirstDay(){ // 第一个白天函数, 主要阶段是上警,宣布死亡结果, 组织发言顺序, 发言, 投票
        // 宣布死亡结果
        System.out.println("【【【天亮了!!!】】】");
        creatPolice(); // 先产生警长
        if (Leaving.isEmpty())
            System.out.println("昨天晚上是平安夜");
        else {
            System.out.println("昨天晚上死的人是\t"+ Leaving);
        }
        for (int i : Leaving){ // 死亡玩家离场, 第一天有遗言
            Alive.Players.get(i).die(2);
            isGameEndAndWhoLose(); // 判胜负
            Alive.Players.get(i).leave();
        }
        TalkInOrders(); // 存活玩家依次发言
        int KilledByVote = Vote(Alive.intPlayers); // 得到票出的玩家的座次号
        Alive.Players.get(KilledByVote).die(3); // 票死, 有遗言
        isGameEndAndWhoLose(); // 判胜负
        Alive.Players.get(KilledByVote).leave();

    }

    public void Day(){ // 白天函数
        System.out.println("【【【天亮了!!!】】】");
        if (Leaving.isEmpty())
            System.out.println("昨天晚上是平安夜");
        else {
            System.out.println("昨天晚上死的人是\t"+ Leaving);
        }
        for (int i : Leaving){ // 死亡玩家离场, 无遗言
            Alive.Players.get(i).die(1);
            isGameEndAndWhoLose(); // 判胜负
            Alive.Players.get(i).leave();
        }
        TalkInOrders(); // 存活玩家依次发言
        int KilledByVote = Vote(Alive.intPlayers); // 得到票出的玩家的座次号
        Alive.Players.get(KilledByVote).die(3); // 票死, 有遗言
        isGameEndAndWhoLose(); // 判胜负
        Alive.Players.get(KilledByVote).leave();
    }

    public int WolfAct(){ // 狼人行动, 返回最后确定击杀的那一个
        /**
         * 这个函数不完整, 因为狼刀的过程, 各个狼之间可以眼神交流, 交换欲击杀的目标, 从而达成一致
         * */
        WfKill.clear(); // 上一轮刀杀目标残留在数组中,清空
        Leaving.clear(); // 将上一轮将离场玩家清除
        for (Village wolf : Alive.Wolves.values()) { // 遍历狼表,将欲击杀目标座次号添加到WfKill数组中
            Wolf wf = (Wolf) wolf;
            wf.wolfkill(Alive.intPlayers, WfKill);
        }
        int count[] = new int[intPlayers.length]; // 统计谁的票最多
        for (int i : WfKill){
            count [i] ++;
        }
        int dyingMan = -1;
        int max = 0;
        for (int i = 0; i < intPlayers.length; i++){ // 找出被最多狼刀杀的那一个
            if (count[i] > max ){
                max = count[i];
                dyingMan = i;
            }
        }

        Leaving.add(dyingMan); // 将最终击杀目标添加到Leaving数组中

        return dyingMan;
    }

    public void WitchAct(){ // 女巫行动
        if (getWitch().getalive()) { // 女巫须存活

            if (this.getWitch().getAntitode()) { // 如果解药还在, 将告知刀杀目标并决定是否营救
                if (Leaving.get(0) != getWitch().getNumber()) { // 刀杀玩家不是女巫
                    System.out.println("女巫, 你的座次号是" + getWitch().getNumber() + " 今天晚上被击杀的玩家是" + Leaving.toString() + "\t 营救请输入1, 否则输入2");
                    if (in.nextInt() == 1) {
                        try {
//                            getWitch().save(Alive.Players.get(Leaving.get(0)));
                            getWitch().useAntitode();
                            Leaving.remove(0); // 营救后将被刀杀溢出将离场数组
                        } catch (Exception e) {
                            System.out.println("女巫不可自救");
                            WitchAct();
                            return;
                        }
                    }
                }
                else
                    System.out.println("女巫, 你的座次号是"+getWitch().getNumber()+"  今晚你被击杀, 不可自救");
            }
            else { // 解药已经没有了
                System.out.println("女巫, 你已经没有解药了, 将不会告诉你谁被击杀");
            }
            if (this.getWitch().getPoison()) { // 如果毒药还在
                System.out.println("当前场上玩家为"+Alive.intPlayers+" 号");
                System.out.println("你有一瓶毒药,你要用吗, 如果使用请输入欲毒玩家座次号,否则请输入-1");
                try {
                    int target = in.nextInt();
                    if (Alive.intPlayers.contains(target) && !Leaving.contains(target)){ // 该玩家在场, 且未被刀杀
                        getWitch().usePoison();
                        Leaving.add(target); // 将被毒杀玩家添加至离场玩家数组
                        Alive.intPlayers.remove(Alive.intPlayers.indexOf(target));
                    }
                }
                catch (Exception e){
                    WitchAct();
                    return;
                }
            }else {
                System.out.println("毒药已经用完, 无法使用");
            }
        }
    }

    public void ProphetAct(){ // 预言家行动
        if(getProphet().getalive()){ // 预言家存活
            System.out.println("预言家, 你的座次号是"+getProphet().getNumber()+"\n目前存活的玩家有\n"+ Alive.intPlayers);
            System.out.println("请选择要查看的玩家身份, 并输入数字");

            try{
                int target = in.nextInt();
                getProphet().checkIdentity(Alive.Players.get(target));
            }
            catch (Exception e){
                //
            }
        }
    }

    public void creatPolice(){ // 上警函数,主要阶段包括,读取每个人的上警状态,依次发言,其余玩家投票

        int[] wantPolice = new int[tableNumber]; // 和玩家个数等长的数组, 下标代表座次号, 值1为上警
        for (int i =0; i < tableNumber; i++){ // 遍历玩家, 产生两个数组, 分别包含上警与不上警玩家,
            System.out.println(i+"号玩家, 你是否要竞选警长, 警长可以组织发言, 并有归票权, 且一票顶两票");
            System.out.println("竞选请输入1, 不竞选请输入2");
            if (in.nextInt() == 1){
                Alive.Players.get(i).wantPolice();
                wantPolice[i] = 1;
            }
        }
        int candidate = 0; // 统计候选人个数, 0 无警长, 1无发言与投票, 多个则都有
        for (int i : wantPolice){
            candidate += i;
        }
        switch (candidate){ // 根据竞选人不同, 情况不同
            case 0: System.out.println("无人竞选警长,本局游戏无警长角色");
                break;
            case 1:{ // 仅一人竞选
                int target = 0;
                for (int i : wantPolice){ // wantPolice和总玩家数目等长, 遍历过程中, 数组下标等于玩家座次号
                    if (i == 1) // 找到了唯一得票者
                        break;
                    target++; //
                }
                Alive.Players.get(target).setPolice();  // 此时target直接等于座次号, 该玩家当选警长
            } break;
            default:{ // 多人竞选警长
                ArrayList<Integer> candidates = new ArrayList<>();
                ArrayList<Integer> voters = new ArrayList<>();
                for (int i=0; i < tableNumber; i++){ // 将所有玩家添加到投票数组中
                    voters.add(i);
                }
                int target = 0;
                for (int i : wantPolice){ // 生成分别包含竞选人和投票人的两个数组, i仅为 0 或 1
                    if (i == 1) {
                        candidates.add(target); // 将候选人座次号添加
                        int num;
                        num = voters.indexOf(target);
                        voters.remove(num); // 将该候选人从投票人中移除
                    }
                    target++;
                }
                for (int i : candidates){ // 每个候选人发言, 次序为升序
                    Alive.Players.get(i).talk();
                }
                System.out.println("目前竞选玩家为"+ candidates);
                int len = candidates.size();
                for (int i =len -1; i >= 0; i--){ // 投票前是否要退水 , i 为玩家座次号
                    System.out.println(candidates.get(i)+"号玩家, 是否退水, 退水请输入1, 否则输入2");
                    int rst = in.nextInt();
                    if (rst == 1 ){ // 退水, 则从竞选人数组脱离, 而不添加至投票人数组

                            candidates.remove(i);
                    }

                }
                if (candidates.size() == 1){
                    System.out.println("仅剩一名玩家上警, "+candidates.get(0)+" 号玩家当选警长");
                    Alive.Players.get(candidates.get(0)).setPolice();
                    return;
                }
                System.out.println("最终竞选玩家为"+candidates);
                int Police = Vote(voters, candidates);
                System.out.println(Police+"号玩家当选警长");
                Alive.Players.get(Police).setPolice(); // 将该玩家设为警长

            }
        }
    }

    public int Vote(ArrayList<Integer> one, ArrayList<Integer> two){ // 投票函数
        ArrayList<Integer> Voters = new ArrayList<>();
        Voters.addAll(one);
        ArrayList<Integer> Candidates = new ArrayList<>();
        Candidates.addAll(two);


        int [] result = new int[Candidates.size()];
        for (int i =0 ; i < Candidates.size(); i++)
            result[i] = 0;

        for (int num: Voters){ // 每个投票人进行投票, 结果放在result里, result下标代表Candidates元素的顺序, 内容代表得票数
            System.out.println(num+" 号玩家, 现在进行投票,候选人有"+ Candidates);
            System.out.println("请输入数字告诉我你的结果, 输入-1 视为弃权");
            int target = in.nextInt(); // 目标玩家座次号
            if (Candidates.contains(target)){ // 未弃权
                int x = Candidates.indexOf(target);
                result[x]++;
                if (num == getPolice()) // num为投票者座次号, 如果是警长,再加一票
                    result[x]++;
            }
        }
        // 计票
        int max = 1; // 最大票数
        ArrayList<Integer> index = new ArrayList<>(); // 最高票得主
        index.clear();
        for (int i = 0 ; i < result.length; i++){ // 一轮计票结束, i 为result和Candidates的下标, 得到玩家座次号还要get(i)
            if (result[i] > max){ // 新最高票产生, 三个数据皆更新
                max = result[i];
                index.clear();
                index.add(Candidates.get(i)); // 添加玩家座次号
            }else {
                if (result[i] == max) { // 产生平票
                    index.add(Candidates.get(i)); // 添加玩家座次号
                }
            }
        }
        switch (index.size()){ // 根据平票个数来判定, 如果为0 说明皆弃权,再次投票, 为1 产生结果, 为多个则需要再次投票
            case 0:{
                System.out.println("无人投票, 再次发言, 发言后再次投票");
                for (int num : Candidates)
                    Alive.Players.get(num).talk(); // 候选人再次发言

                return Vote(Voters, Candidates); // 再次投票
            }
            case 1: return index.get(0); // 返回投票结果
            default:{ // 产生平票现象, 需要再次投票, index数组就是候选人数组,
//                for (int x : index) { // 依次去除平票玩家, x为座次号
//                    Candidates.remove(
//                            Candidates.indexOf(x) // 得到座次号为x的玩家在Candidates中的序号, 去除
//                    );
//                }
                for (int num : index)
                    Alive.Players.get(num).talk(); // 平票玩家再次发言
                return Vote(Voters, index);
            }
        }

    }

    public int Vote(ArrayList<Integer> Vt){ // 内部投票
        ArrayList<Integer> Voters = new ArrayList<>();
        Voters.addAll(Vt);
        int [] result = new int[Voters.size()]; // 下标代表序号, 内容代表票数

        for (int i : Voters) { // 每位玩家投票
            System.out.println("目前存活玩家为"+ Voters);
            System.out.println(i + "号玩家, 请投票, 输入玩家座次号, 弃票请输入 -1 ");
            int target = in.nextInt();
            if (Voters.contains(target)) { // 投票有效
                result[Voters.indexOf(target)]++; // 该玩家票数加1
                if (i == getPolice()) // 警长再加一票
                    result[Voters.indexOf(target)]++;
            }
        }
        // 计票 ,如果平票, 或者无人投票,则再次进行
        int max = 1; // 最大票数
        ArrayList<Integer> index = new ArrayList<>(); // 最高票得主
        for (int i = 0 ; i < result.length; i++){ // 一轮计票结束, i 为result和Voters的下标, 得到玩家座次号还要Voters.get(i)
            if (result[i] > max){ // 新最高票产生, 三个数据皆更新
                max = result[i];
                index.clear();
                index.add(Voters.get(i));
            }else
            if (result[i] == max){ // 产生平票
                index.add(Voters.get(i));
            }
        }

        switch (index.size()){ // 根据平票个数来判定, 如果为0 说明皆弃权,再次投票, 为1 产生结果, 为多个则需要再次投票
            case 0:{
                System.out.println("无人投票, 再次发言, 发言后再次投票");
                for (int num : Voters){ // 再次依次发言, num为座次号
                    Alive.Players.get(num).talk();
                }
                return Vote(Voters);
            }
            case 1:
                return index.get(0);
            default:{ // 产生平票现象, 需要再次投票, index数组就是候选人数组, 要从总数组中将其去除
                for (int x : index) { // 依次去除平票玩家, x为座次号
                    Voters.remove(
                            Voters.indexOf(x) // 得到座次号为x的玩家在Voters中的序号, 去除
                    );
                }
                for (int num : index)
                    Alive.Players.get(num).talk();
                return Vote(Voters, index);
            }
        }

    }
    public void TalkInOrders(){ // 如果无死者或者多个死者, 从警长开始发言, 否则从死左开始发言 。 警长总是最后发言
        if (Leaving.size() == 1){ // 如果仅有一位死者
            int x = Alive.intPlayers.indexOf(Leaving.get(0)); // x 为死者在Alive.intPlayers的index
            for (int i = x+1; i < Alive.intPlayers.size(); i++ ){ // x+1 ~ 最后一名玩家发言, i 同 x仅为index而非座次号
                if (Alive.intPlayers.get(i) == getPolice() ) // 如果该玩家是警长, 跳过
                    continue;
                Alive.Players.get(Alive.intPlayers.get(i)).talk();
            }
            for (int i =0 ; i < x; i ++){ // 0 ~ x-1 玩家发言
                if (Alive.intPlayers.get(i) == getPolice() ) // 跳过警长发言
                    continue;
                Alive.Players.get(Alive.intPlayers.get(i)).talk();
            }
            Alive.Players.get(getPolice()).talk(); // 警长发言
        }
        else { // 多个或者没有死者, 从警左开始发言
            // 找到警长的在存活玩家列表中的次序, 如果没有警长, 则从存活列表的第一位玩家发言
            int x = Alive.intPlayers.indexOf(getPolice()) +1 ; // x 为警长下一位玩家, 或者0号
            for (int i = x; i < Alive.intPlayers.size(); i ++){ // 从警长左边发言至最大号玩家
                Alive.Players.get(Alive.intPlayers.get(i)).talk();
            }
            for (int i =0; i < x; i++){ // 从零号玩家发言至警长
                Alive.Players.get(Alive.intPlayers.get(i)).talk();
            }
        }

    }

    public int getPolice(){ // 获取警长的座位号, 如果没有警长了, 返回
        for (int x : Alive.intPlayers){
            if (Alive.Players.get(x).getPolice())
                return x;
        }
        return -1;
    }

    public HashMap getPlayersMap( ){
        return Alive.Players;
    }

    public static void main(String[] args){
        Group one = new Group();

        one.Night();
        one.FirstDay();

        while(one.isGameEndAndWhoLose() == 0){
            one.Night();
            one.Day();
        }
    }
}
