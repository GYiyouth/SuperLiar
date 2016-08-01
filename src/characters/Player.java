package characters;

import java.util.ArrayList;

/**
 * Created by geyao on 16/7/3.
 */
public class Player { // 玩家

    private int number = 0 ; // 玩家座次号
    private String name; // 玩家姓名
    private boolean alive = true; // 存活状态, true是存活
    private boolean WantPolice = false; // 试图上警,初始为没有上警经历
    private boolean isPolice = false; // 是否是警长,初始为非警长
    private boolean canTalk = false; // 是否有话语权,初始为没有话语权
    private int Talktimes = 0; // 发言次数
    private int identity = 1 ; // 身份标志,0为中立,1位村民,2为神,3为狼

    public void setName( String name){ // 设置姓名
        this.name = name;
    }
    public int setNum( int number ){ // 设置座次号
        this.number = number;
        return number;
    }

    public void setIdentity(int number){ // 设置身份
        this.identity = number;
    }
    public int getIdentity(){ // 获取身份信息
        return this.identity;
    }

    public void print(){ // 向玩家打印该玩家目前状态
        System.out.println("");
        System.out.println(this.getNumber()+"号的状态如下");
        System.out.println("存活状态是"+this.getalive());
        if(this.isWantPolice())
            System.out.println("参加了警长竞选");
        else
            System.out.println("没有参加警长竞选");
        if(this.getPolice())
            System.out.println("目前是警长");
        else
            System.out.println("目前不是警长");
        System.out.println("发言共计"+this.talk()+"次");
    }

    public void die(){ // 所有玩家都可能会死,死亡函数采用die接口
        this.alive = false;
    }

    public boolean getalive(){ // 获取玩家存活状态
        return this.alive;
    }

    public boolean reborn( Player deadplayer ){ // 使某位玩家重生
        if( deadplayer.getalive() == false ) {
            deadplayer.alive = true;
            return true;
        }
        else
            return false;
    }

    public boolean reborn(){ // 玩家重生
        if( this.getalive() == false ){
            this.alive = true;
            return true;
        }
        return false;
    }

    public String getName( Player player ){ // 获取玩家姓名
        return player.name;
    }

    public String getName(){ // 获取玩家姓名
        return this.name;
    }

    public boolean isWantPolice(){ // 是否有上警经历反馈
        return this.WantPolice;
    }

    public boolean getPolice( Player player ){ // 查询玩家是否为警长
        return player.isPolice;
    }

    public boolean getPolice(){ // 查询玩家是否位警长
        return this.isPolice;
    }

    public boolean setPolice(){ // 设定该玩家为警长,若原本便为警长,返回false,否则返回true
        if(this.getPolice())
            return false;
        this.isPolice = true;
        return true;
    }

    public boolean removePolice( Player player ){ // 让玩家下警
        if (this.getPolice()) // 该玩家必须先为警长
            this.isPolice = false;
        else
            return false;
        return true;
    }

    public boolean setPolice( Player nextpolice ){ // 将括号里的玩家设定为下一位警长,原警长自动下警
        if( this.getPolice() && nextpolice.getPolice() == false ){ // 原玩家是警长,下一位玩家不是警长
            removePolice(this);
            nextpolice.setPolice();
            return true;
        }
        return false;
    }

    public boolean getTalkRight(Player player){ // 查询玩家是否可以发言
        return player.canTalk;
    }

    public boolean getTalkRight(){ // 查询玩家是否可以发言
        return this.canTalk;
    }

    public int talk(){ // 发言函数
        if (this.getTalkRight()==false)
            return Talktimes;
        else
            return ++Talktimes;
    }

    public int getNumber(){ // 返回玩家座次号
        return this.number;
    }



    public Player( int number ){ // 构造函数
        if( number>0 && number<=24 )
            this.setNum(number);
        else
            System.out.println("座次号超出范围,请输入1-24间的号码");
    }

    @Override
    public String toString(){ // ToString
        switch (this.identity){
            case 1: return "该玩家是村民";
            case 2: return "该玩家是神";
            case 3: return "该玩家是狼";
            default:return "该玩家独立阵营";
        }
    }

    public int vote( ArrayList<Integer> aliveplayers ){
        return aliveplayers.get( (int)(Math.random()*aliveplayers.size()) );
    }

    public boolean equals( Player player ){ // 比较函数, 玩家座次号不一致即可
        if(  this.getNumber() != player.getNumber() )
            return true;
        return false;
    }



}
