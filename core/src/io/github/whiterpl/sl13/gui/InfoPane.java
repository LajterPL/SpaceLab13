package io.github.whiterpl.sl13.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import io.github.whiterpl.sl13.player.PlayerController;
import io.github.whiterpl.sl13.player.Skill;

public class InfoPane extends Table {

    protected Label statLabel;
    protected Label messageLog;

    public InfoPane(BitmapFont font, NinePatchDrawable border) {

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;

        statLabel = new Label("Character stats", labelStyle);
        statLabel.setFontScale(0.7F);

        messageLog = new Label("", labelStyle); //33x55
        messageLog.setFontScale(0.5F);

        this.background(border);
        this.add(statLabel).expandX().height(Gdx.graphics.getHeight()*0.2F).top().left().pad(5);
        this.row();
        this.add(messageLog).expand().bottom().left().pad(5);
    }

    public void updateStats(String name, short hp, short maxHp, short sp, short maxSp, short characterLvl, int exp, short[] skills) {
        StringBuilder sb = new StringBuilder();
        sb.append("Character stats\n\n");
        sb.append(String.format("Name: [#f2ee02]%-15s[]%n%n", name));
        sb.append(String.format("[#FF0000]HP[]: %3d/%-3d     Level: %-3d%n", hp, maxHp, characterLvl));
        sb.append(String.format("[#0206F2]SP[]: %3d/%-3d     EXP: %-3d%n%n", sp, maxSp, exp));
        sb.append(String.format("MEL: %-4d RAN: %-4d WPR: %-4d%n", skills[Skill.MELEE.getIndex()], skills[Skill.RANGED.getIndex()], skills[Skill.WILLPOWER.getIndex()]));
        sb.append(String.format("TGH: %-4d STR: %-4d TKR: %-4d%n", skills[Skill.TOUGHNESS.getIndex()], skills[Skill.STRENGTH.getIndex()], skills[Skill.TINKERING.getIndex()]));
        sb.append(String.format("IDT:  %-4d HAK: %-4d NEG: %-4d%n", skills[Skill.IDENTIFYING.getIndex()], skills[Skill.HACKING.getIndex()], skills[Skill.NEGOTIATION.getIndex()]));
        statLabel.setText(sb.toString());
    }

    public void updateStats(PlayerController playerController) {
        updateStats(
                playerController.getPlayer().getName(),
                playerController.getPlayer().getCurrentHp(),
                playerController.getPlayer().getMaxHp(),
                playerController.getPlayer().getCurrentSp(),
                playerController.getPlayer().getMaxSp(),
                playerController.getCharacterLevel(),
                playerController.getCurrentExp(),
                playerController.getPlayer().getSkills());
    }

    public void appendMessage(String message) {

        int slimChars = (int) message.chars().filter(c -> "ilILt.',;:!?-~ ".contains(String.valueOf((char) c))).count();
        if (message.length() - slimChars/2 > 33) {
            int slimCharsAtBeginning = (int) (message.chars().limit(33).filter(c -> "ilILt.',;:!?-~ ".contains(String.valueOf((char) c))).count()/2);
            appendMessage(message.substring(0, 33 + slimCharsAtBeginning));
            appendMessage(message.substring(33 + slimCharsAtBeginning));
            return;
        }

        if (messageLog.getText().toString().split("\n").length >= 54) {
            for (int i = 0; i < messageLog.getText().toString().split("\n").length - 54; i++) {
                messageLog.setText(messageLog.getText().substring(messageLog.getText().indexOf("\n", 1)));
            }
        }

        if (messageLog.getText().toString() != "")  messageLog.setText(messageLog.getText().append('\n'));

        messageLog.setText(messageLog.getText().append(message).toString() + " ");
        messageLog.setText(messageLog.getText().substring(0, messageLog.getText().length-1));
    }
}
