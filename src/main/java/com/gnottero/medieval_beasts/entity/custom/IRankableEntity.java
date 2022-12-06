package com.gnottero.medieval_beasts.entity.custom;

import com.gnottero.medieval_beasts.util.EntityRanking;
import net.minecraft.crash.ReportedException;
import net.minecraft.nbt.CompoundNBT;
import org.apache.commons.lang3.EnumUtils;

import static com.gnottero.medieval_beasts.util.EntityRanking.Ranking;

public interface IRankableEntity {

    int getFoodTime();
    int getSkillCooldown();
    double getDefaultHealth();
    Ranking getRank();

    void setFoodTime(int time);
    void setSkillCooldown(int time);
    void setRank(Ranking rank);

    default void writeFoodTimeNbt(CompoundNBT nbt) {
        nbt.putInt("FoodTime", this.getFoodTime());
    }

    default void writeSkillCooldownNbt(CompoundNBT nbt) {
        nbt.putInt("SkillCooldown", this.getSkillCooldown());
    }

    default void writeRankNbt(CompoundNBT nbt) {
        nbt.putString("Rank", this.getRank().getName());
    }

    default void readFoodTimeNbt(CompoundNBT nbt) {
        this.setFoodTime(nbt.getInt("FoodTime"));
    }

    default void readSkillCooldown(CompoundNBT nbt) {
        this.setSkillCooldown(nbt.getInt("SkillCooldown"));
    }

    default void readRankNbt(CompoundNBT nbt) {
        String nbtRank = nbt.getString("Rank");
        if(EnumUtils.isValidEnum(Ranking.class, nbtRank)) {
            this.setRank(Ranking.valueOf(nbtRank));
        } else {
            this.setRank(Ranking.F);
        }
    }

    default void updateFoodTime() {
        if (this.getFoodTime() > 0) {
            this.setFoodTime(this.getFoodTime() - 1);
        }
    }

    default void updateSkillCooldown() {
        if (this.getFoodTime() > 0) {
            if (this.getSkillCooldown() > 0) {
                this.setSkillCooldown(this.getSkillCooldown() - 1);
            }
        } else {
            this.setSkillCooldown(this.getRank().getCooldown());
        }
    }
}
