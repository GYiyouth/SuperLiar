package characters;

import java.util.ArrayList;

/**
 * Created by geyao on 16/7/3.
 */
public class Village { // 玩家

    private int number = -1 ; // 玩家座次号
    private String name; // 玩家姓名
    private boolean alive = true; // 存活状态, true是存活
    private boolean WantPolice = false; // 试图上警,初始为没有上警经历
    private boolean isPolice = false; // 是否是警长,初始为非警长
    private boolean canTalk = false; // 是否有话语权,初始为没有话语权
    private int Talktimes = 0; // 发言次数
    private int identity = 1 ; // 身份标志,0为中立,1位村民,2为神,3为狼
    private boolean isLeave = false; // 离场标志, false未离场, true 已经离场

    public void setName( String name){ // 设置姓名
        this.name = name;
    }
    public boolean setNum( int number ){ // 设置座次号
        if( (number>=0 && number<=24) && this.number == -1 ){
            this.number = number;
            return true;
        }
       return false;
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

    public int die(){ // 所有玩家都可能会死
        this.alive = false;
        return this.getNumber();
    }



    public boolean leave(){ // 离场
        if (!this.getalive() && !isLeave ){
            isLeave = true;
            return true;
        }
        return false;
    }

    public boolean isLeave(){
        return isLeave;
    }

    public boolean getalive(){ // 获取玩家存活状态
        return this.alive;
    }

    public boolean reborn( Village deadplayer ){ // 使某位玩家重生
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
/**
 * 以下这种方法是不安全的,向下转型的典型错误示范
 * **/
    public Village beVillage(){ // 身份转换为村民
        this.identity = 1;
        return this;
    }

    public Prophet beProphet(){ // 身份转换位预言家
        this.identity = 2;
        return (Prophet)this;
    }

    public Hunter beHunter(){ // 转换为猎人
        this.identity = 2;
        return (Hunter) this;
    }

    public Fool beFool(){ // 转换为猎人
        this.identity = 2 ;
        return (Fool) this;
    }


    public String getName( Village village){ // 获取玩家姓名
        return village.name;
    }

    public String getName(){ // 获取玩家姓名
        return this.name;
    }

    public boolean isWantPolice(){ // 是否有上警经历反馈
        return this.WantPolice;
    }

    public boolean getPolice( Village village){ // 查询玩家是否为警长
        return village.isPolice;
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

    public boolean removePolice( Village village){ // 让玩家下警
        if (this.getPolice()) // 该玩家必须先为警长
            this.isPolice = false;
        else
            return false;
        return true;
    }

    public boolean setPolice( Village nextpolice ){ // 将括号里的玩家设定为下一位警长,原警长自动下警
        if( this.getPolice() && nextpolice.getPolice() == false ){ // 原玩家是警长,下一位玩家不是警长
            removePolice(this);
            nextpolice.setPolice();
            return true;
        }
        return false;
    }

    public boolean getTalkRight(Village village){ // 查询玩家是否可以发言
        return village.canTalk;
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



    public Village(){ // 构造函数

    }

    @Override
    public String toString(){ // ToString
        switch (this.identity){
            case 1: return "该玩家是村民\t" + "他的座次号是" + this.getNumber();
            case 2: return "该玩家是神\t" + "他的座次号是" + this.getNumber();
            case 3: return "该玩家是狼\t" + "他的座次号是" + this.getNumber();
            default:return "该玩家独立阵营\t" + "他的座次号是" + this.getNumber();
        }
    }

    public int vote( ArrayList<Integer> aliveplayers ){
        return aliveplayers.get( (int)(Math.random()*aliveplayers.size()) );
    }

    public boolean equals( Village village){ // 比较函数, 玩家座次号不一致即可
        if(  this.getNumber() != village.getNumber() )
            return true;
        return false;
    }



}
