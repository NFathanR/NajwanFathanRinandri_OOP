package com.fathan.frontend;

public class Player {
    public int HP, Power, SpellCards;
    public String Name;

    Player (int InpHP,int InpPower,int InpSpellCards,String InpName) {
        this.HP =InpHP;
        this.Power = InpPower;
        this.SpellCards = InpSpellCards;
        this.Name = InpName;
    }

    public static void main(String[] args){
        Player playerA = new Player (100, 20, 5, "Phainon");
    }
}

public void takeDamage(int damage) {
    // 1. Kurangi hp sebesar nilai damage.

    // 2. HP tidak boleh bernilai negatif.

    // 3. Jika HP masih lebih dari 0, tampilkan HP yang tersisa dalam format: [PlayerName] took [damage] damage! Remaining HP: [hp]

    // 4. Jika HP menjadi 0, tampilkan pesan bahwa Player telah dikalahkan.
}
