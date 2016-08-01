package people;

import act.Clock;
import act.Kill;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Created by geyao on 16/7/5.
 */
public class Wolf extends Player implements Kill{

    private int choice = 0;

    public Wolf( int number ){ // 构造函数
        super(number);
        this.setIdentity(3);
    }

    public int wolfkill( ArrayList<Integer> alivePlayers){ // 夜晚击杀函数, 参数是当前存货玩家的 座次数组
        System.out.println(alivePlayers+"可杀,请选择击杀目标");
        choice = 0; // 将choice置0
        Scanner in = new Scanner(System.in);
        int choiceKill=0;
//        Clock clock = new Clock();    计时器以后再做吧
//        clock.timeBegin(this,30);
        do{
            choiceKill = in.nextInt();
        }
        while ( confirmTarget( alivePlayers, choiceKill) == 0 && choice == 0 );
//        clock.c
        this.choice = choiceKill;
        return this.getChoice();
    }

    public int getChoice(){ // 查询击杀目标
        return choice;
    }

    public void clearChoice(){ // 重置击杀目标为0
        choice = 0;
    }

    public int kill( Player player ){
        if(player.getalive()){
            player.die();
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
