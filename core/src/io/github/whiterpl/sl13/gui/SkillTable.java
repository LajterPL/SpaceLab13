package io.github.whiterpl.sl13.gui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class SkillTable extends Table {
    protected Label title;
    protected Label[] skillLabels;
    protected Label[] valueLabels;

    public SkillTable(BitmapFont font, String title, String skill1, String skill2, String skill3) {

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;

        this.title = new Label(title, labelStyle);
        this.add(this.title).top().center().colspan(2);
        this.row();

        this.valueLabels = new Label[] {
                new Label("0",labelStyle),
                new Label("0",labelStyle),
                new Label("0",labelStyle)
        };

        this.skillLabels = new Label[]{
                new Label(String.format("[#ffffff]%s[]:", skill1), labelStyle),
                new Label(String.format("[#ffffff]%s[]:", skill2), labelStyle),
                new Label(String.format("[#ffffff]%s[]:", skill3), labelStyle)
        };

        for (int i = 0; i < 3; i++) {
            this.add(skillLabels[i]).top().left().pad(10);
            this.add(valueLabels[i]).top().left().pad(10).padLeft(40);
            this.row();
        }
    }

    public void updateSkill(int index, short newValue) {
        valueLabels[index].setText(String.valueOf(newValue));
    }

    public void addPoint(int index) {
        updateSkill(index, (short) (getValue(index) + 1));
    }

    public void removePoint(int index) {
        updateSkill(index, (short) (getValue(index) - 1));
    }

    public void selectSkill(int index) {
        skillLabels[index].setText(skillLabels[index].getText().replace("ffffff", "f2ee02").toString() + " ");
        skillLabels[index].setText(skillLabels[index].getText().substring(0, skillLabels[index].getText().length-1));
    }

    public void unselectSkill(int index) {
        skillLabels[index].setText(skillLabels[index].getText().replace("f2ee02", "ffffff").toString() + " ");
        skillLabels[index].setText(skillLabels[index].getText().substring(0, skillLabels[index].getText().length-1));
    }

    public short getValue(int index) {
        return Short.parseShort(valueLabels[index].getText().toString());
    }
}
