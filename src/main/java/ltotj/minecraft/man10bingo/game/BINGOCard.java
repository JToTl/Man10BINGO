package ltotj.minecraft.man10bingo.game;

import ltotj.minecraft.man10bingo.InventoryGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class BINGOCard {

    Player player;
    public InventoryGUI inv;
    public int[][] panels=new int[5][5];
    int[] row=new int[]{0,0,1,0,0},column=new int[]{0,0,1,0,0};
    public int lines=0,editPos,li_zhi=0;
    int preNum,diagonalUP=1,diagonalDOWN=1;
    public HashMap<Integer,Integer> panelPositions=new HashMap<>();//アイテムから場所(２桁)
    List<Integer> openedPanels=new ArrayList<>();

    public BINGOCard(Player player){
        this.player=player;
        for(int i=0;i<5;i++)for(int j=0;j<5;j++)panels[i][j]=0;
        inv=new InventoryGUI(54,player.getName()+"のBINGOカード");
        for(int i=0;i<9;i++)inv.setItem(45+i,Material.WHITE_STAINED_GLASS_PANE,"");
        inv.setItem(49,Material.BLACK_STAINED_GLASS_PANE,"§dボードをランダムに作成する");
        putPanels();
        panelPositions.put(100,22);
    }

    public void putPanels(){
        for(int i=0;i<5;i++)for(int j=0;j<5;j++){
            if(i!=2||j!=2)inv.setPanel(9*i+j+2,panels[i][j]);
        }
        inv.setItem(22,Material.NETHER_STAR,"フリースポット");
    }

    public void reloadPanel(){
        inv.setPanel(9*(editPos/10) +2 +editPos-10*(editPos/10),panels[editPos/10][editPos-10*(editPos/10)]);
    }

    public void putRandomPanels(){
        panelPositions=new HashMap<>();
        Random random=new Random();
        List<Integer> list=new ArrayList<>();
        for(int i=1;i<76;i++)list.add(i);
        for(int i=0;i<5;i++)for(int j=0;j<5;j++)if(i!=2||j!=2){
            int ins=random.nextInt(list.size());
            panels[i][j]=list.get(ins);
            panelPositions.put(panels[i][j],10*i+j);
            list.remove(ins);
        }
        putPanels();
    }

    public void editPanel(int num){
        if(!panelPositions.containsKey(num)){
            int ins=panels[editPos/10][editPos-10*(editPos/10)];
            panels[editPos/10][editPos-10*(editPos/10)]=num;
            panelPositions.remove(ins);
        }
    }

    public int getEditPosNum(){
        return panels[editPos/10][editPos-10*(editPos/10)];
    }

    public boolean openPanel(int num){
        if(panelPositions.containsKey(num)){
            int pos=9*(panelPositions.get(num)/10)+(panelPositions.get(num)-10*(panelPositions.get(num)/10))+2;
            inv.enchantItem(pos);
            inv.addLore(pos,"§c§l解放済み");
            preNum=panelPositions.get(num);
            return true;
        }
        return false;
    }

    public boolean[] getNewLine(){
        if(preNum==-1)return new boolean[]{false,false};
        int count=0,li_count=0;
        row[preNum/10]+=1;
        column[preNum-10*(preNum/10)]+=1;
        if((preNum-preNum/10)%10==0){
            diagonalDOWN+=1;
            if(diagonalDOWN==5)count+=1;
            else if(diagonalDOWN==4)li_count+=1;
        }
        else if((preNum/10 + 10*(preNum/10))==5){
            diagonalUP+=1;
            if(diagonalUP==5)count+=1;
            else if(diagonalUP==4)li_count+=1;
        }
        if(row[preNum/10]==5)count+=1;
        else if(row[preNum/10]==-4)li_count+=1;
        if(column[preNum-10*(preNum/10)]==5)count+=1;
        else if(column[preNum-10*(preNum/10)]==4)li_count+=1;
        li_zhi=li_count;
        lines+=count;
        return new boolean[]{li_count!=0,count!=0};
    }
}
