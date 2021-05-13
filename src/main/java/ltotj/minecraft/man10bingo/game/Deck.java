package ltotj.minecraft.man10bingo.game;

import ltotj.minecraft.man10bingo.InventoryGUI;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Deck {

    List<Integer> deck=new ArrayList<>();
    Random random=new Random();
    InventoryGUI drawnGUI=new InventoryGUI(54,"既に出た数字");

    public Deck() {
        for(int i=1;i<76;i++)deck.add(i);
    }

    public int draw(){
        int r=random.nextInt(deck.size()),ins=deck.get(r);
        deck.remove(r);
        return ins;
    }


}
