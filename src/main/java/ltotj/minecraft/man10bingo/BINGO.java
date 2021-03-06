package ltotj.minecraft.man10bingo;

import ltotj.minecraft.man10bingo.game.*;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class BINGO {

    boolean canEdit=true,isRunning=true,canRoll=true;
    HashMap<UUID,BINGOData> playerList=new HashMap<>();
    Deck deck=new Deck();
    DrawingThread thread;
    int maxLine=1;
    List<UUID> exceptList=new ArrayList<>();

    BINGO(int maxLine){
        this.maxLine=maxLine;
    }


    class BINGOData{
        BINGOCard bingoCard;
        EditBoard editBoard;
        UUID uuid;
        String name;
        boolean isViewing;

        BINGOData(Player player){
            bingoCard=new BINGOCard(player);
            editBoard=new EditBoard(player);
            uuid=player.getUniqueId();
            name=player.getName();
        }

        public void setNumber(int num){
            if(bingoCard.getEditPosNum()!=0){
                editBoard.removeUsedIcon(bingoCard.getEditPosNum());
                bingoCard.panelPositions.remove(num);
            }
            bingoCard.editPanel(num);
            editBoard.putUsedIcon(num);
            bingoCard.reloadPanel();
        }

        public void setRandomNum(){
            bingoCard.putRandomPanels();
            editBoard.reset();
            for(int i=0;i<5;i++)for(int j=0;j<5;j++){
                if(i==2&&j==2)continue;
                editBoard.putUsedIcon(bingoCard.panels[i][j]);
            }
        }
    }

    private void broadcastMessage(String message){
        Bukkit.getServer().broadcast(Component.text(message), Server.BROADCAST_CHANNEL_USERS);
    }

    public boolean addPlayer(Player player){
        if(canEdit&&!playerList.containsKey(player.getUniqueId())) {
            playerList.put(player.getUniqueId(), new BINGOData(player));
            return true;
        }
        return false;
    }

    public void startBINGO(){
        canEdit=false;
        broadcastMessage("??a??l????????????????????e"+maxLine+"??a??l?????????????????????????????????");
    }

    public boolean rollBINGO(){
        if(canEdit||!canRoll)return false;
        thread=new DrawingThread();
        thread.start();
        return true;
    }

    class DrawingThread extends Thread{

        private void threadSleep(int time){
            try {
                sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private boolean openInv(Player player, Inventory inv){
            if(player==null)return false;
            else if(inv.getViewers().contains((HumanEntity) player))return true;
            Bukkit.getScheduler().runTask(Main.getPlugin(Main.class),new Runnable(){
                @Override
                public void run(){
                    player.openInventory(inv);
                }
            });
            return false;
        }

        private void closeInv(Player player){
            if(player==null)return;
            Bukkit.getScheduler().runTask(Main.getPlugin(Main.class),new Runnable(){
                @Override
                public void run(){
                    player.closeInventory();
                }
            });
        }

        private void playSoundPl(Player player, Sound sound,float pitch){
            if(player==null)return;
            player.playSound(player.getLocation(),sound,2,pitch);
        }

        @Override
        public void run(){
            canRoll=false;
            List<UUID> newLinePlList=new ArrayList<>(),newLi_zhiList=new ArrayList<>();
            int num=deck.draw(),count=0;

            broadcastMessage("??a??l?????????????????????????????????????????c??k");
            threadSleep(2000);

            broadcastMessage("??e"+num+"??a??l???????????c"+GlobalClass.config.getString("items."+num+".name")+"??a??l?????????????????????");
            threadSleep(2000);
            for(BINGOData bingoData:playerList.values()){
                bingoData.isViewing=openInv(Bukkit.getPlayer(bingoData.uuid),bingoData.bingoCard.inv.inv);
            }
            threadSleep(1000);
            for(BINGOData bingoData:playerList.values()){
                if(bingoData.bingoCard.openPanel(num)){
                    count+=1;
                    boolean[] bo=bingoData.bingoCard.getNewLine();
                    if(bo[0])newLi_zhiList.add(bingoData.uuid);
                    if(bo[1])newLinePlList.add(bingoData.uuid);
                    playSoundPl(Bukkit.getPlayer(bingoData.uuid),Sound.BLOCK_BEACON_ACTIVATE,1);
                }
                else playSoundPl(Bukkit.getPlayer(bingoData.uuid),Sound.BLOCK_BEACON_DEACTIVATE,0);
            }
            threadSleep(2000);
            broadcastMessage("??a??l?????????????????????????????????????????????????????e"+count+"??a??l???????????????");
            for(BINGOData bingoData:playerList.values()) {
                if(!bingoData.isViewing){
                    closeInv(Bukkit.getPlayer(bingoData.uuid));
                }
            }
            threadSleep(700);
            for(UUID uuid:newLi_zhiList){
                if(exceptList.contains(uuid))continue;
                BINGOData bingoData=playerList.get(uuid);
                broadcastMessage("??6"+bingoData.name+"??a??l??????????????d"+bingoData.bingoCard.li_zhi+"???????????a??l???????????????");
            }
            for(UUID uuid:newLinePlList){
                if(exceptList.contains(uuid))continue;
                BINGOData bingoData=playerList.get(uuid);
                if(bingoData.bingoCard.lines<maxLine)broadcastMessage("??6"+bingoData.name+"??a??l?????c"+bingoData.bingoCard.lines+"???????????a??l????????????????????????");
                else{
                    broadcastMessage("??cB ??dI ??9N ??eG ??cO ??a??l?????6"+bingoData.name+"??a??l?????g"+maxLine+"???????????a??l????????????????????????");
                    exceptList.add(uuid);
                }
            }
            canRoll=true;
        }
    }
}
