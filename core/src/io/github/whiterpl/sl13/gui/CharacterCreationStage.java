package io.github.whiterpl.sl13.gui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import io.github.whiterpl.sl13.player.PlayerController;

public class CharacterCreationStage extends Stage {
    private final Label nameLabel;
    private SkillTable[] skillTables;
    private Label pointsLabel;
    int pointsToSpend;

    public CharacterCreationStage(BitmapFont font, NinePatchDrawable border) {

        this.pointsToSpend = 36;

        Table rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.center();
        rootTable.background(border);
        rootTable.pad(10);
        this.addActor(rootTable);

        font.getData().markupEnabled = true;
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;


        nameLabel = new Label("[#f2ee02]Name[]: Janek", labelStyle);
        rootTable.add(nameLabel).height(32).expandX().top().center().padTop(30).colspan(3);
        rootTable.row();

        skillTables = new SkillTable[]{
                new SkillTable(font, "[#d1160c]Fighting[]", "Melee", "Ranged", "Willpower"),
                new SkillTable(font, "[#e04e0b]Labour[]", "Toughness", "Strength", "Tinkering"),
                new SkillTable(font, "[#fc0af8]Science[]", "Identifying", "Hacking", "Negotiation")
        };

        for (SkillTable skillTable : skillTables) {
            rootTable.add(skillTable).top().center().expandY();
        }

        rootTable.row();

        pointsLabel = new Label(String.format("Points to spend: %d", pointsToSpend), labelStyle);
        rootTable.add(pointsLabel).colspan(3).center().padBottom(30);

        rootTable.row();
        Label instructionLabel = new Label("left/right arrow - move\nup/down arrow - add/subtract points\nr - random point allocation", labelStyle);
        instructionLabel.setFontScale(0.5f);
        rootTable.add(instructionLabel).colspan(3).pad(20);

        getPlayerController();
    }

    public PlayerController getPlayerController() {
        String name = nameLabel.getText().substring(17);

        short[] skills = new short[9];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                skills[i*3+j] = skillTables[i].getValue(j);
            }
        }

        return new PlayerController(name, skills);
    }

    public void addPoint(int selectionIndex) {
        if (selectionIndex == 0 || pointsToSpend == 0) return;
        int groupIndex = (int) Math.floor((selectionIndex - 1)/3f);
        if (skillTables[groupIndex].getValue((selectionIndex - 1) - (3*groupIndex)) == 9) return;
        pointsToSpend--;
        pointsLabel.setText(String.format("Points to spend: %2d", pointsToSpend));
        skillTables[groupIndex].addPoint((selectionIndex - 1) - (3*groupIndex));
    }

    public void removePoint(int selectionIndex) {
        if (selectionIndex == 0) return;
        int groupIndex = (int) Math.floor((selectionIndex - 1)/3f);
        if (skillTables[groupIndex].getValue((selectionIndex - 1) - (3*groupIndex)) == 0) return;
        pointsToSpend++;
        pointsLabel.setText(String.format("Points to spend: %2d", pointsToSpend));
        skillTables[groupIndex].removePoint((selectionIndex - 1) - (3*groupIndex));
    }

    public void addCharToName(char c) {
        if (nameLabel.getText().length < 33) nameLabel.setText(nameLabel.getText().toString() + c);
    }

    public void removeCharFromName() {
        String name = nameLabel.getText().toString();

        if (name.length() > 17) nameLabel.setText(name.substring(0, name.length() - 1));
    }

    public void selectLabel(int selectionIndex, int oldIndex) {
        if (selectionIndex == 0) {
            // Z jakiegoś powodu Label aktualizuje kolory zapisane za pomocą color markupu tylko kiedy dopisze się coś do niego, dlatego dodaję spację, którą potem od razu usuwam
            nameLabel.setText(nameLabel.getText().replace("ffffff", "f2ee02").toString() + " ");
            nameLabel.setText(nameLabel.getText().substring(0, nameLabel.getText().length-1));
        }
        else {
            int groupIndex = (int) Math.floor((selectionIndex - 1)/3f);
            skillTables[groupIndex].selectSkill((selectionIndex-1) - (3*groupIndex));
        }

        if (oldIndex == 0) {
            nameLabel.setText(nameLabel.getText().replace("f2ee02", "ffffff").toString() + " ");
            nameLabel.setText(nameLabel.getText().substring(0, nameLabel.getText().length-1));
        }
        else {
            int groupIndex = (int) Math.floor((oldIndex - 1)/3f);
            skillTables[groupIndex].unselectSkill((oldIndex - 1) - (3*groupIndex));
        }

        nameLabel.setText(nameLabel.getText().toString());
    }
}
