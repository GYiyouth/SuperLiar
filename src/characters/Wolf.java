package characters;

import act.Kill;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by geyao on 16/7/5.
 */
public class Wolf extends Village implements Kill{

    private int choice = 0;

    public Wolf( ){ // 构造函数
        super();
        this.setIdentity(3);
    }

    public int wolfkill( ArrayList<Integer> alivePlayers, ArrayList<Integer> WfKill){ // 夜晚击杀函数, 参数是当前存活玩家的 座次数组
        System.out.println(alivePlayers+"可杀,请输入欲击杀玩家座次号");
        choice = -1; // 将choice置 -1
        Scanner in = new Scanner(System.in);
        int choiceKill=0;
//        Clock clock = new Clock();    计时器以后再做吧
//        clock.timeBegin(this,30);
        do{
            choiceKill = in.nextInt();
        }
        while ( confirmTarget( alivePlayers, choiceKill) == -1 && choice == -1 );
//        clock.c
        this.choice = choiceKill;
        WfKill.add(choice);
        return this.getChoice();
    }

    public int getChoice(){ // 查询击杀目标
        return choice;
    }

    public void clearChoice(){ // 重置击杀目标为0
        choice = 0;
    }

    public int kill( Village village){
        if(village.getalive()){
            village.die();
            return 1;
        }
        else
            return 0;
    }

    public String print( String god ){
        super.print();
        System.out.println("该玩家是狼人");
        return "该玩家是狼人";
    }

    private int confirmTarget( ArrayList<Integer> alivePlayers, int target){ // 确定数组里是否有目标元素
        for( int a: alivePlayers ){
            if (a == target)
                return a;
        }
        return 0;
    }
}
