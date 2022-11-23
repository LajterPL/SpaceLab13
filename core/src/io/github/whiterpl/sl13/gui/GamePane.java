package io.github.whiterpl.sl13.gui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import io.github.whiterpl.sl13.atoms.region.Region;
import io.github.whiterpl.sl13.atoms.region.Tile;

public class GamePane extends Table {

    private Label gameScreen;

    public GamePane(BitmapFont font, NinePatchDrawable border) {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        gameScreen = new Label("50x36", labelStyle);
        gameScreen.setWrap(true);

        this.add(gameScreen).expand().center().left().pad(12);
        this.background(border);
    }

    public void updateGameScreen(Region activeRegion, int x, int y) {

        x = x < 25 ? 25 : Math.min(x, Region.WIDTH - 25);
        y = y < 18 ? 18 : Math.min(y, Region.HEIGHT - 18);

        StringBuilder sb = new StringBuilder();

        for (int iY = y - 18; iY < y + 18; iY++) {
            for (int iX = x - 25; iX < x + 25; iX++) {
                Tile temp = activeRegion.getTile(iX, iY);
                if (temp == null) sb.append(" ");
                else {
                    if (temp.getMob() != null) {
                        sb.append("[#");
                        sb.append(temp.getMob().getColorString());
                        sb.append(']');
                        sb.append(temp.getMob().getSymbol());
                        sb.append("[]");
                    } else if (temp.getStructure() != null) {
                        sb.append("[#");
                        sb.append(temp.getStructure().getColorString());
                        sb.append(']');
                        sb.append(temp.getStructure().getSymbol());
                        sb.append("[]");
                    } else if (!temp.getItems().isEmpty()) {
                        sb.append("[#");
                        sb.append(temp.getItems().get(0).getColorString());
                        sb.append(']');
                        sb.append(temp.getItems().get(0).getSymbol());
                        sb.append("[]");
                    } else {
                        sb.append(" ");
                    }
                }
            }
            if (iY != y + 17) sb.append("\n");
        }

        gameScreen.setText(sb.toString());

    }
}
