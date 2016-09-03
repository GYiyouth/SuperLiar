package characters;

import RuleAlgorithm.Alive;
import act.MyButton;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by geyao on 16/7/3.
 */
public class Village implements Runnable { // 玩家

    private volatile int number = -1; // 玩家座次号
    private String name; // 玩家姓名
    private boolean alive = true; // 存活状态, true是存活
    private boolean WantPolice = false; // 试图上警,初始为没有上警经历
    private boolean isPolice = false; // 是否是警长,初始为非警长
    private boolean canTalk = false; // 是否有话语权,初始为没有话语权
    private int Talktimes = 0; // 发言次数
    private int identity = 1; // 身份标志,0为中立,1位村民,2为狼,3为预言家,4为女巫, 5为猎人, 6为白痴
    private boolean isLeave = false; // 离场标志, false未离场, true 已经离场
    public volatile Object talkKey = new Object();
    public volatile boolean runKey = false; // 控制线程运行节奏

    public Thread voteThread;
    public Thread chonfirmThread;
    public Thread village;
    //    private static volatile boolean endKey = false; // 控制游戏结束
//    private static volatile Object lockObjEnd = new Object(); // 结束游戏的key
    // GUI相关成员
    JFrame welcome = new JFrame("欢迎来到狼人杀游戏");
    Container container = welcome.getContentPane();

    JPanel north = new JPanel(new GridLayout(1, 4, 50, 5)); // 上方面板, 有四个按钮
    JPanel east = new JPanel(new GridLayout(2, 1, 20, 90)); // 右方面板, 2个按钮
    JPanel south = new JPanel(new GridLayout(2, 1, 90, 20)); // 下方面板, 第一排是确定取消键, 第二排是四个按钮键
    JPanel west = new JPanel(new GridLayout(2, 4, 20, 90)); // 左方面板, 2个按钮
    JPanel center = new JPanel(new GridLayout(0, 1, 20, 30)); // 中间面板
    JPanel talkPanel = new JPanel(new GridLayout(1, 0)); // 话语权面板, 用来狼人自爆或者说话PASS
    JButton pass = new JButton(); // 聊天过, 自爆按钮
    JTextArea jTextArea = new JTextArea();

    // 按钮, 每个都代表一个玩家
    private MyButtonGroup myButtonGroup;

    public void setName(String name) { // 设置姓名
        this.name = name;
    }

    public boolean setNum(int number) { // 设置座次号
        if ((number >= 0 && number <= 24) && this.number == -1) {
            this.number = number;
            return true;
        }
        return false;
    }

    public Village() { // 构造函数


        village = new Thread(this);
        village.start();
        chonfirmThread = new Thread(new Runnable() { // 投票线程, 需要调用者, 目前仅用于警长竞选
            @Override
            public void run() {

//                    while (Alive.voteKey[getNumber()]) {
                        if (MadeChoose()) // 如果上警
                            Alive.voteResult[getNumber()] = 1;
                        else
                            Alive.voteResult[getNumber()] = 0;
                        Alive.voteKey[getNumber()] = false; // 投完置0
                        return;
//                    }
                }

        });

    }



    @Override
    public void run() { // 线程run函数
        // 首先新建一个窗口





        while (this.getNumber() == -1)
            ; // 分配座次号
        welcome.setVisible(true);
        welcome.setSize(800, 600);
        welcome.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE); // 不让退出
        welcome.setLayout(new BorderLayout()); // 边缘布局
        container.add(center, BorderLayout.CENTER); // 添加中央面板
        container.add(north, BorderLayout.NORTH);
        container.add(south, BorderLayout.SOUTH);
        container.add(east, BorderLayout.EAST);
        container.add(west, BorderLayout.WEST);
        center.add(new JScrollPane(jTextArea)); // 文字区域文字区域启动滚动条
        center.add(talkPanel);
        talkPanel.setVisible(false); //  一开始不可见
        talkPanel.add(pass); // 添加按钮
        jTextArea.setLineWrap(true); // 自动换行

        myButtonGroup = new MyButtonGroup();
        village.setName("座次号为"+this.getNumber()+toString()); // 修改线程名字
        switch (this.getIdentity()) {
            case 1:
                welcome.setTitle("村民, 座次号为" + (this.getNumber() + 1 ) + "号");
                break;
            case 2: {
                welcome.setTitle("狼人, 座次号为" + (this.getNumber() + 1) + "号");
//                this.lightOthersButton(Alive.intWolves, Color.RED); // 狼人辨认同伴
            }
                break;
            case 3:
                welcome.setTitle("预言家, 座次号为" + (this.getNumber() + 1 ) + "号");
                break;
            case 4:
                welcome.setTitle("女巫, 座次号为" + (this.getNumber() + 1 ) + "号");
                break;
            case 5:
                welcome.setTitle("猎人, 座次号为" + (this.getNumber() + 1 ) + "号");
                break;
            case 6:
                welcome.setTitle("白痴, 座次号为" + (this.getNumber() + 1 ) + "号");
                break;
            default:
                break;
        }


        while (!Alive.endGame) { // 游戏未结束
            day();
            night();
        }
    }

    public void day(){ // 白天投票函数, 需要准备好Alive.candidates中的相应数组, 投票完会清空, 处理了警长的情况.
        while (Alive.dayKey){ // 白天关键词有效
            while (Alive.voteKey[this.getNumber()]){ // 轮到他投票了
                jTextArea.append("\n投票环节到, 选择目标玩家, 点确定提交\n");


                int res = input(Alive.candidates.get( this.getNumber() ) ); // 根据特定的数组, 得到投票结果
                Alive.candidates.get(this.getNumber()).clear(); // 投票完清空候选人列表
                if (res >= 0) { // 未弃票
                    Alive.voteResult[res] ++; // 被投玩家加一票
                    if (this.getPolice()) // 警长再加一票
                        Alive.voteResult[res]++;
                }
                Alive.voteKey[this.getNumber()] = false; // 投完置false结束循环
            }
        }
    }



    public void night(){ // 夜晚函数, 交给各个子类函数继承

    }

    public void setIdentity(int number) { // 设置身份
        this.identity = number;
    }

    public int getIdentity() { // 获取身份信息
        return this.identity;
    }

    public void print() { // 向玩家打印该玩家目前状态

        MessageToAll(this.getNumber() + "号的状态如下\n");
        MessageToAll("存活状态是" + this.getalive());
        if (this.isWantPolice())
            MessageToAll("\n参加了警长竞选\n");
        else
            MessageToAll("没有参加警长竞选\n");
        if (this.getPolice())
            MessageToAll("目前是警长\n");
        else
            MessageToAll("目前不是警长\n");
        MessageToAll("发言共计" + this.Talktimes + "次\n");
    }

    public int die(int way) { // 死要进行移交警徽, 判胜负的功能, 死亡时移出Alive.intPlayers数组,
        // 有遗言死way = 2, 无遗言死way = 1, 票死 = 3
        this.alive = false;
        switch (this.getIdentity()) { // 根据身份从相应的map中去除
            case 1: { // 村民身份
                Alive.Villagers.remove(this.getNumber());
                break;
            }
            case 2: { // 狼身份
                Alive.Wolves.remove(this.getNumber());
                break;
            }
            default: { // 狼身份
                Alive.Gods.remove(this.getNumber());
                break;
            }
        }

        // 根据死亡方式决定是否说话
        MessageToAll(this.getNumber() + "号玩家死亡\n");
        switch (way) {
            case 1:
                MessageToAll("无遗言\n");
                break;
            case 2: {
                MessageToAll("首夜被杀, 有遗言\n");
                this.talk();
            }
            break;
            case 3: {
                MessageToAll("被票死, 有遗言\n");
                this.talk();
            }
            break;
        }
        if (this.isPolice) { // 如果该玩家是警长, 则进行警徽移交或撕警徽
            ArrayList<Integer> temp = new ArrayList<>(); // 新建一个包含所有存活玩家,但不包含将死玩家的数组
            temp.clear();
            temp.addAll(Alive.intPlayers);
            temp.removeAll(Alive.Leaving);
            jTextArea.append("请选择移交警徽, 当前存活玩家如下, 请点击移交对象, \"放弃\"撕掉警徽\n");

            int target = input(temp); // 根据活人移交警徽
            if (temp.contains(target)) {
                this.setPolice(Alive.Players.get(target)); // 新的警长产生
            } else {
                MessageToAll("警徽将被撕毁\n");
            }
        }
        return this.getNumber();
    }


    public boolean leave() { // 离场
        if (!this.getalive() && !isLeave) {
            isLeave = true;
            return true;
        }
        return false;
    }

    public boolean isLeave() {
        return isLeave;
    }

    public boolean getalive() { // 获取玩家存活状态
        return this.alive;
    }

    public boolean reborn(Village deadplayer) { // 使某位玩家重生
        if (deadplayer.getalive() == false) {
            deadplayer.alive = true;
            return true;
        } else
            return false;
    }

    public boolean reborn() { // 玩家重生

        this.alive = true;
        return true;

    }

    public String getName(Village village) { // 获取玩家姓名
        return village.name;
    }

    public String getName() { // 获取玩家姓名
        return this.name;
    }

    public boolean isWantPolice() { // 是否有上警经历反馈
        return this.WantPolice;
    }

    public boolean getPolice(Village village) { // 查询玩家是否为警长
        return village.isPolice;
    }

    public boolean getPolice() { // 查询玩家是否位警长
        return this.isPolice;
    }

    public boolean setPolice() { // 设定该玩家为警长,若原本便为警长,返回false,否则返回true
        if (this.getPolice())
            return false;
        this.isPolice = true;
        return true;
    }

    public void wantPolice() { // 试图竞选警长
        this.WantPolice = true;
    }

    public boolean removePolice(Village village) { // 让玩家下警
        if (this.getPolice()) // 该玩家必须先为警长
            this.isPolice = false;
        else
            return false;
        return true;
    }

    public boolean setPolice(Village nextpolice) { // 将括号里的玩家设定为下一位警长,原警长自动下警
        if (this.getPolice() && nextpolice.getPolice() == false) { // 原玩家是警长,下一位玩家不是警长
            removePolice(this);
            nextpolice.setPolice();
            return true;
        }
        return false;
    }

    public boolean getTalkRight() { // 查询玩家是否可以发言
        return this.canTalk;
    }

    public synchronized void talk() { // 发言函数
        talkPanel.setVisible(true); // 对于可见
        lightOthersButton(Alive.intPlayers, Color.BLUE); // 通知其他玩家我在说话
        Thread timeLimit = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(99999); // 沉睡
                } catch (InterruptedException e) {
                    talkPanel.setVisible(false); // 隐藏可见
                    lightOthersButton(Alive.intPlayers, Color.BLACK); // 恢复颜色
                }
            }
        });
        timeLimit.start();
        pass.addActionListener(new ActionListener() { // 添加pass动作,
            @Override
            public void actionPerformed(ActionEvent e) {
                timeLimit.interrupt();
            }
        });
        try {
            timeLimit.join();
        } catch (InterruptedException e) {
            MessageToAll((getNumber()+1) + "号玩家发言结束 \n");
        }
        finally {
            talkPanel.setVisible(false); //
        }
        Talktimes++;
        return ;
    }

    public int getNumber() { // 返回玩家座次号
        return this.number;
    }


    @Override
    public String toString() { // ToString
        switch (this.identity) {
            case 1:
                return "该玩家是村民\t" + "他的座次号是" + this.getNumber();
            case 2:
                return "该玩家是狼\t" + "他的座次号是" + this.getNumber();
            case 3:
                return "该玩家是预言家\t" + "他的座次号是" + this.getNumber();
            case 4:
                return "该玩家是女巫\t" + "他的座次号是" + this.getNumber();
            case 5:
                return "该玩家是猎人\t" + "他的座次号是" + this.getNumber();
            case 6:
                return "该玩家是白痴\t" + "他的座次号是" + this.getNumber();

            default:
                return "该玩家独立阵营\t" + "他的座次号是" + this.getNumber();
        }
    }

    public int input(){ // ,候选人为Alive.intPlayers 获取玩家输入, 仅返回数字, 不修改数组
        return myButtonGroup.getKey();
    }

    public int input(ArrayList<Integer> list){ // 根据数组获取玩家输入, 仅有数组中的玩家供选择, 仅返回数字, 不修改数组
        return myButtonGroup.getKey(list);
    }

//    public int vote( ArrayList<Integer> aliveplayers ){
//        return aliveplayers.get( (int)(Math.random()*aliveplayers.size()) );
//    }

    public boolean equals(Village village) { // 比较函数, 玩家座次号不一致即可
        if (this.getNumber() != village.getNumber())
            return true;
        return false;
    }

    public void sendMessage(String message){ // 往jTestArea写东西
        jTextArea.append(message);
    }


    public void MessageToAll(String message){ // 给所有玩家无论死活发送消息
        for (Village village : Alive.Players.values())
            village.sendMessage(message);
    }

    public void sendMessage(String message, ArrayList<Integer> list){ // 给特定一组玩家发送信息
        for (int i : list){
            Alive.Players.get(i).sendMessage(message);
        }
    }

    public void sendMessage(String message, int num){ // 给特定玩家发送信息

        Alive.Players.get(num).sendMessage(message);

    }

    public boolean MadeChoose(){ // 是与否的选择
        return myButtonGroup.Choose();
    }

    public MyButtonGroup getMyButtonGroup(){ // 获取this.myButtonGroup
        return myButtonGroup;
    }

    public void lightOthersButton(ArrayList<Integer> list, Color color){ // 使其他玩家的 自己的按钮 变色
        for (int i : list){
            Alive.Players.get(i).getMyButtonGroup().getButton(this.getNumber()).setForeground(color);
        }
    }



    class MyButtonGroup { // 内部类, 控制12个按钮, 控制输入情况
        MyButton[] myButtons = new MyButton[12]; // 12个按钮
        JButton yes = new JButton("确定");
        JButton no = new JButton("放弃");
        JButton yes2 = new JButton("是");
        JButton no2 = new JButton("否");
        JButton chooseConfirm = new JButton("提交");
        private int num = -1; // 目前倾向
        private int key = -1; // 最终结果
        boolean close = false;  // 结束key, 点下确定后即设置位true
        boolean choose = false; // 是与否key, 默认是false
        boolean chooseMade = false; // 是 否 是否被点击的key
        Thread delay; // 做不好这个延时函数
        JPanel centerConfirm = new JPanel(new GridLayout(1, 0)); // 选择与否面板, 在中央
        public MyButtonGroup() { // 构造函数, 初始化12 个按钮, 设定位置
            for (int i = 0; i < 12; i++) {
                myButtons[i] = new MyButton(i);
                myButtons[i].addActionListener(new clickChoose(i)); // 给每个玩家设定为相应的选择task
                myButtons[i].refresh();

            }
//            myButtons[getNumber()].setBorderPainted(true);
            myButtons[getNumber()].setForeground(Color.red); // 自己的按钮特殊处理
//            myButtons[getNumber()].setBackground(Color.GREEN);
//            URL url = ImageIcon.class.getResource("Myself.png");
//            Icon icon = new ImageIcon(url);
//            myButtons[getNumber()].setIcon(icon);
            for (int i = 0; i < 4; i++) {
                north.add(myButtons[i]); // 添加前四个按钮
            }
            for (int i = 4; i < 6; i++) {
                east.add(myButtons[i]); // 右边两个按钮
            }
            JPanel south1 = new JPanel(new GridLayout(1, 2, 20, 5)); // 放确定取消键
            JPanel south2 = new JPanel(new GridLayout(1, 4, 20, 5));
            south.add(south1);
            center.add(centerConfirm);
            yes.setSize(50, 20);
            no.setSize(50, 20);
            yes.addActionListener(new clickConfirm());
            no.addActionListener(new clickCancel());
            yes.setBorderPainted(false); // 不显示边框
            no.setBorderPainted(false); // 不显示边框
            south.add(south1);
            yes2.setSize(50, 20);
            no2.setSize(50, 20);
            chooseConfirm.setSize(50, 20);
            yes2.addActionListener(new Yes2());
            no2.addActionListener(new No2());
            chooseConfirm.addActionListener(new ChooseMade());
            yes2.setBorderPainted(false); // 不显示边框
            no2.setBorderPainted(false); // 不显示边框
            chooseConfirm.setBorderPainted(false);
            centerConfirm.add(yes2);
            centerConfirm.add(no2);
            centerConfirm.add(chooseConfirm);
            centerConfirm.setVisible(false); // 默认不可见
            south1.add(yes);
            south1.add(no);
            for (int i = 9; i > 5; i--) {
                south2.add(myButtons[i]); // 倒序添加下面四个按钮
            }
            south.add(south2);
            for (int i = 11; i > 9; i--) {
                west.add(myButtons[i]); // 倒序添加左边两个按钮
            }
            closeAll();
        }

        public void refreshAll() { // 根据未离场玩家刷新按钮
            yes.setEnabled(true);
            no.setEnabled(true);
            for (int i : Alive.intPlayers) {
                myButtons[i].refresh();
            }
        }

        public void refresh(ArrayList<Integer> list) { // 根据特定数组刷新按钮
            yes.setEnabled(true);
            no.setEnabled(true);
            for (int i : list) {
                myButtons[i].refresh(list);
            }
        }

        public void closeAll() { // 关闭所有玩家按钮
            yes.setEnabled(false);
            no.setEnabled(false);
            for (MyButton one : myButtons)
                one.setEnabled(false);
        }

        public MyButton getButton(int i){ // 取得某个按钮
            return myButtons[i];
        }

        public int getKey() { // 根据按钮获取投票结果
            refreshAll();
            key = -1;
            num = -1;
//            float time = System.currentTimeMillis(); // 当前时间
//            close = false; // 没有答案
//            while (!close
////                    && (System.currentTimeMillis()-time) > 10000
//                    ) // 已经有了答案或者超过时间
//                ;
            delay = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        delay.sleep(100000); // 十秒投票时间
                    } catch (InterruptedException e) {

                    }finally {
                        if (Alive.intPlayers.contains(num))
                            sendMessage((num +1)+"号玩家, 已提交\n");
                        else
                            sendMessage("已弃票");
                    }
                }
            });
            delay.start();
            try {
                delay.join();
            } catch (InterruptedException e) {
                System.out.println("getKey()出现问题");
            }
            closeAll();
            return key;
        }

        public int getKey(ArrayList<Integer> list) { // 根据按钮获取投票结果
            refresh(list); // 仅开启数组中的按钮
            sendMessage("请选择目标玩家, 10秒后自动提交\n");
            key = -1; // 什么操作都没做, 提交-1, 点了放弃提交-2, 否则提交正常玩家座次号
            num = -1;
            float time = System.currentTimeMillis(); // 当前时间
            close = false; // 没有答案
//            while (!close
////                    && (System.currentTimeMillis()-time) > 10000
//                    ) // 已经有了答案或者超过时间
//                ;
            delay = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        delay.sleep(100000); // 100秒投票时间
                    } catch (InterruptedException e) {

                    }
                    finally {
                        if (list.contains(num)) {
                            sendMessage((num + 1) + "号玩家, 已提交\n");
                        }
                        else
                            sendMessage("已弃票");
                    }
                }
            });
            delay.start();
            try {
                delay.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("getKey()出现问题");
            }
            closeAll();
            return key;
        }

        public boolean Choose(){ // 是与否的选择选择函数

            closeAll();
            choose = false;
            centerConfirm.setVisible(true); // 先将面板设置为可见
            sendMessage("点击 是 / 否 按钮, 确认请点提交\n请在十秒钟内做出选择\n");
            delay = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        delay.sleep(100000); // 十秒投票时间
                    } catch (InterruptedException e) {

                    }
                }
            });
            delay.start();
            try {
                delay.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            chooseMade = false;
            centerConfirm.setVisible(false); // 再次将该面板设置为不可见
            return choose;

        }

        class clickChoose implements ActionListener { // 玩家按钮的动作
            private final int temp; // 每个按钮对应的玩家座次号

            public clickChoose(int i) {

                temp = i;

            }

            @Override
            public void actionPerformed(ActionEvent e) {
                num = temp; // 将key设定为点击的玩家序号
                jTextArea.append("选择" + (num + 1) + "号玩家, 确定吗\n");

            }
        }

        class clickConfirm implements ActionListener { // 确定按钮动作
            @Override
            public void actionPerformed(ActionEvent e) {
                key = num; // 将key设定为点击的玩家序号

                if (key >= 0) { // 进行了选择
                    jTextArea.append("选择" + (num + 1) + "号玩家\n");
                    close = true; // 可以结束了
                    closeAll(); // 关闭所有按钮
                    delay.interrupt();
                }
                else {
                    if(num == -2) {// 弃票
                        jTextArea.append("选择放弃\n");
                        close = true; // 可以结束了
                        closeAll(); // 关闭所有按钮
                        delay.interrupt();
                    }
                    else
                        jTextArea.append("还未选择\n");
                }

            }
        }
        class clickCancel implements ActionListener { // 取消按钮动作
            @Override
            public void actionPerformed(ActionEvent e) {
                num = -2; // 将key设定为点击的玩家序号
                jTextArea.append("重置选择\n点击确定视为弃票\n");
            }
        }
        class Yes2 implements ActionListener{ // yes2动作
            @Override
            public void actionPerformed(ActionEvent e){
                jTextArea.append("\n选择是, 确认请点提交提交\n");
                choose = true;
                chooseMade = true;
//                delay.interrupt();
            }
        }
        class No2 implements ActionListener{ // no2动作
            @Override
            public void actionPerformed(ActionEvent e){
                jTextArea.append("\n选择否, 确认请点提交提交\n");
                choose = false;
                chooseMade = true;
            }
        }
        class ChooseMade implements ActionListener{ // 提交动作
            @Override
            public void actionPerformed(ActionEvent e){
                if (chooseMade == true){ // 做出了选择
                    jTextArea.append("已提交\n");
                    // 此时还未重置2个choose关键字
                    delay.interrupt();
                }
                else {
                    jTextArea.append("未作出选择, 先点击是 或者 否做出选择\n");
                }
            }
        }
    }
}